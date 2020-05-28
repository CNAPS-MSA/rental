package com.skcc.rental.web.rest;

import com.skcc.rental.service.ReturnedItemService;
import com.skcc.rental.web.rest.errors.BadRequestAlertException;
import com.skcc.rental.web.rest.dto.ReturnedItemDTO;

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
 * REST controller for managing {@link com.skcc.rental.domain.ReturnedItem}.
 */
@RestController
@RequestMapping("/api")
public class ReturnedItemResource {

    private final Logger log = LoggerFactory.getLogger(ReturnedItemResource.class);

    private static final String ENTITY_NAME = "rentalReturnedItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReturnedItemService returnedItemService;

    public ReturnedItemResource(ReturnedItemService returnedItemService) {
        this.returnedItemService = returnedItemService;
    }

    /**
     * {@code POST  /returned-items} : Create a new returnedItem.
     *
     * @param returnedItemDTO the returnedItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new returnedItemDTO, or with status {@code 400 (Bad Request)} if the returnedItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/returned-items")
    public ResponseEntity<ReturnedItemDTO> createReturnedItem(@RequestBody ReturnedItemDTO returnedItemDTO) throws URISyntaxException {
        log.debug("REST request to save ReturnedItem : {}", returnedItemDTO);
        if (returnedItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new returnedItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReturnedItemDTO result = returnedItemService.save(returnedItemDTO);
        return ResponseEntity.created(new URI("/api/returned-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /returned-items} : Updates an existing returnedItem.
     *
     * @param returnedItemDTO the returnedItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated returnedItemDTO,
     * or with status {@code 400 (Bad Request)} if the returnedItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the returnedItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/returned-items")
    public ResponseEntity<ReturnedItemDTO> updateReturnedItem(@RequestBody ReturnedItemDTO returnedItemDTO) throws URISyntaxException {
        log.debug("REST request to update ReturnedItem : {}", returnedItemDTO);
        if (returnedItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ReturnedItemDTO result = returnedItemService.save(returnedItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, returnedItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /returned-items} : get all the returnedItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of returnedItems in body.
     */
    @GetMapping("/returned-items")
    public ResponseEntity<List<ReturnedItemDTO>> getAllReturnedItems(Pageable pageable) {
        log.debug("REST request to get a page of ReturnedItems");
        Page<ReturnedItemDTO> page = returnedItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /returned-items/:id} : get the "id" returnedItem.
     *
     * @param id the id of the returnedItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the returnedItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/returned-items/{id}")
    public ResponseEntity<ReturnedItemDTO> getReturnedItem(@PathVariable Long id) {
        log.debug("REST request to get ReturnedItem : {}", id);
        Optional<ReturnedItemDTO> returnedItemDTO = returnedItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(returnedItemDTO);
    }

    /**
     * {@code DELETE  /returned-items/:id} : delete the "id" returnedItem.
     *
     * @param id the id of the returnedItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/returned-items/{id}")
    public ResponseEntity<Void> deleteReturnedItem(@PathVariable Long id) {
        log.debug("REST request to delete ReturnedItem : {}", id);
        returnedItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
