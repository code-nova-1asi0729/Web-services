package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class EquipmentCodeAttributeConverter implements AttributeConverter<EquipmentCode, String> {

    @Override
    public String convertToDatabaseColumn(EquipmentCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public EquipmentCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new EquipmentCode(dbData);
    }
}