package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.SerialNumber;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.SensorPersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.SensorPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the sensor domain repository with Spring Data JPA.
 */
@Repository
public class SensorRepositoryImpl implements SensorRepository {

    private final SensorPersistenceRepository sensorPersistenceRepository;

    public SensorRepositoryImpl(SensorPersistenceRepository sensorPersistenceRepository) {
        this.sensorPersistenceRepository = sensorPersistenceRepository;
    }

    @Override
    public List<Sensor> findAll() {
        return sensorPersistenceRepository.findAll().stream()
                .map(SensorPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Sensor> findAllByEquipmentId(Long equipmentId) {
        return sensorPersistenceRepository.findAllByEquipmentId(equipmentId).stream()
                .map(SensorPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Sensor> findById(Long id) {
        return sensorPersistenceRepository.findById(id)
                .map(SensorPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsBySerialNumber(SerialNumber serialNumber) {
        return sensorPersistenceRepository.existsBySerialNumber(serialNumber);
    }

    @Override
    public Sensor save(Sensor sensor) {
        var saved = sensorPersistenceRepository.save(SensorPersistenceAssembler.toPersistenceFromDomain(sensor));
        return SensorPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
