package com.skcc.rental.web.rest;

import com.skcc.rental.RentalApp;
import com.skcc.rental.domain.ReturnedItem;
import com.skcc.rental.repository.ReturnedItemRepository;
import com.skcc.rental.service.ReturnedItemService;
import com.skcc.rental.web.rest.dto.ReturnedItemDTO;
import com.skcc.rental.web.rest.mapper.ReturnedItemMapper;

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
 * Integration tests for the {@link ReturnedItemResource} REST controller.
 */
@SpringBootTest(classes = RentalApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class ReturnedItemResourceIT {

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    private static final LocalDate DEFAULT_RETURNED_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_RETURNED_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_BOOK_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_BOOK_TITLE = "BBBBBBBBBB";

    @Autowired
    private ReturnedItemRepository returnedItemRepository;

    @Autowired
    private ReturnedItemMapper returnedItemMapper;

    @Autowired
    private ReturnedItemService returnedItemService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReturnedItemMockMvc;

    private ReturnedItem returnedItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnedItem createEntity(EntityManager em) {
        ReturnedItem returnedItem = new ReturnedItem()
            .bookId(DEFAULT_BOOK_ID)
            .returnedDate(DEFAULT_RETURNED_DATE)
            .bookTitle(DEFAULT_BOOK_TITLE);
        return returnedItem;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReturnedItem createUpdatedEntity(EntityManager em) {
        ReturnedItem returnedItem = new ReturnedItem()
            .bookId(UPDATED_BOOK_ID)
            .returnedDate(UPDATED_RETURNED_DATE)
            .bookTitle(UPDATED_BOOK_TITLE);
        return returnedItem;
    }

    @BeforeEach
    public void initTest() {
        returnedItem = createEntity(em);
    }

    @Test
    @Transactional
    public void createReturnedItem() throws Exception {
        int databaseSizeBeforeCreate = returnedItemRepository.findAll().size();

        // Create the ReturnedItem
        ReturnedItemDTO returnedItemDTO = returnedItemMapper.toDto(returnedItem);
        restReturnedItemMockMvc.perform(post("/api/returned-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(returnedItemDTO)))
            .andExpect(status().isCreated());

        // Validate the ReturnedItem in the database
        List<ReturnedItem> returnedItemList = returnedItemRepository.findAll();
        assertThat(returnedItemList).hasSize(databaseSizeBeforeCreate + 1);
        ReturnedItem testReturnedItem = returnedItemList.get(returnedItemList.size() - 1);
        assertThat(testReturnedItem.getBookId()).isEqualTo(DEFAULT_BOOK_ID);
        assertThat(testReturnedItem.getReturnedDate()).isEqualTo(DEFAULT_RETURNED_DATE);
        assertThat(testReturnedItem.getBookTitle()).isEqualTo(DEFAULT_BOOK_TITLE);
    }

    @Test
    @Transactional
    public void createReturnedItemWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = returnedItemRepository.findAll().size();

        // Create the ReturnedItem with an existing ID
        returnedItem.setId(1L);
        ReturnedItemDTO returnedItemDTO = returnedItemMapper.toDto(returnedItem);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReturnedItemMockMvc.perform(post("/api/returned-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(returnedItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnedItem in the database
        List<ReturnedItem> returnedItemList = returnedItemRepository.findAll();
        assertThat(returnedItemList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReturnedItems() throws Exception {
        // Initialize the database
        returnedItemRepository.saveAndFlush(returnedItem);

        // Get all the returnedItemList
        restReturnedItemMockMvc.perform(get("/api/returned-items?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(returnedItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())))
            .andExpect(jsonPath("$.[*].returnedDate").value(hasItem(DEFAULT_RETURNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].bookTitle").value(hasItem(DEFAULT_BOOK_TITLE)));
    }

    @Test
    @Transactional
    public void getReturnedItem() throws Exception {
        // Initialize the database
        returnedItemRepository.saveAndFlush(returnedItem);

        // Get the returnedItem
        restReturnedItemMockMvc.perform(get("/api/returned-items/{id}", returnedItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(returnedItem.getId().intValue()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()))
            .andExpect(jsonPath("$.returnedDate").value(DEFAULT_RETURNED_DATE.toString()))
            .andExpect(jsonPath("$.bookTitle").value(DEFAULT_BOOK_TITLE));
    }

    @Test
    @Transactional
    public void getNonExistingReturnedItem() throws Exception {
        // Get the returnedItem
        restReturnedItemMockMvc.perform(get("/api/returned-items/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReturnedItem() throws Exception {
        // Initialize the database
        returnedItemRepository.saveAndFlush(returnedItem);

        int databaseSizeBeforeUpdate = returnedItemRepository.findAll().size();

        // Update the returnedItem
        ReturnedItem updatedReturnedItem = returnedItemRepository.findById(returnedItem.getId()).get();
        // Disconnect from session so that the updates on updatedReturnedItem are not directly saved in db
        em.detach(updatedReturnedItem);
        updatedReturnedItem
            .bookId(UPDATED_BOOK_ID)
            .returnedDate(UPDATED_RETURNED_DATE)
            .bookTitle(UPDATED_BOOK_TITLE);
        ReturnedItemDTO returnedItemDTO = returnedItemMapper.toDto(updatedReturnedItem);

        restReturnedItemMockMvc.perform(put("/api/returned-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(returnedItemDTO)))
            .andExpect(status().isOk());

        // Validate the ReturnedItem in the database
        List<ReturnedItem> returnedItemList = returnedItemRepository.findAll();
        assertThat(returnedItemList).hasSize(databaseSizeBeforeUpdate);
        ReturnedItem testReturnedItem = returnedItemList.get(returnedItemList.size() - 1);
        assertThat(testReturnedItem.getBookId()).isEqualTo(UPDATED_BOOK_ID);
        assertThat(testReturnedItem.getReturnedDate()).isEqualTo(UPDATED_RETURNED_DATE);
        assertThat(testReturnedItem.getBookTitle()).isEqualTo(UPDATED_BOOK_TITLE);
    }

    @Test
    @Transactional
    public void updateNonExistingReturnedItem() throws Exception {
        int databaseSizeBeforeUpdate = returnedItemRepository.findAll().size();

        // Create the ReturnedItem
        ReturnedItemDTO returnedItemDTO = returnedItemMapper.toDto(returnedItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReturnedItemMockMvc.perform(put("/api/returned-items")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(returnedItemDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReturnedItem in the database
        List<ReturnedItem> returnedItemList = returnedItemRepository.findAll();
        assertThat(returnedItemList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReturnedItem() throws Exception {
        // Initialize the database
        returnedItemRepository.saveAndFlush(returnedItem);

        int databaseSizeBeforeDelete = returnedItemRepository.findAll().size();

        // Delete the returnedItem
        restReturnedItemMockMvc.perform(delete("/api/returned-items/{id}", returnedItem.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ReturnedItem> returnedItemList = returnedItemRepository.findAll();
        assertThat(returnedItemList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
