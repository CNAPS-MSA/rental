package com.skcc.rental.web.rest;

import com.skcc.rental.service.OverdueItemService;
import com.skcc.rental.web.rest.errors.BadRequestAlertException;
import com.skcc.rental.web.rest.dto.OverdueItemDTO;

import com.skcc.rental.web.rest.mapper.OverdueItemMapper;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.skcc.rental.domain.OverdueItem}.
 */
@RestController
@RequestMapping("/api")
public class OverdueItemResource {

    private final Logger log = LoggerFactory.getLogger(OverdueItemResource.class);

    private static final String ENTITY_NAME = "rentalOverdueItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OverdueItemService overdueItemService;
    private final OverdueItemMapper overdueItemMapper;
    public OverdueItemResource(OverdueItemService overdueItemService, OverdueItemMapper overdueItemMapper) {
        this.overdueItemService = overdueItemService;
        this.overdueItemMapper = overdueItemMapper;
    }

    /**
     * {@code POST  /overdue-items} : Create a new overdueItem.
     *
     * @param overdueItemDTO the overdueItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new overdueItemDTO, or with status {@code 400 (Bad Request)} if the overdueItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/overdue-items")
    public ResponseEntity<OverdueItemDTO> createOverdueItem(@RequestBody OverdueItemDTO overdueItemDTO) throws URISyntaxException {
        log.debug("REST request to save OverdueItem : {}", overdueItemDTO);
        if (overdueItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new overdueItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OverdueItemDTO result = overdueItemMapper.toDto(overdueItemService.save(overdueItemMapper.toEntity(overdueItemDTO)));
        return ResponseEntity.created(new URI("/api/overdue-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /overdue-items} : Updates an existing overdueItem.
     *
     * @param overdueItemDTO the overdueItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated overdueItemDTO,
     * or with status {@code 400 (Bad Request)} if the overdueItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the overdueItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/overdue-items")
    public ResponseEntity<OverdueItemDTO> updateOverdueItem(@RequestBody OverdueItemDTO overdueItemDTO) throws URISyntaxException {
        log.debug("REST request to update OverdueItem : {}", overdueItemDTO);
        if (overdueItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        OverdueItemDTO result = overdueItemMapper.toDto(overdueItemService.save(overdueItemMapper.toEntity(overdueItemDTO)));
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, overdueItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /overdue-items} : get all the overdueItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of overdueItems in body.
     */
    @GetMapping("/overdue-items")
    public ResponseEntity<List<OverdueItemDTO>> getAllOverdueItems(Pageable pageable) {
        log.debug("REST request to get a page of OverdueItems");
        Page<OverdueItemDTO> page = overdueItemService.findAll(pageable).map(overdueItemMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /overdue-items/:id} : get the "id" overdueItem.
     *
     * @param id the id of the overdueItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the overdueItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/overdue-items/{id}")
    public ResponseEntity<OverdueItemDTO> getOverdueItem(@PathVariable Long id) {
        log.debug("REST request to get OverdueItem : {}", id);
        Optional<OverdueItemDTO> overdueItemDTO = overdueItemService.findOne(id).map(overdueItemMapper::toDto);
        return ResponseUtil.wrapOrNotFound(overdueItemDTO);
    }

    /**
     * {@code DELETE  /overdue-items/:id} : delete the "id" overdueItem.
     *
     * @param id the id of the overdueItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/overdue-items/{id}")
    public ResponseEntity<Void> deleteOverdueItem(@PathVariable Long id) {
        log.debug("REST request to delete OverdueItem : {}", id);
        overdueItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
    @GetMapping("/overdue-items/rental/{rentalId}")
    public ResponseEntity<List<OverdueItemDTO>> loadOverdueItemsByRental(@PathVariable("rentalId")Long rentalId, Pageable pageable){
        Page<OverdueItemDTO> overdueItemDTOS = overdueItemService.findOverdueItemsByRental(rentalId, pageable).map(overdueItemMapper::toDto);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), overdueItemDTOS);
        return ResponseEntity.ok().headers(headers).body(overdueItemDTOS.getContent());
    }
}
