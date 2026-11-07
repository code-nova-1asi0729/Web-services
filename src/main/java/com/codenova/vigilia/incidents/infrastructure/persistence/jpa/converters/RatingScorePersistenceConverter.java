package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.converters;

import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
/**
 * Persists RatingScore as a nullable integer column
 */
@Converter
public class RatingScorePersistenceConverter implements AttributeConverter<RatingScore, Integer> {
    @Override
    public Integer convertToDatabaseColumn(RatingScore attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public RatingScore convertToEntityAttribute(Integer dbData) {
        return dbData == null ? null : new RatingScore(dbData);
    }
}