package com.skcc.rental.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.rental.adaptor.BookClient;
import com.skcc.rental.adaptor.RentalProducer;
import com.skcc.rental.adaptor.UserClient;
import com.skcc.rental.domain.Rental;
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

    private final RentedItemRepository rentedItemRepository;

    private final ReturnedItemRepository returnedItemRepository;

    private final RentalProducer rentalProducer;

    private final BookClient bookClient;

    private final UserClient userClient;

    private int pointPerBooks = 30;

    public RentalServiceImpl(RentalRepository rentalRepository, RentedItemRepository rentedItemRepository, ReturnedItemRepository returnedItemRepository,
                             RentalProducer rentalProducer, BookClient bookClient, UserClient userClient) {
        this.rentalRepository = rentalRepository;
        this.rentedItemRepository = rentedItemRepository;
        this.returnedItemRepository = returnedItemRepository;
        this.rentalProducer = rentalProducer;
        this.bookClient = bookClient;
        this.userClient = userClient;
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
     * @param books
     * @return
     */
    @Transactional
    public Rental rentBooks(Long userId, List<BookInfoDTO> books) {
        log.debug("Rent Books by : ", userId, " Book List : ", books);
        Rental rental = rentalRepository.findByUserId(userId).get();
        try {
            Boolean checkRentalStatus = rental.checkRentalAvailable(books.size());
            if (checkRentalStatus) {

                books.forEach(bookInfo -> rental.rentBook(bookInfo.getId(), bookInfo.getTitle()));
                rentalRepository.save(rental);

                books.forEach(b -> {
                    try {
                        updateBookStatus(b.getId(), "UNAVAILABLE");
                        updateBookCatalog(b.getId(), "RENT_BOOK");
                    } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
                savePoints(userId, books.size());
            }
        } catch (Exception e) {
            String errorMessage = e.getMessage();
            System.out.println(errorMessage);
            return null;
        }
        return rental;
    }

    /**
     * 여러 권 반납하기
     *
     * @param userId
     * @param bookIds
     * @return
     */
    @Transactional
    public Rental returnBooks(Long userId, List<Long> bookIds) {
        log.debug("Return books by ", userId, " Return Book List : ", bookIds);
        Rental rental = rentalRepository.findByUserId(userId).get();

        Rental finalRental = rental;
        bookIds.forEach(bookid -> finalRental.returnbook(bookid));
        rental = rentalRepository.save(finalRental);

        bookIds.forEach(b -> {
            try {
                updateBookStatus(b, "AVAILABLE");
                updateBookCatalog(b, "RETURN_BOOK");
            } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return rental;
    }

    /**
     * 연체처리 여러 권
     *
     * @param userId
     * @param books
     * @return
     */
    @Override
    public Rental overdueBooks(Long userId, List<Long> books) {
        Rental rental = rentalRepository.findByUserId(userId).get();

        books.forEach(bookid -> rental.overdueBook(bookid));
        rental.makeRentUnable();
        return rentalRepository.save(rental);
    }


    /**
     * 연체된 책 반납하기 (여러권)
     *
     * @param userid
     * @param books
     * @return
     */
    @Override
    public Rental returnOverdueBooks(Long userid, List<Long> books) {
        Rental rental = rentalRepository.findByUserId(userid).get();

        books.forEach(bookid -> rental.returnOverdueBook(bookid));

        books.forEach(b -> { //책상태 업데이트
            try {
                updateBookStatus(b, "AVAILABLE");
                updateBookCatalog(b, "RETURN_BOOK");
            } catch (ExecutionException | InterruptedException | JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return rentalRepository.save(rental);
    }



    @Override
    public Rental releaseOverdue(Long userId) {
        Rental rental = rentalRepository.findByUserId(userId).get();
        rental=rental.releaseOverdue(rental.getLateFee());
        return rentalRepository.save(rental);
    }

    @Override
    public ResponseEntity payLatefee(Long userId) {
        int latefee = rentalRepository.findByUserId(userId).get().getLateFee();
        LatefeeDTO latefeeDTO = new LatefeeDTO();
        latefeeDTO.setLatefee(latefee);
        latefeeDTO.setUserId(userId);
        ResponseEntity result = userClient.usePoint(latefeeDTO);
        return result;
    }

    @Override
    public void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException {
        rentalProducer.updateBookStatus(bookId, bookStatus);
    }

    @Override
    public void savePoints(Long userId, int bookCnt) throws ExecutionException, InterruptedException, JsonProcessingException {
        rentalProducer.savePoints(userId, bookCnt * pointPerBooks);
    }

    @Override
    public void updateBookCatalog(Long bookId, String eventType) throws InterruptedException, ExecutionException, JsonProcessingException {
        rentalProducer.updateBookCatalogStatus(bookId, eventType);
    }



}
