package com.skcc.rental.service.impl;

import com.skcc.rental.service.OverdueItemService;
import com.skcc.rental.domain.OverdueItem;
import com.skcc.rental.repository.OverdueItemRepository;
import com.skcc.rental.service.dto.OverdueItemDTO;
import com.skcc.rental.service.mapper.OverdueItemMapper;
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

    public OverdueItemServiceImpl(OverdueItemRepository overdueItemRepository, OverdueItemMapper overdueItemMapper) {
        this.overdueItemRepository = overdueItemRepository;
        this.overdueItemMapper = overdueItemMapper;
    }

    /**
     * Save a overdueItem.
     *
     * @param overdueItemDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public OverdueItemDTO save(OverdueItemDTO overdueItemDTO) {
        log.debug("Request to save OverdueItem : {}", overdueItemDTO);
        OverdueItem overdueItem = overdueItemMapper.toEntity(overdueItemDTO);
        overdueItem = overdueItemRepository.save(overdueItem);
        return overdueItemMapper.toDto(overdueItem);
    }

    /**
     * Get all the overdueItems.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OverdueItemDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OverdueItems");
        return overdueItemRepository.findAll(pageable)
            .map(overdueItemMapper::toDto);
    }

    /**
     * Get one overdueItem by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OverdueItemDTO> findOne(Long id) {
        log.debug("Request to get OverdueItem : {}", id);
        return overdueItemRepository.findById(id)
            .map(overdueItemMapper::toDto);
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
}
