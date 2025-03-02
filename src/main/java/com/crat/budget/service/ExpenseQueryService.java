package com.crat.budget.service;

import com.crat.budget.domain.*; // for static metamodels
import com.crat.budget.domain.Expense;
import com.crat.budget.repository.ExpenseRepository;
import com.crat.budget.service.criteria.ExpenseCriteria;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Expense} entities in the database.
 * The main input is a {@link ExpenseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link Expense} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExpenseQueryService extends QueryService<Expense> {

    private static final Logger LOG = LoggerFactory.getLogger(ExpenseQueryService.class);

    private final ExpenseRepository expenseRepository;

    public ExpenseQueryService(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    /**
     * Return a {@link Page} of {@link Expense} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Expense> findByCriteria(ExpenseCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Expense> specification = createSpecification(criteria);
        return expenseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExpenseCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Expense> specification = createSpecification(criteria);
        return expenseRepository.count(specification);
    }

    /**
     * Function to convert {@link ExpenseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Expense> createSpecification(ExpenseCriteria criteria) {
        Specification<Expense> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Expense_.id));
            }
            if (criteria.getAchievementsInThePastYear() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getAchievementsInThePastYear(), Expense_.achievementsInThePastYear)
                );
            }
            if (criteria.getNewYearForecast() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNewYearForecast(), Expense_.newYearForecast));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildSpecification(criteria.getCategory(), Expense_.category));
            }
            if (criteria.getFinancialYearId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getFinancialYearId(), root ->
                        root.join(Expense_.financialYear, JoinType.LEFT).get(FinancialYear_.id)
                    )
                );
            }
            if (criteria.getAnnexDecisionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getAnnexDecisionId(), root ->
                        root.join(Expense_.annexDecision, JoinType.LEFT).get(AnnexDecision_.id)
                    )
                );
            }
            if (criteria.getArticleId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getArticleId(), root -> root.join(Expense_.articles, JoinType.LEFT).get(Article_.id))
                );
            }
        }
        return specification;
    }
}
