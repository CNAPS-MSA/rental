package com.skcc.rental.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.skcc.rental.domain.Rental;

import com.skcc.rental.web.rest.dto.BookInfo;
import com.skcc.rental.web.rest.dto.LatefeeDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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


    /****
     *
     * Business Logic
     *
     * 책 대여하기
     *
     * ****/
    Rental rentBooks(Long userId, List<BookInfo> books);

    /****
     *
     * Business Logic
     *
     * 책 반납하기
     *
     * ****/

    Rental returnBooks(Long userId, List<Long> bookIds);

    void updateBookStatus(Long bookId, String bookStatus) throws ExecutionException, InterruptedException, JsonProcessingException;

    void savePoints(Long userId, int bookCnt) throws ExecutionException, InterruptedException, JsonProcessingException;

    Rental overdueBooks(Long userId, List<Long> books);

    Rental returnOverdueBooks(Long userid, List<Long> books);

    Rental releaseOverdue(Long userId, int latefee);

    LatefeeDTO createLatefee(Long userId);

    void updateBookCatalog(String title, String rent_book) throws InterruptedException, ExecutionException, JsonProcessingException;

    BookInfo getBookInfoForReturn(Long b);
}
