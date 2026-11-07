package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
import com.codenova.vigilia.assetmonitoring.domain.repositories.AlertRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.AlertPersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.AlertPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the alert domain repository with Spring Data JPA.
 */
@Repository
public class AlertRepositoryImpl implements AlertRepository {

    private final AlertPersistenceRepository alertPersistenceRepository;

    public AlertRepositoryImpl(AlertPersistenceRepository alertPersistenceRepository) {
        this.alertPersistenceRepository = alertPersistenceRepository;
    }

    @Override
    public List<Alert> findAll() {
        return alertPersistenceRepository.findAll().stream()
                .map(AlertPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Alert> findById(Long id) {
        return alertPersistenceRepository.findById(id)
                .map(AlertPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Alert> findAllByStatus(AlertStatus status) {
        return alertPersistenceRepository.findAllByStatus(status).stream()
                .map(AlertPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Alert save(Alert alert) {
        var saved = alertPersistenceRepository.save(AlertPersistenceAssembler.toPersistenceFromDomain(alert));
        return AlertPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
