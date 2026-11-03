package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.DeleteBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.shared.application.patterns.Result;
/**
 * Handles commands that change buildings.
 */
public interface BuildingCommandService {

    Result<Building, BuildingCommandFailure> handle(CreateBuildingCommand command);

    Result<Building, BuildingCommandFailure> handle(UpdateBuildingCommand command);

    Result<Building, BuildingCommandFailure> handle(DeleteBuildingCommand command);
}