package com.cratechnologie.financialmanagement.web.rest;

import com.cratechnologie.financialmanagement.domain.TransactionLog;
import com.cratechnologie.financialmanagement.repository.TransactionLogRepository;
import com.cratechnologie.financialmanagement.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.cratechnologie.financialmanagement.domain.TransactionLog}.
 */
@RestController
@RequestMapping("/api/transaction-logs")
@Transactional
public class TransactionLogResource {

    private static final Logger log = LoggerFactory.getLogger(TransactionLogResource.class);

    private static final String ENTITY_NAME = "transactionLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionLogRepository transactionLogRepository;

    public TransactionLogResource(TransactionLogRepository transactionLogRepository) {
        this.transactionLogRepository = transactionLogRepository;
    }

    /**
     * {@code POST  /transaction-logs} : Create a new transactionLog.
     *
     * @param transactionLog the transactionLog to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionLog, or with status {@code 400 (Bad Request)} if the transactionLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TransactionLog> createTransactionLog(@Valid @RequestBody TransactionLog transactionLog)
        throws URISyntaxException {
        log.debug("REST request to save TransactionLog : {}", transactionLog);
        if (transactionLog.getId() != null) {
            throw new BadRequestAlertException("A new transactionLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        transactionLog = transactionLogRepository.save(transactionLog);
        return ResponseEntity.created(new URI("/api/transaction-logs/" + transactionLog.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, transactionLog.getId().toString()))
            .body(transactionLog);
    }

    /**
     * {@code PUT  /transaction-logs/:id} : Updates an existing transactionLog.
     *
     * @param id the id of the transactionLog to save.
     * @param transactionLog the transactionLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionLog,
     * or with status {@code 400 (Bad Request)} if the transactionLog is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TransactionLog> updateTransactionLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionLog transactionLog
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionLog : {}, {}", id, transactionLog);
        if (transactionLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        transactionLog = transactionLogRepository.save(transactionLog);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionLog.getId().toString()))
            .body(transactionLog);
    }

    /**
     * {@code PATCH  /transaction-logs/:id} : Partial updates given fields of an existing transactionLog, field will ignore if it is null
     *
     * @param id the id of the transactionLog to save.
     * @param transactionLog the transactionLog to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionLog,
     * or with status {@code 400 (Bad Request)} if the transactionLog is not valid,
     * or with status {@code 404 (Not Found)} if the transactionLog is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionLog couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionLog> partialUpdateTransactionLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionLog transactionLog
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionLog partially : {}, {}", id, transactionLog);
        if (transactionLog.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionLog.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionLog> result = transactionLogRepository
            .findById(transactionLog.getId())
            .map(existingTransactionLog -> {
                if (transactionLog.getAction() != null) {
                    existingTransactionLog.setAction(transactionLog.getAction());
                }
                if (transactionLog.getActionAt() != null) {
                    existingTransactionLog.setActionAt(transactionLog.getActionAt());
                }

                return existingTransactionLog;
            })
            .map(transactionLogRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionLog.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-logs} : get all the transactionLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TransactionLog>> getAllTransactionLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TransactionLogs");
        Page<TransactionLog> page = transactionLogRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-logs/:id} : get the "id" transactionLog.
     *
     * @param id the id of the transactionLog to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionLog, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionLog> getTransactionLog(@PathVariable("id") Long id) {
        log.debug("REST request to get TransactionLog : {}", id);
        Optional<TransactionLog> transactionLog = transactionLogRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(transactionLog);
    }

    /**
     * {@code DELETE  /transaction-logs/:id} : delete the "id" transactionLog.
     *
     * @param id the id of the transactionLog to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransactionLog(@PathVariable("id") Long id) {
        log.debug("REST request to delete TransactionLog : {}", id);
        transactionLogRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
