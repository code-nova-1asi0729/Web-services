package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;

import java.util.List;
import java.util.Optional;
/**
 * Domain contract to store and retrieve buildings
 */
public interface BuildingRepository {

    List<Building> findAll();

    Optional<Building> findById(Long id);

    boolean existsById(Long id);

    boolean existsByCode(BuildingCode code);

    Building save(Building building);

    void deleteById(Long id);
}