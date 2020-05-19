package com.skcc.rental.service;

import com.skcc.rental.service.dto.OverdueItemDTO;

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
     * @param overdueItemDTO the entity to save.
     * @return the persisted entity.
     */
    OverdueItemDTO save(OverdueItemDTO overdueItemDTO);

    /**
     * Get all the overdueItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<OverdueItemDTO> findAll(Pageable pageable);

    /**
     * Get the "id" overdueItem.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<OverdueItemDTO> findOne(Long id);

    /**
     * Delete the "id" overdueItem.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
