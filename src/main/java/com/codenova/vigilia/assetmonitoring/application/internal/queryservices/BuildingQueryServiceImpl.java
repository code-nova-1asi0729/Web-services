package com.codenova.vigilia.assetmonitoring.application.internal.queryservices;

import com.codenova.vigilia.assetmonitoring.application.queryservices.BuildingQueryService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllBuildingsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetBuildingByIdQuery;
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
/**
 * Default implementation of BuildingQueryService.
 */
@Service
public class BuildingQueryServiceImpl implements BuildingQueryService {
    private final BuildingRepository buildingRepository;

    public BuildingQueryServiceImpl(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Override
    public List<Building> handle(GetAllBuildingsQuery query) {
        return buildingRepository.findAll();
    }

    @Override
    public Optional<Building> handle(GetBuildingByIdQuery query) {
        return buildingRepository.findById(query.buildingId());
    }
}