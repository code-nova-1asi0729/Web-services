package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.DeviceCode;
import com.codenova.vigilia.assetmonitoring.domain.repositories.DeviceRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.DevicePersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.DevicesPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DeviceRepositoryImpl implements DeviceRepository {

    private final DevicesPersistenceRepository devicesPersistenceRepository;

    public DeviceRepositoryImpl(DevicesPersistenceRepository devicesPersistenceRepository) {
        this.devicesPersistenceRepository = devicesPersistenceRepository;
    }

    @Override
    public List<Device> findAll() {
        return devicesPersistenceRepository.findAll().stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Device> findAllByBuildingId(Long buildingId) {
        return devicesPersistenceRepository.findAllByBuildingId(buildingId).stream()
                .map(DevicePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Device> findById(Long id) {
        return devicesPersistenceRepository.findById(id)
                .map(DevicePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByBuildingIdAndCode(Long buildingId, DeviceCode code) {
        return devicesPersistenceRepository.existsByBuildingIdAndCode(buildingId, code);
    }

    @Override
    public Device save(Device device) {
    var deviceEntity = DevicePersistenceAssembler.toPersistenceFromDomain(device);
    var savedEntity = devicesPersistenceRepository.save(deviceEntity);
    return DevicePersistenceAssembler.toDomainFromPersistence(savedEntity);

    }
}
