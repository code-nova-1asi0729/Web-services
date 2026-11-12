package com.codenova.vigilia.assetmonitoring.application.internal.queryservices;

import com.codenova.vigilia.assetmonitoring.application.queryservices.DeviceQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllDevicesQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetDevicesByBuildingIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetDeviceByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.repositories.DeviceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeviceQueryServiceImpl implements DeviceQueryService {

    private final DeviceRepository deviceRepository;

    public DeviceQueryServiceImpl(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Override
    public List<Device> handle(GetAllDevicesQuery query) {
        return deviceRepository.findAll();
    }

    @Override
    public List<Device> handle(GetDevicesByBuildingIdQuery query) {
        return deviceRepository.findAllByBuildingId(query.buildingId());
    }

    @Override
    public Optional<Device> handle(GetDeviceByIdQuery query) {
        return deviceRepository.findById(query.deviceId());
    }
}