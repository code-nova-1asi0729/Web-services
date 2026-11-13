package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.DevicePersistenceEntity;

/**
 * Converts between the CriticalDevice aggregate and its persistence entity.
 */
public final class DevicePersistenceAssembler {

    private DevicePersistenceAssembler() {
    }

    public static Device toDomainFromPersistence(DevicePersistenceEntity entity) {
        return Device.reconstitute(
                entity.getId(),
                entity.getBuildingId(),
                entity.getCode(),
                entity.getName(),
                entity.getType(),
                entity.getLocation(),
                entity.getInstallationDate(),
                entity.getStatus());
    }

    public static DevicePersistenceEntity toPersistenceFromDomain(Device device) {
        var entity = new DevicePersistenceEntity();
        // A new device has no id yet, so JPA generates it
        if (device.getId() != null) entity.setId(device.getId());
        entity.setBuildingId(device.getBuildingId());
        entity.setCode(device.getCode());
        entity.setName(device.getName());
        entity.setType(device.getType());
        entity.setLocation(device.getLocation());
        entity.setInstallationDate(device.getInstallationDate());
        entity.setStatus(device.getStatus());
        return entity;
    }
}
