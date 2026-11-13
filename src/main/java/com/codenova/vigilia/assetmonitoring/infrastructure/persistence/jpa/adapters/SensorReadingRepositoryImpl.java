package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.domain.repositories.SensorReadingRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.SensorReadingPersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.SensorReadingPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the sensor reading domain repository with Spring Data JPA.
 */
@Repository
public class SensorReadingRepositoryImpl implements SensorReadingRepository {

    private final SensorReadingPersistenceRepository sensorReadingPersistenceRepository;

    public SensorReadingRepositoryImpl(SensorReadingPersistenceRepository sensorReadingPersistenceRepository) {
        this.sensorReadingPersistenceRepository = sensorReadingPersistenceRepository;
    }

    @Override
    public List<SensorReading> findAll() {
        return sensorReadingPersistenceRepository.findAllByOrderByRecordedAtDesc().stream()
                .map(SensorReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<SensorReading> findAllByDeviceId(Long deviceId) {
        return sensorReadingPersistenceRepository.findAllByDeviceIdOrderByRecordedAtDesc(deviceId).stream()
                .map(SensorReadingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<SensorReading> findById(Long id) {
        return sensorReadingPersistenceRepository.findById(id)
                .map(SensorReadingPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public SensorReading save(SensorReading reading) {
        var saved = sensorReadingPersistenceRepository.save(SensorReadingPersistenceAssembler.toPersistenceFromDomain(reading));
        return SensorReadingPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
