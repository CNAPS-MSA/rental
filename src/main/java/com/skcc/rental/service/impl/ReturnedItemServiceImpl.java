package com.skcc.rental.service.impl;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.service.ReturnedItemService;
import com.skcc.rental.domain.ReturnedItem;
import com.skcc.rental.repository.ReturnedItemRepository;
import com.skcc.rental.web.rest.dto.ReturnedItemDTO;
import com.skcc.rental.web.rest.mapper.ReturnedItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ReturnedItem}.
 */
@Service
@Transactional
public class ReturnedItemServiceImpl implements ReturnedItemService {

    private final Logger log = LoggerFactory.getLogger(ReturnedItemServiceImpl.class);

    private final ReturnedItemRepository returnedItemRepository;

    private final ReturnedItemMapper returnedItemMapper;
    private final RentalService rentalService;
    public ReturnedItemServiceImpl(ReturnedItemRepository returnedItemRepository, ReturnedItemMapper returnedItemMapper, RentalService rentalService) {
        this.returnedItemRepository = returnedItemRepository;
        this.returnedItemMapper = returnedItemMapper;
        this.rentalService = rentalService;
    }

    /**
     * Save a returnedItem.
     *
     * @param returnedItem the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ReturnedItem save(ReturnedItem returnedItem) {
        log.debug("Request to save ReturnedItem : {}", returnedItem);
        return returnedItemRepository.save(returnedItem);

    }

    /**
     * Get all the returnedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ReturnedItem> findAll(Pageable pageable) {
        log.debug("Request to get all ReturnedItems");
        return returnedItemRepository.findAll(pageable);
    }

    /**
     * Get one returnedItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ReturnedItem> findOne(Long id) {
        log.debug("Request to get ReturnedItem : {}", id);
        return returnedItemRepository.findById(id);
    }

    /**
     * Delete the returnedItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete ReturnedItem : {}", id);
        returnedItemRepository.deleteById(id);
    }

    @Override
    public Page<ReturnedItem> findReturnedItemsByRental(Long rentalId, Pageable pageable) {
        Rental rental = rentalService.findOne(rentalId).get();
        return returnedItemRepository.findByRental(rental, pageable);

    }
}
