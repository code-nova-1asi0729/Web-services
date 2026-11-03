package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.BuildingCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.BuildingCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.DeleteBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.BuildingRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Default implementation of BuildingCommandService.
 */
@Slf4j
@Service
public class BuildingCommandServiceImpl implements BuildingCommandService {
    private final BuildingRepository buildingRepository;

    public BuildingCommandServiceImpl(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Override
    @Transactional
    public Result<Building, BuildingCommandFailure> handle(CreateBuildingCommand command) {
        if (buildingRepository.existsByCode(command.code())) {
            log.warn("Duplicate building code: {}", command.code().value());
            return Result.failure(new BuildingCommandFailure.DuplicateCode());
        }
        try {
            var building = buildingRepository.save(new Building(command));
            log.info("Building created: id={}, code={}", building.getId(), building.getCode().value());
            return Result.success(building);
        } catch (DataIntegrityViolationException exception) {
            log.warn("Duplicate building code: {}", command.code().value());
            return Result.failure(new BuildingCommandFailure.DuplicateCode());
        }
    }

    @Override
    @Transactional
    public Result<Building, BuildingCommandFailure> handle(UpdateBuildingCommand command) {
        var building = buildingRepository.findById(command.buildingId());
        if (building.isEmpty()) {
            log.warn("Building not found: id={}", command.buildingId());
            return Result.failure(new BuildingCommandFailure.NotFound());
        }
        building.get().update(command);
        var updated = buildingRepository.save(building.get());
        log.info("Building updated: id={}", updated.getId());
        return Result.success(updated);
    }

    @Override
    @Transactional
    public Result<Building, BuildingCommandFailure> handle(DeleteBuildingCommand command) {
        var building = buildingRepository.findById(command.buildingId());
        if (building.isEmpty()) {
            log.warn("Building not found: id={}", command.buildingId());
            return Result.failure(new BuildingCommandFailure.NotFound());
        }
        buildingRepository.delete(building.get());
        log.info("Building deleted: id={}", command.buildingId());
        return Result.success(building.get());
    }
}