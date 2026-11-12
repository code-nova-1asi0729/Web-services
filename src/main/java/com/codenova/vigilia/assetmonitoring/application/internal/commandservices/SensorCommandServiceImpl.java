package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.AssignSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ReleaseSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.repositories.DeviceRepository;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SensorCommandServiceImpl implements SensorCommandService {

    private final SensorRepository sensorRepository;
    private final DeviceRepository deviceRepository;

    public SensorCommandServiceImpl(SensorRepository sensorRepository, DeviceRepository deviceRepository) {
        this.sensorRepository = sensorRepository;
        this.deviceRepository = deviceRepository;
    }

    @Override
    @Transactional
    public Result<Sensor, SensorCommandFailure> handle(RegisterSensorCommand command) {
        if (sensorRepository.existsBySerialNumber(command.serialNumber())) {
            log.warn("Duplicate sensor serial number: {}", command.serialNumber().value());
            return Result.failure(new SensorCommandFailure.DuplicateSerialNumber());
        }
        try {
            var sensor = sensorRepository.save(new Sensor(command));
            log.info("Sensor registered: id={}, serialNumber={}", sensor.getId(), sensor.getSerialNumber().value());
            return Result.success(sensor);
        } catch (DataIntegrityViolationException exception) {
            log.warn("Duplicate sensor serial number: {}", command.serialNumber().value());
            return Result.failure(new SensorCommandFailure.DuplicateSerialNumber());
        }
    }

    @Override
    @Transactional
    public Result<Sensor, SensorCommandFailure> handle(AssignSensorCommand command) {
        var sensor = sensorRepository.findById(command.sensorId());
        if (sensor.isEmpty()) return Result.failure(new SensorCommandFailure.NotFound());
        var equipment = deviceRepository.findById(command.equipmentId());
        if (equipment.isEmpty()) return Result.failure(new SensorCommandFailure.EquipmentNotFound());
        if (equipment.get().isDecommissioned()) return Result.failure(new SensorCommandFailure.EquipmentDecommissioned());
        if (!sensor.get().assignTo(command.equipmentId())) return Result.failure(new SensorCommandFailure.AlreadyAssigned());
        log.info("Sensor assigned: id={}, equipmentId={}", command.sensorId(), command.equipmentId());
        return Result.success(sensorRepository.save(sensor.get()));
    }

    @Override
    @Transactional
    public Result<Sensor, SensorCommandFailure> handle(ReleaseSensorCommand command) {
        var sensor = sensorRepository.findById(command.sensorId());
        if (sensor.isEmpty()) return Result.failure(new SensorCommandFailure.NotFound());
        if (!sensor.get().release()) return Result.failure(new SensorCommandFailure.NotAssigned());
        log.info("Sensor released: id={}", command.sensorId());
        return Result.success(sensorRepository.save(sensor.get()));
    }
}
