package com.cratechnologie.financialmanagement.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class TransactionTypeAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionTypeAllPropertiesEquals(TransactionType expected, TransactionType actual) {
        assertTransactionTypeAutoGeneratedPropertiesEquals(expected, actual);
        assertTransactionTypeAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionTypeAllUpdatablePropertiesEquals(TransactionType expected, TransactionType actual) {
        assertTransactionTypeUpdatableFieldsEquals(expected, actual);
        assertTransactionTypeUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionTypeAutoGeneratedPropertiesEquals(TransactionType expected, TransactionType actual) {
        assertThat(expected)
            .as("Verify TransactionType auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionTypeUpdatableFieldsEquals(TransactionType expected, TransactionType actual) {
        assertThat(expected)
            .as("Verify TransactionType relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertTransactionTypeUpdatableRelationshipsEquals(TransactionType expected, TransactionType actual) {}
}