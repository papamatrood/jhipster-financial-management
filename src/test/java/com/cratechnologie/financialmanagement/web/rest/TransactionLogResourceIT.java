package com.cratechnologie.financialmanagement.web.rest;

import static com.cratechnologie.financialmanagement.domain.TransactionLogAsserts.*;
import static com.cratechnologie.financialmanagement.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.cratechnologie.financialmanagement.IntegrationTest;
import com.cratechnologie.financialmanagement.domain.TransactionLog;
import com.cratechnologie.financialmanagement.repository.TransactionLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link TransactionLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransactionLogResourceIT {

    private static final String DEFAULT_ACTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTION_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transaction-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransactionLogRepository transactionLogRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransactionLogMockMvc;

    private TransactionLog transactionLog;

    private TransactionLog insertedTransactionLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionLog createEntity(EntityManager em) {
        TransactionLog transactionLog = new TransactionLog().action(DEFAULT_ACTION).actionAt(DEFAULT_ACTION_AT);
        return transactionLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransactionLog createUpdatedEntity(EntityManager em) {
        TransactionLog transactionLog = new TransactionLog().action(UPDATED_ACTION).actionAt(UPDATED_ACTION_AT);
        return transactionLog;
    }

    @BeforeEach
    public void initTest() {
        transactionLog = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedTransactionLog != null) {
            transactionLogRepository.delete(insertedTransactionLog);
            insertedTransactionLog = null;
        }
    }

    @Test
    @Transactional
    void createTransactionLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransactionLog
        var returnedTransactionLog = om.readValue(
            restTransactionLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionLog)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransactionLog.class
        );

        // Validate the TransactionLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertTransactionLogUpdatableFieldsEquals(returnedTransactionLog, getPersistedTransactionLog(returnedTransactionLog));

        insertedTransactionLog = returnedTransactionLog;
    }

    @Test
    @Transactional
    void createTransactionLogWithExistingId() throws Exception {
        // Create the TransactionLog with an existing ID
        transactionLog.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransactionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionLog)))
            .andExpect(status().isBadRequest());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionLog.setAction(null);

        // Create the TransactionLog, which fails.

        restTransactionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transactionLog.setActionAt(null);

        // Create the TransactionLog, which fails.

        restTransactionLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionLog)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransactionLogs() throws Exception {
        // Initialize the database
        insertedTransactionLog = transactionLogRepository.saveAndFlush(transactionLog);

        // Get all the transactionLogList
        restTransactionLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transactionLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION)))
            .andExpect(jsonPath("$.[*].actionAt").value(hasItem(DEFAULT_ACTION_AT.toString())));
    }

    @Test
    @Transactional
    void getTransactionLog() throws Exception {
        // Initialize the database
        insertedTransactionLog = transactionLogRepository.saveAndFlush(transactionLog);

        // Get the transactionLog
        restTransactionLogMockMvc
            .perform(get(ENTITY_API_URL_ID, transactionLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transactionLog.getId().intValue()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION))
            .andExpect(jsonPath("$.actionAt").value(DEFAULT_ACTION_AT.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTransactionLog() throws Exception {
        // Get the transactionLog
        restTransactionLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransactionLog() throws Exception {
        // Initialize the database
        insertedTransactionLog = transactionLogRepository.saveAndFlush(transactionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionLog
        TransactionLog updatedTransactionLog = transactionLogRepository.findById(transactionLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransactionLog are not directly saved in db
        em.detach(updatedTransactionLog);
        updatedTransactionLog.action(UPDATED_ACTION).actionAt(UPDATED_ACTION_AT);

        restTransactionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTransactionLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedTransactionLog))
            )
            .andExpect(status().isOk());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransactionLogToMatchAllProperties(updatedTransactionLog);
    }

    @Test
    @Transactional
    void putNonExistingTransactionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transactionLog.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransactionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transactionLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransactionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transactionLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransactionLogWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionLog = transactionLogRepository.saveAndFlush(transactionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionLog using partial update
        TransactionLog partialUpdatedTransactionLog = new TransactionLog();
        partialUpdatedTransactionLog.setId(transactionLog.getId());

        partialUpdatedTransactionLog.action(UPDATED_ACTION);

        restTransactionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionLog))
            )
            .andExpect(status().isOk());

        // Validate the TransactionLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransactionLog, transactionLog),
            getPersistedTransactionLog(transactionLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransactionLogWithPatch() throws Exception {
        // Initialize the database
        insertedTransactionLog = transactionLogRepository.saveAndFlush(transactionLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transactionLog using partial update
        TransactionLog partialUpdatedTransactionLog = new TransactionLog();
        partialUpdatedTransactionLog.setId(transactionLog.getId());

        partialUpdatedTransactionLog.action(UPDATED_ACTION).actionAt(UPDATED_ACTION_AT);

        restTransactionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransactionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransactionLog))
            )
            .andExpect(status().isOk());

        // Validate the TransactionLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransactionLogUpdatableFieldsEquals(partialUpdatedTransactionLog, getPersistedTransactionLog(partialUpdatedTransactionLog));
    }

    @Test
    @Transactional
    void patchNonExistingTransactionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionLog.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransactionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transactionLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransactionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transactionLog))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransactionLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transactionLog.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransactionLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transactionLog)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransactionLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransactionLog() throws Exception {
        // Initialize the database
        insertedTransactionLog = transactionLogRepository.saveAndFlush(transactionLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transactionLog
        restTransactionLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, transactionLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transactionLogRepository.count();
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

    protected TransactionLog getPersistedTransactionLog(TransactionLog transactionLog) {
        return transactionLogRepository.findById(transactionLog.getId()).orElseThrow();
    }

    protected void assertPersistedTransactionLogToMatchAllProperties(TransactionLog expectedTransactionLog) {
        assertTransactionLogAllPropertiesEquals(expectedTransactionLog, getPersistedTransactionLog(expectedTransactionLog));
    }

    protected void assertPersistedTransactionLogToMatchUpdatableProperties(TransactionLog expectedTransactionLog) {
        assertTransactionLogAllUpdatablePropertiesEquals(expectedTransactionLog, getPersistedTransactionLog(expectedTransactionLog));
    }
}
