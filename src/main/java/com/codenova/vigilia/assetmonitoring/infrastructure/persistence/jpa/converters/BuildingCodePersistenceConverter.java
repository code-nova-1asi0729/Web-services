package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
/**
 * Persists BuildingCode as a plain string column.
 */
@Converter
public class BuildingCodePersistenceConverter implements AttributeConverter<BuildingCode, String> {
    @Override
    public String convertToDatabaseColumn(BuildingCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public BuildingCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new BuildingCode(dbData);
    }
}