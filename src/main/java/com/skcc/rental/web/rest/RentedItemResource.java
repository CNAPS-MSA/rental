package com.skcc.rental.web.rest;

import com.skcc.rental.domain.Rental;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.service.RentedItemService;
import com.skcc.rental.web.rest.errors.BadRequestAlertException;
import com.skcc.rental.web.rest.dto.RentedItemDTO;

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
 * REST controller for managing {@link com.skcc.rental.domain.RentedItem}.
 */
@RestController
@RequestMapping("/api")
public class RentedItemResource {

    private final Logger log = LoggerFactory.getLogger(RentedItemResource.class);

    private static final String ENTITY_NAME = "rentalRentedItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RentedItemService rentedItemService;
    private final RentalService rentalService;
    public RentedItemResource(RentedItemService rentedItemService, RentalService rentalService) {
        this.rentedItemService = rentedItemService;
        this.rentalService = rentalService;
    }

    /**
     * {@code POST  /rented-items} : Create a new rentedItem.
     *
     * @param rentedItemDTO the rentedItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rentedItemDTO, or with status {@code 400 (Bad Request)} if the rentedItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rented-items")
    public ResponseEntity<RentedItemDTO> createRentedItem(@RequestBody RentedItemDTO rentedItemDTO) throws URISyntaxException {
        log.debug("REST request to save RentedItem : {}", rentedItemDTO);
        if (rentedItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new rentedItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RentedItemDTO result = rentedItemService.save(rentedItemDTO);
        return ResponseEntity.created(new URI("/api/rented-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rented-items} : Updates an existing rentedItem.
     *
     * @param rentedItemDTO the rentedItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rentedItemDTO,
     * or with status {@code 400 (Bad Request)} if the rentedItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rentedItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rented-items")
    public ResponseEntity<RentedItemDTO> updateRentedItem(@RequestBody RentedItemDTO rentedItemDTO) throws URISyntaxException {
        log.debug("REST request to update RentedItem : {}", rentedItemDTO);
        if (rentedItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        RentedItemDTO result = rentedItemService.save(rentedItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rentedItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /rented-items} : get all the rentedItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rentedItems in body.
     */
    @GetMapping("/rented-items")
    public ResponseEntity<List<RentedItemDTO>> getAllRentedItems(Pageable pageable) {
        log.debug("REST request to get a page of RentedItems");
        Page<RentedItemDTO> page = rentedItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rented-items/:id} : get the "id" rentedItem.
     *
     * @param id the id of the rentedItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rentedItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rented-items/{id}")
    public ResponseEntity<RentedItemDTO> getRentedItem(@PathVariable Long id) {
        log.debug("REST request to get RentedItem : {}", id);
        Optional<RentedItemDTO> rentedItemDTO = rentedItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rentedItemDTO);
    }

    /**
     * {@code DELETE  /rented-items/:id} : delete the "id" rentedItem.
     *
     * @param id the id of the rentedItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rented-items/{id}")
    public ResponseEntity<Void> deleteRentedItem(@PathVariable Long id) {
        log.debug("REST request to delete RentedItem : {}", id);
        rentedItemService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    @GetMapping("/rented-items/title/{title}")
    public ResponseEntity<List<RentedItemDTO>> loadRentedItemsByTitle(@PathVariable("title")String title, Pageable pageable){
        Page<RentedItemDTO> rentedItemDTOS = rentedItemService.findByTitle(title, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), rentedItemDTOS);
        return ResponseEntity.ok().headers(headers).body(rentedItemDTOS.getContent());
    }
    @GetMapping("/rented-items/rental/{rentalId}")
    public ResponseEntity<List<RentedItemDTO>> loadRentedItemsByRental(@PathVariable("rentalId")Long rentalId, Pageable pageable){
        Page<RentedItemDTO> rentedItemDTOS = rentedItemService.findRentedItemsByRental(rentalId, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), rentedItemDTOS);
        return ResponseEntity.ok().headers(headers).body(rentedItemDTOS.getContent());
    }
}
