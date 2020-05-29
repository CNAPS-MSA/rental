package com.skcc.rental.service;

import com.skcc.rental.domain.Rental;

import com.skcc.rental.web.rest.dto.BookInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

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
    Rental rentBooks(Long userId, List<BookInfo> books) throws Exception;

    /****
     *
     * Business Logic
     *
     * 책 반납하기
     *
     * ****/

    Rental returnBooks(Long userId, List<Long> bookIds);

    void updateBookStatus(List<Long> bookIds, String bookStatus);
}
