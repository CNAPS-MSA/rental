package com.skcc.rental.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.domain.event.UserIdCreated;
import com.skcc.rental.web.rest.dto.BookInfoDTO;
import com.skcc.rental.web.rest.dto.LatefeeDTO;
import com.skcc.rental.web.rest.dto.RentedItemDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * Service Interface for managing {@link com.skcc.rental.domain.Rental}.
 */
public interface RentalService {

    /**
     * Save a rental.
     *
     * @param rentalDTO the entity to save.
     * @return the persisted entity.
     */
    Rental save(Rental rentalDTO);

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Rental> findAll(Pageable pageable);

    /**
     * Get the "id" rental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Rental> findOne(Long id);

    /**
     * Delete the "id" rental.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Rental createRental(UserIdCreated userIdCreated);

    /****
     *
     * Business Logic
     *
     * 책 대여하기
     *
     * ****/
    RentedItem rentBook(Long userId, BookInfoDTO book);

    /****
     *
     * Business Logic
     *
     * 책 반납하기
     *
     * ****/

    Optional<Rental> findRentalByUser(Long userId);

    Rental returnBook(Long userId, Long bookIds);

    void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException;

    void savePoints(Long userId) throws ExecutionException, InterruptedException, JsonProcessingException;

    Rental returnOverdueBooks(Long userid, Long book);

    Rental releaseOverdue(Long userId);

    LatefeeDTO getLatefee(Long userId);

    void updateBookCatalog(Long bookId, String eventType) throws InterruptedException, ExecutionException, JsonProcessingException;

    Long beOverdueBooks(Long rentalId, Long bookId);



    //ResponseEntity usePoint
}
