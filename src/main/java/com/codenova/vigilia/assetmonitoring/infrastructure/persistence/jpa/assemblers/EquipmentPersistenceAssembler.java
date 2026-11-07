package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.EquipmentPersistenceEntity;

/**
 * Converts between the CriticalEquipment aggregate and its persistence entity.
 */
public final class EquipmentPersistenceAssembler {

    private EquipmentPersistenceAssembler() {
    }

    public static CriticalEquipment toDomainFromPersistence(EquipmentPersistenceEntity entity) {
        return CriticalEquipment.reconstitute(
                entity.getId(),
                entity.getBuildingId(),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getLocation(),
                entity.getInstallationDate(),
                entity.getStatus());
    }

    public static EquipmentPersistenceEntity toPersistenceFromDomain(CriticalEquipment equipment) {
        var entity = new EquipmentPersistenceEntity();
        // A new equipment has no id yet, so JPA generates it
        if (equipment.getId() != null) entity.setId(equipment.getId());
        entity.setBuildingId(equipment.getBuildingId());
        entity.setCode(equipment.getCode());
        entity.setName(equipment.getName());
        entity.setType(equipment.getType());
        entity.setLocation(equipment.getLocation());
        entity.setInstallationDate(equipment.getInstallationDate());
        entity.setStatus(equipment.getStatus());
        return entity;
    }
}
