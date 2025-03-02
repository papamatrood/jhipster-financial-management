package com.crat.budget.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AnnexDecisionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnnexDecisionAllPropertiesEquals(AnnexDecision expected, AnnexDecision actual) {
        assertAnnexDecisionAutoGeneratedPropertiesEquals(expected, actual);
        assertAnnexDecisionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnnexDecisionAllUpdatablePropertiesEquals(AnnexDecision expected, AnnexDecision actual) {
        assertAnnexDecisionUpdatableFieldsEquals(expected, actual);
        assertAnnexDecisionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnnexDecisionAutoGeneratedPropertiesEquals(AnnexDecision expected, AnnexDecision actual) {
        assertThat(actual)
            .as("Verify AnnexDecision auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnnexDecisionUpdatableFieldsEquals(AnnexDecision expected, AnnexDecision actual) {
        assertThat(actual)
            .as("Verify AnnexDecision relevant properties")
            .satisfies(a -> assertThat(a.getDesignation()).as("check designation").isEqualTo(expected.getDesignation()))
            .satisfies(a -> assertThat(a.getExpenseAmount()).as("check expenseAmount").isEqualTo(expected.getExpenseAmount()))
            .satisfies(a -> assertThat(a.getCreditsAlreadyOpen()).as("check creditsAlreadyOpen").isEqualTo(expected.getCreditsAlreadyOpen())
            )
            .satisfies(a -> assertThat(a.getCreditsOpen()).as("check creditsOpen").isEqualTo(expected.getCreditsOpen()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAnnexDecisionUpdatableRelationshipsEquals(AnnexDecision expected, AnnexDecision actual) {
        assertThat(actual)
            .as("Verify AnnexDecision relationships")
            .satisfies(a -> assertThat(a.getFinancialYear()).as("check financialYear").isEqualTo(expected.getFinancialYear()));
    }
}
