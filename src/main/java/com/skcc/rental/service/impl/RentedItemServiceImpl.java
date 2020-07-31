package com.skcc.rental.service.impl;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.service.RentedItemService;
import com.skcc.rental.domain.RentedItem;
import com.skcc.rental.repository.RentedItemRepository;
import com.skcc.rental.web.rest.dto.RentedItemDTO;
import com.skcc.rental.web.rest.mapper.RentedItemMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link RentedItem}.
 */
@Service
@Transactional
public class RentedItemServiceImpl implements RentedItemService {

    private final Logger log = LoggerFactory.getLogger(RentedItemServiceImpl.class);
    private final RentalService rentalService;
    private final RentedItemRepository rentedItemRepository;

    private final RentedItemMapper rentedItemMapper;

    public RentedItemServiceImpl(RentalService rentalService, RentedItemRepository rentedItemRepository, RentedItemMapper rentedItemMapper) {
        this.rentalService = rentalService;
        this.rentedItemRepository = rentedItemRepository;
        this.rentedItemMapper = rentedItemMapper;
    }

    /**
     * Save a rentedItem.
     *
     * @param rentedItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public RentedItemDTO save(RentedItemDTO rentedItemDTO) {
        log.debug("Request to save RentedItem : {}", rentedItemDTO);
        RentedItem rentedItem = rentedItemMapper.toEntity(rentedItemDTO);
        rentedItem = rentedItemRepository.save(rentedItem);
        return rentedItemMapper.toDto(rentedItem);
    }

    /**
     * Get all the rentedItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<RentedItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all RentedItems");
        return rentedItemRepository.findAll(pageable)
            .map(rentedItemMapper::toDto);
    }

    /**
     * Get one rentedItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<RentedItemDTO> findOne(Long id) {
        log.debug("Request to get RentedItem : {}", id);
        return rentedItemRepository.findById(id)
            .map(rentedItemMapper::toDto);
    }

    /**
     * Delete the rentedItem by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete RentedItem : {}", id);
        rentedItemRepository.deleteById(id);
    }

    @Override
    public List<RentedItem> findAllForManage() {
        return rentedItemRepository.findAll();
    }

    @Override
    public Page<RentedItemDTO> findByTitle(String title, Pageable pageable) {
        return rentedItemRepository.findByBookTitleContaining(title, pageable)
            .map(rentedItemMapper::toDto);
    }

    @Override
    public Page<RentedItemDTO> findRentedItemsByRental(Long rentalId, Pageable pageable) {
        Rental rental = rentalService.findOne(rentalId).get();
        return rentedItemRepository.findByRental(rental, pageable).map(rentedItemMapper::toDto);
    }


}
