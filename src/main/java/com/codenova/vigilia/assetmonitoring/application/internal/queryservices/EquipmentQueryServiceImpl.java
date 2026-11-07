package com.codenova.vigilia.assetmonitoring.application.internal.queryservices;

import com.codenova.vigilia.assetmonitoring.application.queryservices.EquipmentQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllEquipmentQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetEquipmentByBuildingIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetEquipmentByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.repositories.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EquipmentQueryServiceImpl implements EquipmentQueryService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentQueryServiceImpl(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    @Override
    public List<CriticalEquipment> handle(GetAllEquipmentQuery query) {
        return equipmentRepository.findAll();
    }

    @Override
    public List<CriticalEquipment> handle(GetEquipmentByBuildingIdQuery query) {
        return equipmentRepository.findAllByBuildingId(query.buildingId());
    }

    @Override
    public Optional<CriticalEquipment> handle(GetEquipmentByIdQuery query) {
        return equipmentRepository.findById(query.equipmentId());
    }
}