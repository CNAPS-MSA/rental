package com.skcc.rental.service;

import com.skcc.rental.domain.OverdueItem;
import com.skcc.rental.web.rest.dto.OverdueItemDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link com.skcc.rental.domain.OverdueItem}.
 */
public interface OverdueItemService {

    /**
     * Save a overdueItem.
     *
     * @param overdueItem the entity to save.
     * @return the persisted entity.
     */
    OverdueItem save(OverdueItem overdueItem);

    /**
     * Get all the overdueItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OverdueItem> findAll(Pageable pageable);

    /**
     * Get the "id" overdueItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OverdueItem> findOne(Long id);

    /**
     * Delete the "id" overdueItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    Page<OverdueItem> findOverdueItemsByRental(Long rentalId, Pageable pageable);
}
