package com.skcc.rental.service;

import com.skcc.rental.web.rest.dto.RentedItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.skcc.rental.domain.RentedItem}.
 */
public interface RentedItemService {

    /**
     * Save a rentedItem.
     *
     * @param rentedItemDTO the entity to save.
     * @return the persisted entity.
     */
    RentedItemDTO save(RentedItemDTO rentedItemDTO);

    /**
     * Get all the rentedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<RentedItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" rentedItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<RentedItemDTO> findOne(Long id);

    /**
     * Delete the "id" rentedItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
