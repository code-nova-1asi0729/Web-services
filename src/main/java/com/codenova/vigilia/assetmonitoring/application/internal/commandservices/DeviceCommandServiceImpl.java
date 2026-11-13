package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.DeviceCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.DeviceCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
import com.codenova.vigilia.assetmonitoring.domain.repositories.DeviceRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class DeviceCommandServiceImpl implements DeviceCommandService {

    private final DeviceRepository deviceRepository;
    private final BuildingRepository buildingRepository;

    public DeviceCommandServiceImpl(DeviceRepository deviceRepository,
                                    BuildingRepository buildingRepository) {
        this.deviceRepository = deviceRepository;
        this.buildingRepository = buildingRepository;
    }

    @Override
    @Transactional
    public Result<Device, DeviceCommandFailure> handle(RegisterDeviceCommand command) {
        if (!buildingRepository.existsById(command.buildingId())) {
            log.warn("Building not found: id={}", command.buildingId());
            return Result.failure(new DeviceCommandFailure.BuildingNotFound());
        }
        if (deviceRepository.existsByBuildingIdAndCode(command.buildingId(), command.code())) {
            log.warn("Duplicate device code: {}", command.code().value());
            return Result.failure(new DeviceCommandFailure.DuplicateCode());
        }
        try {
            var device = deviceRepository.save(new Device(command));
            log.info("Device registered: id={}, code={}", device.getId(), device.getCode().value());
            return Result.success(device);
        } catch (DataIntegrityViolationException exception) {
            log.warn("Duplicate device code: {}", command.code().value());
            return Result.failure(new DeviceCommandFailure.DuplicateCode());
        }
    }

    @Override
    @Transactional
    public Result<Device, DeviceCommandFailure> handle(ModifyDeviceCommand command) {
        var device = deviceRepository.findById(command.deviceId());
        if (device.isEmpty()) return Result.failure(new DeviceCommandFailure.NotFound());
        if (device.get().isDecommissioned()) return Result.failure(new DeviceCommandFailure.AlreadyDecommissioned());
        device.get().update(command);
        return Result.success(deviceRepository.save(device.get()));
    }
}