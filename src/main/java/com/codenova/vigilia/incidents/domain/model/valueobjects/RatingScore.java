package com.codenova.vigilia.incidents.domain.model.valueobjects;
/**
 * Score from 1 to 5 that a resident gives to a resolved incident
 */
public record RatingScore(Integer value) {
    public RatingScore {
        if (value == null || value < 1 || value > 5) throw new IllegalArgumentException("incident.error.rating.range");
    }
}