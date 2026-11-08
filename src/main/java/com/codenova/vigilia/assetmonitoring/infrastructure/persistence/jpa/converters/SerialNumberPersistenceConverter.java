package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Persists SerialNumber as a plain string column.
 */
@Converter
public class SerialNumberPersistenceConverter implements AttributeConverter<SerialNumber, String> {

    @Override
    public String convertToDatabaseColumn(SerialNumber attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public SerialNumber convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new SerialNumber(dbData);
    }
}
