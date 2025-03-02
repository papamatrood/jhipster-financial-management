package com.crat.budget.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class SubTitleAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSubTitleAllPropertiesEquals(SubTitle expected, SubTitle actual) {
        assertSubTitleAutoGeneratedPropertiesEquals(expected, actual);
        assertSubTitleAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSubTitleAllUpdatablePropertiesEquals(SubTitle expected, SubTitle actual) {
        assertSubTitleUpdatableFieldsEquals(expected, actual);
        assertSubTitleUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSubTitleAutoGeneratedPropertiesEquals(SubTitle expected, SubTitle actual) {
        assertThat(actual)
            .as("Verify SubTitle auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSubTitleUpdatableFieldsEquals(SubTitle expected, SubTitle actual) {
        assertThat(actual)
            .as("Verify SubTitle relevant properties")
            .satisfies(a -> assertThat(a.getCode()).as("check code").isEqualTo(expected.getCode()))
            .satisfies(a -> assertThat(a.getDesignation()).as("check designation").isEqualTo(expected.getDesignation()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertSubTitleUpdatableRelationshipsEquals(SubTitle expected, SubTitle actual) {
        // empty method
    }
}
