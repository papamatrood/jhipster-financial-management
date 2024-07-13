package com.cratechnologie.financialmanagement.domain;

import static com.cratechnologie.financialmanagement.domain.TransactionLogTestSamples.*;
import static com.cratechnologie.financialmanagement.domain.TransactionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.cratechnologie.financialmanagement.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransactionLogTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TransactionLog.class);
        TransactionLog transactionLog1 = getTransactionLogSample1();
        TransactionLog transactionLog2 = new TransactionLog();
        assertThat(transactionLog1).isNotEqualTo(transactionLog2);

        transactionLog2.setId(transactionLog1.getId());
        assertThat(transactionLog1).isEqualTo(transactionLog2);

        transactionLog2 = getTransactionLogSample2();
        assertThat(transactionLog1).isNotEqualTo(transactionLog2);
    }

    @Test
    void transactionTest() {
        TransactionLog transactionLog = getTransactionLogRandomSampleGenerator();
        Transaction transactionBack = getTransactionRandomSampleGenerator();

        transactionLog.setTransaction(transactionBack);
        assertThat(transactionLog.getTransaction()).isEqualTo(transactionBack);

        transactionLog.transaction(null);
        assertThat(transactionLog.getTransaction()).isNull();
    }
}
