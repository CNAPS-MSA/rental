package com.skcc.rental.web.rest;

import com.skcc.rental.RentalApp;
import com.skcc.rental.domain.OverdueItem;
import com.skcc.rental.repository.OverdueItemRepository;
import com.skcc.rental.service.OverdueItemService;
import com.skcc.rental.service.dto.OverdueItemDTO;
import com.skcc.rental.service.mapper.OverdueItemMapper;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link OverdueItemResource} REST controller.
 */
@SpringBootTest(classes = RentalApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class OverdueItemResourceIT {

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    private static final LocalDate DEFAULT_DUE_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DUE_DATE = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private OverdueItemRepository overdueItemRepository;

    @Autowired
    private OverdueItemMapper overdueItemMapper;

    @Autowired
    private OverdueItemService overdueItemService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOverdueItemMockMvc;

    private OverdueItem overdueItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OverdueItem createEntity(EntityManager em) {
        OverdueItem overdueItem = new OverdueItem()
            .bookId(DEFAULT_BOOK_ID)
            .dueDate(DEFAULT_DUE_DATE);
        return overdueItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OverdueItem createUpdatedEntity(EntityManager em) {
        OverdueItem overdueItem = new OverdueItem()
            .bookId(UPDATED_BOOK_ID)
            .dueDate(UPDATED_DUE_DATE);
        return overdueItem;
    }

    @BeforeEach
    public void initTest() {
        overdueItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createOverdueItem() throws Exception {
        int databaseSizeBeforeCreate = overdueItemRepository.findAll().size();

        // Create the OverdueItem
        OverdueItemDTO overdueItemDTO = overdueItemMapper.toDto(overdueItem);
        restOverdueItemMockMvc.perform(post("/api/overdue-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(overdueItemDTO)))
            .andExpect(status().isCreated());

        // Validate the OverdueItem in the database
        List<OverdueItem> overdueItemList = overdueItemRepository.findAll();
        assertThat(overdueItemList).hasSize(databaseSizeBeforeCreate + 1);
        OverdueItem testOverdueItem = overdueItemList.get(overdueItemList.size() - 1);
        assertThat(testOverdueItem.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testOverdueItem.getDueDate()).isEqualTo(DEFAULT_DUE_DATE);
    }

    @Test
    @Transactional
    public void createOverdueItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = overdueItemRepository.findAll().size();

        // Create the OverdueItem with an existing ID
        overdueItem.setId(1L);
        OverdueItemDTO overdueItemDTO = overdueItemMapper.toDto(overdueItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restOverdueItemMockMvc.perform(post("/api/overdue-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(overdueItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OverdueItem in the database
        List<OverdueItem> overdueItemList = overdueItemRepository.findAll();
        assertThat(overdueItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllOverdueItems() throws Exception {
        // Initialize the database
        overdueItemRepository.saveAndFlush(overdueItem);

        // Get all the overdueItemList
        restOverdueItemMockMvc.perform(get("/api/overdue-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(overdueItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())));
    }
    
    @Test
    @Transactional
    public void getOverdueItem() throws Exception {
        // Initialize the database
        overdueItemRepository.saveAndFlush(overdueItem);

        // Get the overdueItem
        restOverdueItemMockMvc.perform(get("/api/overdue-items/{id}", overdueItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(overdueItem.getId().intValue()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingOverdueItem() throws Exception {
        // Get the overdueItem
        restOverdueItemMockMvc.perform(get("/api/overdue-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateOverdueItem() throws Exception {
        // Initialize the database
        overdueItemRepository.saveAndFlush(overdueItem);

        int databaseSizeBeforeUpdate = overdueItemRepository.findAll().size();

        // Update the overdueItem
        OverdueItem updatedOverdueItem = overdueItemRepository.findById(overdueItem.getId()).get();
        // Disconnect from session so that the updates on updatedOverdueItem are not directly saved in db
        em.detach(updatedOverdueItem);
        updatedOverdueItem
            .bookId(UPDATED_BOOK_ID)
            .dueDate(UPDATED_DUE_DATE);
        OverdueItemDTO overdueItemDTO = overdueItemMapper.toDto(updatedOverdueItem);

        restOverdueItemMockMvc.perform(put("/api/overdue-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(overdueItemDTO)))
            .andExpect(status().isOk());

        // Validate the OverdueItem in the database
        List<OverdueItem> overdueItemList = overdueItemRepository.findAll();
        assertThat(overdueItemList).hasSize(databaseSizeBeforeUpdate);
        OverdueItem testOverdueItem = overdueItemList.get(overdueItemList.size() - 1);
        assertThat(testOverdueItem.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testOverdueItem.getDueDate()).isEqualTo(UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingOverdueItem() throws Exception {
        int databaseSizeBeforeUpdate = overdueItemRepository.findAll().size();

        // Create the OverdueItem
        OverdueItemDTO overdueItemDTO = overdueItemMapper.toDto(overdueItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOverdueItemMockMvc.perform(put("/api/overdue-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(overdueItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OverdueItem in the database
        List<OverdueItem> overdueItemList = overdueItemRepository.findAll();
        assertThat(overdueItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteOverdueItem() throws Exception {
        // Initialize the database
        overdueItemRepository.saveAndFlush(overdueItem);

        int databaseSizeBeforeDelete = overdueItemRepository.findAll().size();

        // Delete the overdueItem
        restOverdueItemMockMvc.perform(delete("/api/overdue-items/{id}", overdueItem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<OverdueItem> overdueItemList = overdueItemRepository.findAll();
        assertThat(overdueItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
