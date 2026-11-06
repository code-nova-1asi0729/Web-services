package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.repositories.IncidentRepository;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.assemblers.IncidentPersistenceAssembler;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.repositories.IncidentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the incident domain repository with Spring Data JPA.
 */
@Repository
public class IncidentRepositoryImpl implements IncidentRepository {

    private final IncidentPersistenceRepository incidentPersistenceRepository;

    public IncidentRepositoryImpl(IncidentPersistenceRepository incidentPersistenceRepository) {
        this.incidentPersistenceRepository = incidentPersistenceRepository;
    }

    @Override
    public List<Incident> findAll() {
        return incidentPersistenceRepository.findAll().stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Incident> findById(Long id) {
        return incidentPersistenceRepository.findById(id)
                .map(IncidentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Incident> findAllByBuildingId(Long buildingId) {
        return incidentPersistenceRepository.findAllByBuildingId(buildingId).stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Incident save(Incident incident) {
        var saved = incidentPersistenceRepository.save(IncidentPersistenceAssembler.toPersistenceFromDomain(incident));
        return IncidentPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
