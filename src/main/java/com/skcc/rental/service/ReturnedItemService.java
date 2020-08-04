package com.skcc.rental.service;

import com.skcc.rental.web.rest.dto.ReturnedItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.skcc.rental.domain.ReturnedItem}.
 */
public interface ReturnedItemService {

    /**
     * Save a returnedItem.
     *
     * @param returnedItemDTO the entity to save.
     * @return the persisted entity.
     */
    ReturnedItemDTO save(ReturnedItemDTO returnedItemDTO);

    /**
     * Get all the returnedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ReturnedItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" returnedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ReturnedItemDTO> findOne(Long id);

    /**
     * Delete the "id" returnedItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<ReturnedItemDTO> findReturnedItemsByRental(Long rentalId, Pageable pageable);
}
