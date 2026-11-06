package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.EquipmentCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.EquipmentCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.EquipmentRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class EquipmentCommandServiceImpl implements EquipmentCommandService {

    private final EquipmentRepository equipmentRepository;
    private final BuildingRepository buildingRepository;

    public EquipmentCommandServiceImpl(EquipmentRepository equipmentRepository,
                                       BuildingRepository buildingRepository) {
        this.equipmentRepository = equipmentRepository;
        this.buildingRepository = buildingRepository;
    }

    @Override
    @Transactional
    public Result<CriticalEquipment, EquipmentCommandFailure> handle(RegisterEquipmentCommand command) {
        if (!buildingRepository.existsById(command.buildingId())) {
            log.warn("Building not found: id={}", command.buildingId());
            return Result.failure(new EquipmentCommandFailure.BuildingNotFound());
        }
        if (equipmentRepository.existsByBuildingIdAndCode(command.buildingId(), command.code())) {
            log.warn("Duplicate equipment code: {}", command.code().value());
            return Result.failure(new EquipmentCommandFailure.DuplicateCode());
        }
        try {
            var equipment = equipmentRepository.save(new CriticalEquipment(command));
            log.info("Equipment created: id={}, code={}", equipment.getId(), equipment.getCode().value());
            return Result.success(equipment);
        } catch (DataIntegrityViolationException exception) {
            log.warn("Duplicate equipment code: {}", command.code().value());
            return Result.failure(new EquipmentCommandFailure.DuplicateCode());
        }
    }

    @Override
    @Transactional
    public Result<CriticalEquipment, EquipmentCommandFailure> handle(ModifyEquipmentCommand command) {
        var equipment = equipmentRepository.findById(command.equipmentId());
        if (equipment.isEmpty()) return Result.failure(new EquipmentCommandFailure.NotFound());
        if (equipment.get().isDecommissioned()) return Result.failure(new EquipmentCommandFailure.AlreadyDecommissioned());
        equipment.get().update(command);
        return Result.success(equipmentRepository.save(equipment.get()));
    }
}