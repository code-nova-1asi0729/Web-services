package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorReadingCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.SensorReadingCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RecordReadingCommand;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorReadingRepository;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
public class SensorReadingCommandServiceImpl implements SensorReadingCommandService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingCommandServiceImpl(SensorReadingRepository sensorReadingRepository,
                                           SensorRepository sensorRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
        this.sensorRepository = sensorRepository;
    }

    @Override
    @Transactional
    public Result<SensorReading, SensorReadingCommandFailure> handle(RecordReadingCommand command) {
        var sensor = sensorRepository.findById(command.sensorId());
        if (sensor.isEmpty()) return Result.failure(new SensorReadingCommandFailure.SensorNotFound());
        if (!sensor.get().isAssigned()) return Result.failure(new SensorReadingCommandFailure.SensorNotAssigned());

        var reading = sensorReadingRepository.save(new SensorReading(command, sensor.get().getEquipmentId()));
        sensor.get().registerSignal(reading.getRecordedAt());
        sensorRepository.save(sensor.get());

        if (!reading.isValid()) log.warn("Invalid reading rejected: sensorId={}, metric={}, value={}",
                reading.getSensorId(), reading.getMetric(), reading.getValue());
        return Result.success(reading);
    }
}
