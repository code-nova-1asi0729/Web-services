package com.codenova.vigilia.incidents.domain.model.valueobjects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RatingScoreTest {

    @Test
    void acceptsLowestAndHighestScore() {
        assertEquals(1, new RatingScore(1).value());
        assertEquals(5, new RatingScore(5).value());
    }

    @Test
    void rejectsScoreOutOfRange() {
        assertThrows(IllegalArgumentException.class, () -> new RatingScore(0));
        assertThrows(IllegalArgumentException.class, () -> new RatingScore(6));
    }
}