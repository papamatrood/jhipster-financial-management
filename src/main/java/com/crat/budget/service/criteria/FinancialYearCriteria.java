package com.crat.budget.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crat.budget.domain.FinancialYear} entity. This class is used
 * in {@link com.crat.budget.web.rest.FinancialYearResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /financial-years?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FinancialYearCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private IntegerFilter year;

    private LongFilter recipeId;

    private LongFilter expenseId;

    private LongFilter annexDecisionId;

    private Boolean distinct;

    public FinancialYearCriteria() {}

    public FinancialYearCriteria(FinancialYearCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.year = other.optionalYear().map(IntegerFilter::copy).orElse(null);
        this.recipeId = other.optionalRecipeId().map(LongFilter::copy).orElse(null);
        this.expenseId = other.optionalExpenseId().map(LongFilter::copy).orElse(null);
        this.annexDecisionId = other.optionalAnnexDecisionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public FinancialYearCriteria copy() {
        return new FinancialYearCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public IntegerFilter getYear() {
        return year;
    }

    public Optional<IntegerFilter> optionalYear() {
        return Optional.ofNullable(year);
    }

    public IntegerFilter year() {
        if (year == null) {
            setYear(new IntegerFilter());
        }
        return year;
    }

    public void setYear(IntegerFilter year) {
        this.year = year;
    }

    public LongFilter getRecipeId() {
        return recipeId;
    }

    public Optional<LongFilter> optionalRecipeId() {
        return Optional.ofNullable(recipeId);
    }

    public LongFilter recipeId() {
        if (recipeId == null) {
            setRecipeId(new LongFilter());
        }
        return recipeId;
    }

    public void setRecipeId(LongFilter recipeId) {
        this.recipeId = recipeId;
    }

    public LongFilter getExpenseId() {
        return expenseId;
    }

    public Optional<LongFilter> optionalExpenseId() {
        return Optional.ofNullable(expenseId);
    }

    public LongFilter expenseId() {
        if (expenseId == null) {
            setExpenseId(new LongFilter());
        }
        return expenseId;
    }

    public void setExpenseId(LongFilter expenseId) {
        this.expenseId = expenseId;
    }

    public LongFilter getAnnexDecisionId() {
        return annexDecisionId;
    }

    public Optional<LongFilter> optionalAnnexDecisionId() {
        return Optional.ofNullable(annexDecisionId);
    }

    public LongFilter annexDecisionId() {
        if (annexDecisionId == null) {
            setAnnexDecisionId(new LongFilter());
        }
        return annexDecisionId;
    }

    public void setAnnexDecisionId(LongFilter annexDecisionId) {
        this.annexDecisionId = annexDecisionId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FinancialYearCriteria that = (FinancialYearCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(year, that.year) &&
            Objects.equals(recipeId, that.recipeId) &&
            Objects.equals(expenseId, that.expenseId) &&
            Objects.equals(annexDecisionId, that.annexDecisionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, recipeId, expenseId, annexDecisionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FinancialYearCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalYear().map(f -> "year=" + f + ", ").orElse("") +
            optionalRecipeId().map(f -> "recipeId=" + f + ", ").orElse("") +
            optionalExpenseId().map(f -> "expenseId=" + f + ", ").orElse("") +
            optionalAnnexDecisionId().map(f -> "annexDecisionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
