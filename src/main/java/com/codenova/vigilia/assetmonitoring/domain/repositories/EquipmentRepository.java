package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve critical equipment.
 */
public interface EquipmentRepository {

    List<CriticalEquipment> findAll();

    List<CriticalEquipment> findAllByBuildingId(Long buildingId);

    Optional<CriticalEquipment> findById(Long id);

    boolean existsByBuildingIdAndCode(Long buildingId, EquipmentCode code);

    CriticalEquipment save(CriticalEquipment equipment);
}
