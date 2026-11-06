package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
public interface EquipmentRepository extends JpaRepository<CriticalEquipment, Long>{
    boolean existsByBuildingIdAndCode(Long buildingId, EquipmentCode code);

    List<CriticalEquipment> findAllByBuildingId(Long buildingId);
}
