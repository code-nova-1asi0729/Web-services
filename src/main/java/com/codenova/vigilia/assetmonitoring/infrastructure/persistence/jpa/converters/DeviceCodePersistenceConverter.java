package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * Persists DeviceCode as a plain string column.
 */
@Converter
public class DeviceCodePersistenceConverter implements AttributeConverter<DeviceCode, String> {

    @Override
    public String convertToDatabaseColumn(DeviceCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public DeviceCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new DeviceCode(dbData);
    }
}
