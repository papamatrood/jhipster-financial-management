package com.crat.budget.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ChapterAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChapterAllPropertiesEquals(Chapter expected, Chapter actual) {
        assertChapterAutoGeneratedPropertiesEquals(expected, actual);
        assertChapterAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChapterAllUpdatablePropertiesEquals(Chapter expected, Chapter actual) {
        assertChapterUpdatableFieldsEquals(expected, actual);
        assertChapterUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChapterAutoGeneratedPropertiesEquals(Chapter expected, Chapter actual) {
        assertThat(actual)
            .as("Verify Chapter auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChapterUpdatableFieldsEquals(Chapter expected, Chapter actual) {
        assertThat(actual)
            .as("Verify Chapter relevant properties")
            .satisfies(a -> assertThat(a.getCode()).as("check code").isEqualTo(expected.getCode()))
            .satisfies(a -> assertThat(a.getDesignation()).as("check designation").isEqualTo(expected.getDesignation()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertChapterUpdatableRelationshipsEquals(Chapter expected, Chapter actual) {
        assertThat(actual)
            .as("Verify Chapter relationships")
            .satisfies(a -> assertThat(a.getSubTitle()).as("check subTitle").isEqualTo(expected.getSubTitle()));
    }
}
