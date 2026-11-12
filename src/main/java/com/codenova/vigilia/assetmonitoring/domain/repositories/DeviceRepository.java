package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve critical device.
 */
public interface DeviceRepository {

    List<Device> findAll();

    List<Device> findAllByBuildingId(Long buildingId);

    Optional<Device> findById(Long id);

    boolean existsByBuildingIdAndCode(Long buildingId, DeviceCode code);

    Device save(Device device);
}
