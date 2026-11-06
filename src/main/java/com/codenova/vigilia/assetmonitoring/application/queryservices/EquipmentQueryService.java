package com.codenova.vigilia.assetmonitoring.application.queryservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllEquipmentQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetEquipmentByBuildingIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetEquipmentByIdQuery;

import java.util.List;
import java.util.Optional;

public interface EquipmentQueryService {

    List<CriticalEquipment> handle(GetAllEquipmentQuery query);

    List<CriticalEquipment> handle(GetEquipmentByBuildingIdQuery query);

    Optional<CriticalEquipment> handle(GetEquipmentByIdQuery query);
}