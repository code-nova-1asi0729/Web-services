package com.codenova.vigilia.assetmonitoring.application.queryservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllSensorReadingsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorReadingByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetSensorReadingsByDeviceIdQuery;

import java.util.List;
import java.util.Optional;

public interface SensorReadingQueryService {

    List<SensorReading> handle(GetAllSensorReadingsQuery query);

    List<SensorReading> handle(GetSensorReadingsByDeviceIdQuery query);

    Optional<SensorReading> handle(GetSensorReadingByIdQuery query);
}
