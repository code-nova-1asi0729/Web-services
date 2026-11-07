package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.BuildingPersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.BuildingPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the building domain repository with Spring Data JPA.
 */
@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    private final BuildingPersistenceRepository buildingPersistenceRepository;

    public BuildingRepositoryImpl(BuildingPersistenceRepository buildingPersistenceRepository) {
        this.buildingPersistenceRepository = buildingPersistenceRepository;
    }

    @Override
    public List<Building> findAll() {
        return buildingPersistenceRepository.findAll().stream()
                .map(BuildingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Building> findById(Long id) {
        return buildingPersistenceRepository.findById(id)
                .map(BuildingPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsById(Long id) {
        return buildingPersistenceRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(BuildingCode code) {
        return buildingPersistenceRepository.existsByCode(code);
    }

    @Override
    public Building save(Building building) {
        var saved = buildingPersistenceRepository.save(BuildingPersistenceAssembler.toPersistenceFromDomain(building));
        return BuildingPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        buildingPersistenceRepository.deleteById(id);
    }
}
