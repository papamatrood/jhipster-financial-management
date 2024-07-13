package com.cratechnologie.financialmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionLogTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransactionLog getTransactionLogSample1() {
        return new TransactionLog().id(1L).action("action1");
    }

    public static TransactionLog getTransactionLogSample2() {
        return new TransactionLog().id(2L).action("action2");
    }

    public static TransactionLog getTransactionLogRandomSampleGenerator() {
        return new TransactionLog().id(longCount.incrementAndGet()).action(UUID.randomUUID().toString());
    }
}
