package com.skcc.rental.service;

import com.skcc.rental.service.dto.RentalDTO;

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
    RentalDTO save(RentalDTO rentalDTO);

    /**
     * Get all the rentals.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentalDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rental.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentalDTO> findOne(Long id);

    /**
     * Delete the "id" rental.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    RentalDTO rentBooks(Long userId, List<Long> bookIds);

}
