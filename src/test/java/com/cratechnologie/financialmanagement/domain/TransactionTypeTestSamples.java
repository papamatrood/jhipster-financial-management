package com.cratechnologie.financialmanagement.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class TransactionTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static TransactionType getTransactionTypeSample1() {
        return new TransactionType().id(1L).name("name1");
    }

    public static TransactionType getTransactionTypeSample2() {
        return new TransactionType().id(2L).name("name2");
    }

    public static TransactionType getTransactionTypeRandomSampleGenerator() {
        return new TransactionType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
