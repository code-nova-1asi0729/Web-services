package com.codenova.vigilia.assetmonitoring.application.queryservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetAllBuildingsQuery;
import com.codenova.vigilia.assetmonitoring.domain.model.queries.GetBuildingByIdQuery;

import java.util.List;
import java.util.Optional;
/**
 * Handles queries that read buildings.
 */
public interface BuildingQueryService {
    List<Building> handle(GetAllBuildingsQuery query);

    Optional<Building> handle(GetBuildingByIdQuery query);
}