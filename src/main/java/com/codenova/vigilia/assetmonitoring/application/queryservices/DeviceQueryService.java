package com.codenova.vigilia.assetmonitoring.application.queryservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllDevicesQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetDevicesByBuildingIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetDeviceByIdQuery;

import java.util.List;
import java.util.Optional;

public interface DeviceQueryService {

    List<Device> handle(GetAllDevicesQuery query);

    List<Device> handle(GetDevicesByBuildingIdQuery query);

    Optional<Device> handle(GetDeviceByIdQuery query);
}