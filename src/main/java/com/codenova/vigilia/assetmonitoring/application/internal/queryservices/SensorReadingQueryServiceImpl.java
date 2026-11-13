package com.codenova.vigilia.assetmonitoring.application.internal.queryservices;

import com.codenova.vigilia.assetmonitoring.application.queryservices.SensorReadingQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllSensorReadingsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorReadingByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorReadingsByDeviceIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorReadingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorReadingQueryServiceImpl implements SensorReadingQueryService {

    private final SensorReadingRepository sensorReadingRepository;

    public SensorReadingQueryServiceImpl(SensorReadingRepository sensorReadingRepository) {
        this.sensorReadingRepository = sensorReadingRepository;
    }

    @Override
    public List<SensorReading> handle(GetAllSensorReadingsQuery query) {
        return sensorReadingRepository.findAll();
    }

    @Override
    public List<SensorReading> handle(GetSensorReadingsByDeviceIdQuery query) {
        return sensorReadingRepository.findAllByDeviceId(query.deviceId());
    }

    @Override
    public Optional<SensorReading> handle(GetSensorReadingByIdQuery query) {
        return sensorReadingRepository.findById(query.readingId());
    }
}
