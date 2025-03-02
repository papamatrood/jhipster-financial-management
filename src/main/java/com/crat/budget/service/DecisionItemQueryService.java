package com.crat.budget.service;

import com.crat.budget.domain.*; // for static metamodels
import com.crat.budget.domain.DecisionItem;
import com.crat.budget.repository.DecisionItemRepository;
import com.crat.budget.service.criteria.DecisionItemCriteria;
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
 * Service for executing complex queries for {@link DecisionItem} entities in the database.
 * The main input is a {@link DecisionItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DecisionItem} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DecisionItemQueryService extends QueryService<DecisionItem> {

    private static final Logger LOG = LoggerFactory.getLogger(DecisionItemQueryService.class);

    private final DecisionItemRepository decisionItemRepository;

    public DecisionItemQueryService(DecisionItemRepository decisionItemRepository) {
        this.decisionItemRepository = decisionItemRepository;
    }

    /**
     * Return a {@link Page} of {@link DecisionItem} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DecisionItem> findByCriteria(DecisionItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DecisionItem> specification = createSpecification(criteria);
        return decisionItemRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DecisionItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<DecisionItem> specification = createSpecification(criteria);
        return decisionItemRepository.count(specification);
    }

    /**
     * Function to convert {@link DecisionItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DecisionItem> createSpecification(DecisionItemCriteria criteria) {
        Specification<DecisionItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DecisionItem_.id));
            }
            if (criteria.getBeneficiary() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBeneficiary(), DecisionItem_.beneficiary));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), DecisionItem_.amount));
            }
            if (criteria.getObservation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getObservation(), DecisionItem_.observation));
            }
            if (criteria.getDecisionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getDecisionId(), root -> root.join(DecisionItem_.decision, JoinType.LEFT).get(Decision_.id))
                );
            }
        }
        return specification;
    }
}
