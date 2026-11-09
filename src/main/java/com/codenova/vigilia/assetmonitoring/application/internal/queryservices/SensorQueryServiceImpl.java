package com.codenova.vigilia.assetmonitoring.application.internal.queryservices;

import com.codenova.vigilia.assetmonitoring.application.queryservices.SensorQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllSensorsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorsByEquipmentIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SensorQueryServiceImpl implements SensorQueryService {

    private final SensorRepository sensorRepository;

    public SensorQueryServiceImpl(SensorRepository sensorRepository) {
        this.sensorRepository = sensorRepository;
    }

    @Override
    public List<Sensor> handle(GetAllSensorsQuery query) {
        return sensorRepository.findAll();
    }

    @Override
    public List<Sensor> handle(GetSensorsByEquipmentIdQuery query) {
        return sensorRepository.findAllByEquipmentId(query.equipmentId());
    }

    @Override
    public Optional<Sensor> handle(GetSensorByIdQuery query) {
        return sensorRepository.findById(query.sensorId());
    }
}
