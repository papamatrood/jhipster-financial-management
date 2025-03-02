package com.crat.budget.repository;

import com.crat.budget.domain.Mandate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Mandate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MandateRepository extends JpaRepository<Mandate, Long>, JpaSpecificationExecutor<Mandate> {}
