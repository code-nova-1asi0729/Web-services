package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.BuildingPersistenceEntity;

/**
 * Converts between the Building aggregate and its persistence entity.
 */
public final class BuildingPersistenceAssembler {

    private BuildingPersistenceAssembler() {
    }

    public static Building toDomainFromPersistence(BuildingPersistenceEntity entity) {
        return Building.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getAddress(),
                entity.getDistrict(),
                entity.getTotalUnits(),
                entity.getStatus());
    }

    public static BuildingPersistenceEntity toPersistenceFromDomain(Building building) {
        var entity = new BuildingPersistenceEntity();
        // A new building has no id yet, so JPA generates it
        if (building.getId() != null) entity.setId(building.getId());
        entity.setName(building.getName());
        entity.setCode(building.getCode());
        entity.setAddress(building.getAddress());
        entity.setDistrict(building.getDistrict());
        entity.setTotalUnits(building.getTotalUnits());
        entity.setStatus(building.getStatus());
        return entity;
    }
}
