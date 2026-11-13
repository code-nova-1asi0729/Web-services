package com.codenova.vigilia.assetmonitoring.application.queryservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllSensorsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorsByDeviceIdQuery;

import java.util.List;
import java.util.Optional;

public interface SensorQueryService {

    List<Sensor> handle(GetAllSensorsQuery query);

    List<Sensor> handle(GetSensorsByDeviceIdQuery query);

    Optional<Sensor> handle(GetSensorByIdQuery query);
}
