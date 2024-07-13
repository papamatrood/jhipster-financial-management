package com.cratechnologie.financialmanagement.repository;

import com.cratechnologie.financialmanagement.domain.TransactionLog;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TransactionLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {}
