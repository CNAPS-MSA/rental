package com.skcc.rental.web.rest;

import com.skcc.rental.RentalApp;
import com.skcc.rental.adaptor.RentalProducer;
import com.skcc.rental.domain.Rental;
import com.skcc.rental.domain.enumeration.RentalStatus;
import com.skcc.rental.repository.RentalRepository;
import com.skcc.rental.service.RentalService;
import com.skcc.rental.web.rest.dto.RentalDTO;
import com.skcc.rental.web.rest.mapper.RentalMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@link RentalResource} REST controller.
 */
@SpringBootTest(classes = RentalApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class RentalResourceIT {

    private static final Long DEFAULT_USER_ID = 1L;
    private static final Long UPDATED_USER_ID = 2L;

    private static final RentalStatus DEFAULT_RENTAL_STATUS = RentalStatus.RENT_AVAILABLE;
    private static final RentalStatus UPDATED_RENTAL_STATUS = RentalStatus.RENT_UNAVAILABLE;

    private static final int DEFAULT_LATE_FEE = 1;
    private static final int UPDATED_LATE_FEE = 2;

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private RentalService rentalService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalMockMvc;

    @Autowired
    private RentalProducer rentalProducer;

    private Rental rental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createEntity(EntityManager em) {
        Rental rental = new Rental()
            .userId(DEFAULT_USER_ID)
            .rentalStatus(DEFAULT_RENTAL_STATUS)
            .lateFee(DEFAULT_LATE_FEE);
        return rental;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createUpdatedEntity(EntityManager em) {
        Rental rental = new Rental()
            .userId(UPDATED_USER_ID)
            .rentalStatus(UPDATED_RENTAL_STATUS)
            .lateFee(UPDATED_LATE_FEE);
        return rental;
    }

    @BeforeEach
    public void initTest() {
        rental = createEntity(em);
    }

    @Test
    @Transactional
    public void createRental() throws Exception {
        int databaseSizeBeforeCreate = rentalRepository.findAll().size();

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);
        restRentalMockMvc.perform(post("/api/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isCreated());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate + 1);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getUserId()).isEqualTo(DEFAULT_USER_ID);
        assertThat(testRental.getRentalStatus()).isEqualTo(DEFAULT_RENTAL_STATUS);
        assertThat(testRental.getLateFee()).isEqualTo(DEFAULT_LATE_FEE);
    }

    @Test
    @Transactional
    public void createRentalWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = rentalRepository.findAll().size();

        // Create the Rental with an existing ID
        rental.setId(1L);
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalMockMvc.perform(post("/api/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllRentals() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList
        restRentalMockMvc.perform(get("/api/rentals?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].rentalStatus").value(hasItem(DEFAULT_RENTAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].lateFee").value(hasItem(DEFAULT_LATE_FEE)));
    }

    @Test
    @Transactional
    public void getRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get the rental
        restRentalMockMvc.perform(get("/api/rentals/{id}", rental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rental.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID.intValue()))
            .andExpect(jsonPath("$.rentalStatus").value(DEFAULT_RENTAL_STATUS.toString()))
            .andExpect(jsonPath("$.lateFee").value(DEFAULT_LATE_FEE));
    }

    @Test
    @Transactional
    public void getNonExistingRental() throws Exception {
        // Get the rental
        restRentalMockMvc.perform(get("/api/rentals/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental
        Rental updatedRental = rentalRepository.findById(rental.getId()).get();
        // Disconnect from session so that the updates on updatedRental are not directly saved in db
        em.detach(updatedRental);
        updatedRental
            .userId(UPDATED_USER_ID)
            .rentalStatus(UPDATED_RENTAL_STATUS)
            .lateFee(UPDATED_LATE_FEE);
        RentalDTO rentalDTO = rentalMapper.toDto(updatedRental);

        restRentalMockMvc.perform(put("/api/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getUserId()).isEqualTo(UPDATED_USER_ID);
        assertThat(testRental.getRentalStatus()).isEqualTo(UPDATED_RENTAL_STATUS);
        assertThat(testRental.getLateFee()).isEqualTo(UPDATED_LATE_FEE);
    }

    @Test
    @Transactional
    public void updateNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc.perform(put("/api/rentals")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(rentalDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeDelete = rentalRepository.findAll().size();

        // Delete the rental
        restRentalMockMvc.perform(delete("/api/rentals/{id}", rental.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
