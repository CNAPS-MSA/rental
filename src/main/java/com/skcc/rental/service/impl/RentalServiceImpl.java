package com.skcc.rental.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.rental.adaptor.BookClient;
import com.skcc.rental.adaptor.RentalProducer;
import com.skcc.rental.adaptor.UserClient;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.domain.event.UserIdCreated;
import com.skcc.rental.repository.RentalRepository;
import com.skcc.rental.repository.RentedItemRepository;
import com.skcc.rental.repository.ReturnedItemRepository;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.web.rest.dto.BookInfoDTO;
import com.skcc.rental.web.rest.dto.LatefeeDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service Implementation for managing {@link Rental}.
 */
@Service
@Transactional
public class RentalServiceImpl implements RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalServiceImpl.class);

    private final RentalRepository rentalRepository;

    private final RentalProducer rentalProducer;

    private int pointPerBooks = 30;

    public RentalServiceImpl(RentalRepository rentalRepository, RentalProducer rentalProducer) {
        this.rentalRepository = rentalRepository;
        this.rentalProducer = rentalProducer;

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

    public Rental createRental(UserIdCreated userIdCreated) {
        Rental rental = Rental.createRental(userIdCreated.getUserId());
        rentalRepository.save(rental);
        return rental;
    }

    /**
     * 여러권 대여하기
     *
     * @param userId
     * @param book
     * @return
     */
    @Transactional
    public RentedItem rentBook(Long userId, BookInfoDTO book) {
        log.debug("Rent Books by : ", userId, " Book List : ", book);
        Rental rental = rentalRepository.findByUserId(userId).get();
        RentedItem rentedItem = new RentedItem();
        try {
            Boolean checkRentalStatus = rental.checkRentalAvailable();
            if (checkRentalStatus) {
                rental = rental.rentBook(book.getId(), book.getTitle());
                rentalRepository.save(rental);

                try{
                    updateBookStatus(book.getId(), "UNAVAILABLE");
                    updateBookCatalog(book.getId(), "RENT_BOOK");
                } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                    e.printStackTrace();
                }

                savePoints(userId);
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            System.out.println(errorMessage);
            return null;
        }
        return rentedItem;
    }

    @Override
    public Optional<Rental> findRentalByUser(Long userId) {
        return rentalRepository.findByUserId(userId);
    }

    /**
     * 여러 권 반납하기
     *
     * @param userId
     * @param bookId
     * @return
     */
    @Transactional
    public Rental returnBook(Long userId, Long bookId) {
        log.debug("Return books by ", userId, " Return Book List : ", bookId);
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental = rental.returnbook(bookId);
        rental = rentalRepository.save(rental);


        try {
                updateBookStatus(bookId, "AVAILABLE");
                updateBookCatalog(bookId, "RETURN_BOOK");
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
        }

        return rental;
    }

    /**
     * 연체된 책 반납하기
     *
     * @param userid
     * @param book
     * @return
     */
    @Override
    public Rental returnOverdueBooks(Long userid, Long book) {
        Rental rental = rentalRepository.findByUserId(userid).get();

        rental = rental.returnOverdueBook(book);
        try {
            updateBookStatus(book, "AVAILABLE");
            updateBookCatalog(book, "RETURN_BOOK");
        } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
            e.printStackTrace();
        }

        return rentalRepository.save(rental);
    }



    @Override
    public Rental releaseOverdue(Long userId) {
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental=rental.releaseOverdue(rental.getLateFee());
        return rentalRepository.save(rental);
    }

    @Override
    public LatefeeDTO getLatefee(Long userId) {
        LatefeeDTO latefeeDTO = new LatefeeDTO();
        latefeeDTO.setLatefee(rentalRepository.findByUserId(userId).get().getLateFee());
        latefeeDTO.setUserId(userId);
        return latefeeDTO;
    }

    @Override
    public void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException {
        rentalProducer.updateBookStatus(bookId, bookStatus);
    }

    @Override
    public void savePoints(Long userId) throws ExecutionException, InterruptedException, JsonProcessingException {
        rentalProducer.savePoints(userId, pointPerBooks);
    }

    @Override
    public void updateBookCatalog(Long bookId, String eventType) throws InterruptedException, ExecutionException, JsonProcessingException {
        rentalProducer.updateBookCatalogStatus(bookId, eventType);
    }

    @Override
    public Long beOverdueBooks(Long rentalId, Long bookId) {
        Rental rental = rentalRepository.findById(rentalId).get();
        rental= rental.overdueBook(bookId);
        rental = rental.makeRentUnable();
        rentalRepository.save(rental);
        return bookId;
    }




}
