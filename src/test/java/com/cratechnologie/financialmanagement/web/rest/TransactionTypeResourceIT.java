package com.cratechnologie.financialmanagement.web.rest;

import static com.cratechnologie.financialmanagement.domain.TransactionTypeAsserts.*;
import static com.cratechnologie.financialmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.financialmanagement.IntegrationTest;
import com.cratechnologie.financialmanagement.domain.TransactionType;
import com.cratechnologie.financialmanagement.repository.TransactionTypeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TransactionTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/transaction-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionTypeRepository transactionTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionTypeMockMvc;

    private TransactionType transactionType;

    private TransactionType insertedTransactionType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType().name(DEFAULT_NAME);
        return transactionType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionType createUpdatedEntity(EntityManager em) {
        TransactionType transactionType = new TransactionType().name(UPDATED_NAME);
        return transactionType;
    }

    @BeforeEach
    public void initTest() {
        transactionType = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTransactionType != null) {
            transactionTypeRepository.delete(insertedTransactionType);
            insertedTransactionType = null;
        }
    }

    @Test
    @Transactional
    void createTransactionType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionType
        var returnedTransactionType = om.readValue(
            restTransactionTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionType.class
        );

        // Validate the TransactionType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTransactionTypeUpdatableFieldsEquals(returnedTransactionType, getPersistedTransactionType(returnedTransactionType));

        insertedTransactionType = returnedTransactionType;
    }

    @Test
    @Transactional
    void createTransactionTypeWithExistingId() throws Exception {
        // Create the TransactionType with an existing ID
        transactionType.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionType.setName(null);

        // Create the TransactionType, which fails.

        restTransactionTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionTypes() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get all the transactionTypeList
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        // Get the transactionType
        restTransactionTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingTransactionType() throws Exception {
        // Get the transactionType
        restTransactionTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType
        TransactionType updatedTransactionType = transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionType are not directly saved in db
        em.detach(updatedTransactionType);
        updatedTransactionType.name(UPDATED_NAME);

        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionTypeToMatchAllProperties(updatedTransactionType);
    }

    @Test
    @Transactional
    void putNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.name(UPDATED_NAME);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionType, transactionType),
            getPersistedTransactionType(transactionType)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionTypeWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionType using partial update
        TransactionType partialUpdatedTransactionType = new TransactionType();
        partialUpdatedTransactionType.setId(transactionType.getId());

        partialUpdatedTransactionType.name(UPDATED_NAME);

        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionType))
            )
            .andExpect(status().isOk());

        // Validate the TransactionType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionTypeUpdatableFieldsEquals(
            partialUpdatedTransactionType,
            getPersistedTransactionType(partialUpdatedTransactionType)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionType))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionType() throws Exception {
        // Initialize the database
        insertedTransactionType = transactionTypeRepository.saveAndFlush(transactionType);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionType
        restTransactionTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionTypeRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected TransactionType getPersistedTransactionType(TransactionType transactionType) {
        return transactionTypeRepository.findById(transactionType.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionTypeToMatchAllProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllPropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }

    protected void assertPersistedTransactionTypeToMatchUpdatableProperties(TransactionType expectedTransactionType) {
        assertTransactionTypeAllUpdatablePropertiesEquals(expectedTransactionType, getPersistedTransactionType(expectedTransactionType));
    }
}
