package com.skcc.rental.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hazelcast.client.impl.protocol.codec.ReplicatedMapAddEntryListenerCodec;
import com.skcc.rental.adaptor.RentalKafkaProducer;
import com.skcc.rental.domain.OverdueItem;
import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.domain.enumeration.RentalStatus;
import com.skcc.rental.repository.RentedItemRepository;
import com.skcc.rental.repository.ReturnedItemRepository;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.repository.RentalRepository;
import com.skcc.rental.web.rest.dto.BookInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.print.PrintException;
import java.awt.print.Book;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.skcc.rental.domain.RentedItem.createRentedItem;

/**
 * Service Implementation for managing {@link Rental}.
 */
@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentedItemRepository rentedItemRepository;

    private final ReturnedItemRepository returnedItemRepository;

    private final RentalKafkaProducer rentalKafkaProducer;

    private int pointPerBooks = 30;

    public RentalServiceImpl(RentalRepository rentalRepository, RentedItemRepository rentedItemRepository, ReturnedItemRepository returnedItemRepository,
                             RentalKafkaProducer rentalKafkaProducer) {
        this.rentalRepository = rentalRepository;
        this.rentedItemRepository = rentedItemRepository;
        this.returnedItemRepository = returnedItemRepository;
        this.rentalKafkaProducer = rentalKafkaProducer;
    }

    /**
     * Save a rental.
     *
     * @param rental
     * @return the persisted entity.
     */
    @Override
    public Rental save(Rental rental) {
        log.debug("Request to save Rental : {}", rental);
        return rentalRepository.save(rental);
    }

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<Rental> findAll(Pageable pageable) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll(pageable);
    }

    /**
     * Get one rental by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Rental> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id);
    }

    /**
     * Delete the rental by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }

    @Transactional
    public Rental rentBooks(Long userId, List<BookInfo> books) {
        log.debug("Rent Books by : ", userId, " Book List : ", books);
        Rental rental = rentalRepository.findByUserId(userId).get();


        try{
            Boolean checkRentalStatus = rental.checkRentalAvailable(books.size());
            if(checkRentalStatus){
            List<RentedItem> rentedItems = books.stream()
                .map(bookInfo -> RentedItem.createRentedItem(bookInfo.getId(), bookInfo.getTitle(), LocalDate.now()))
                .collect(Collectors.toList());

            for (RentedItem rentedItem : rentedItems) {
                rental = rental.rentBook(rentedItem);


            }
            rentalRepository.save(rental);


            }

        }catch (Exception e){
            String errorMessage = e.getMessage();
            System.out.println(errorMessage);
            return null;
        }
        return rental;

    }


    @Transactional
    public Rental returnBooks(Long userId, List<Long> bookIds) {
        log.debug("Return books by ", userId, " Return Book List : ", bookIds);
        Rental rental = rentalRepository.findByUserId(userId).get();

        List<RentedItem> rentedItems = rental.getRentedItems().stream()
            .filter(rentedItem -> bookIds.contains(rentedItem.getBookId()))
            .collect(Collectors.toList());
        log.debug("bookIds contain :" , rentedItems.size());

        if(rentedItems.size()>0) {
            for (RentedItem rentedItem : rentedItems) {
                rental= rental.returnbook(rentedItem);
            }

            rental = rentalRepository.save(rental);

            return rental;
        }else{

            return null;
        }

    }

    @Override
    public void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException {
        rentalKafkaProducer.updateBookStatus(bookId, bookStatus);
    }

    @Override
    public void savePoints(Long userId, int bookCnt) throws ExecutionException, InterruptedException, JsonProcessingException{
        rentalKafkaProducer.savePoints(userId, bookCnt*pointPerBooks);
    }

    @Override
    public Rental overdueBooks(Long userId, List<Long> books) {
        Rental rental = rentalRepository.findByUserId(userId).get();

        List<RentedItem> rentedItems = rental.getRentedItems().stream()
            .filter(rentedItem -> books.contains(rentedItem.getBookId()))
            .collect(Collectors.toList());

        if(rentedItems.size()>0){
            for(RentedItem rentedItem: rentedItems) {
                rental = rental.overdueBook(rentedItem);
            }
            rental.setRentalStatus(RentalStatus.RENT_UNAVAILABLE);
            rental.setLateFee(rental.getLateFee()+(long)30); //연체시 연체비 30포인트 누적
            return rentalRepository.save(rental);
        }else{
            return null;
        }



    }

    @Override
    public Rental returnOverdueBooks(Long userid, List<Long> books) {
        Rental rental = rentalRepository.findByUserId(userid).get();

        List<OverdueItem> overdueItems = rental.getOverdueItems().stream()
            .filter(overdueItem -> books.contains(overdueItem.getBookId()))
            .collect(Collectors.toList());

        for(OverdueItem overdueItem:overdueItems){
            rental = rental.returnOverdueBook(overdueItem);
        }
        return rentalRepository.save(rental);
    }

    @Override
    public Rental releaseOverdue(Long userId, int latefee) {
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental=rental.releaseOverdue((long)latefee);
        return rentalRepository.save(rental);
    }


}
