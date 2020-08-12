package com.skcc.rental.service.impl;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.service.OverdueItemService;
import com.skcc.rental.domain.OverdueItem;
import com.skcc.rental.repository.OverdueItemRepository;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.web.rest.dto.OverdueItemDTO;
import com.skcc.rental.web.rest.mapper.OverdueItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link OverdueItem}.
 */
@Service
@Transactional
public class OverdueItemServiceImpl implements OverdueItemService {

    private final Logger log = LoggerFactory.getLogger(OverdueItemServiceImpl.class);

    private final OverdueItemRepository overdueItemRepository;

    private final OverdueItemMapper overdueItemMapper;
    private final RentalService rentalService;
    public OverdueItemServiceImpl(OverdueItemRepository overdueItemRepository, OverdueItemMapper overdueItemMapper, RentalService rentalService) {
        this.overdueItemRepository = overdueItemRepository;
        this.overdueItemMapper = overdueItemMapper;
        this.rentalService = rentalService;
    }

    /**
     * Save a overdueItem.
     *
     * @param overdueItem the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OverdueItem save(OverdueItem overdueItem) {
        log.debug("Request to save OverdueItem : {}", overdueItem);
        return overdueItemRepository.save(overdueItem);

    }

    /**
     * Get all the overdueItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OverdueItem> findAll(Pageable pageable) {
        log.debug("Request to get all OverdueItems");
        return overdueItemRepository.findAll(pageable);
    }

    /**
     * Get one overdueItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OverdueItem> findOne(Long id) {
        log.debug("Request to get OverdueItem : {}", id);
        return overdueItemRepository.findById(id);
    }

    /**
     * Delete the overdueItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OverdueItem : {}", id);
        overdueItemRepository.deleteById(id);
    }

    @Override
    public Page<OverdueItem> findOverdueItemsByRental(Long rentalId, Pageable pageable) {
        Rental rental = rentalService.findOne(rentalId).get();
        return overdueItemRepository.findByRental(rental, pageable);
    }
}
