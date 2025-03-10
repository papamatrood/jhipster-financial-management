package com.crat.budget.service;

import com.crat.budget.domain.FinancialYear;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.crat.budget.domain.FinancialYear}.
 */
public interface FinancialYearService {
    /**
     * Save a financialYear.
     *
     * @param financialYear the entity to save.
     * @return the persisted entity.
     */
    FinancialYear save(FinancialYear financialYear);

    /**
     * Updates a financialYear.
     *
     * @param financialYear the entity to update.
     * @return the persisted entity.
     */
    FinancialYear update(FinancialYear financialYear);

    /**
     * Partially updates a financialYear.
     *
     * @param financialYear the entity to update partially.
     * @return the persisted entity.
     */
    Optional<FinancialYear> partialUpdate(FinancialYear financialYear);

    /**
     * Get all the FinancialYear where Recipe is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<FinancialYear> findAllWhereRecipeIsNull();
    /**
     * Get all the FinancialYear where Expense is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<FinancialYear> findAllWhereExpenseIsNull();
    /**
     * Get all the FinancialYear where AnnexDecision is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<FinancialYear> findAllWhereAnnexDecisionIsNull();

    /**
     * Get the "id" financialYear.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<FinancialYear> findOne(Long id);

    /**
     * Delete the "id" financialYear.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
