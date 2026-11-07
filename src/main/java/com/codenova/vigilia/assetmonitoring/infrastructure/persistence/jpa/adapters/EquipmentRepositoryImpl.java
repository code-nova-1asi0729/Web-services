package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.EquipmentCode;
import com.codenova.vigilia.assetmonitoring.domain.repositories.EquipmentRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.EquipmentPersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.EquipmentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the equipment domain repository with Spring Data JPA.
 */
@Repository
public class EquipmentRepositoryImpl implements EquipmentRepository {

    private final EquipmentPersistenceRepository equipmentPersistenceRepository;

    public EquipmentRepositoryImpl(EquipmentPersistenceRepository equipmentPersistenceRepository) {
        this.equipmentPersistenceRepository = equipmentPersistenceRepository;
    }

    @Override
    public List<CriticalEquipment> findAll() {
        return equipmentPersistenceRepository.findAll().stream()
                .map(EquipmentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<CriticalEquipment> findAllByBuildingId(Long buildingId) {
        return equipmentPersistenceRepository.findAllByBuildingId(buildingId).stream()
                .map(EquipmentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<CriticalEquipment> findById(Long id) {
        return equipmentPersistenceRepository.findById(id)
                .map(EquipmentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByBuildingIdAndCode(Long buildingId, EquipmentCode code) {
        return equipmentPersistenceRepository.existsByBuildingIdAndCode(buildingId, code);
    }

    @Override
    public CriticalEquipment save(CriticalEquipment equipment) {
        var saved = equipmentPersistenceRepository.save(EquipmentPersistenceAssembler.toPersistenceFromDomain(equipment));
        return EquipmentPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
