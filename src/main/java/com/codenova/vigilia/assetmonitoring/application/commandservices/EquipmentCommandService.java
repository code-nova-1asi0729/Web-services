package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateEquipmentCommand;
import com.codenova.vigilia.shared.application.patterns.Result;

public interface EquipmentCommandService {

    Result<CriticalEquipment, EquipmentCommandFailure> handle(CreateEquipmentCommand command);

    Result<CriticalEquipment, EquipmentCommandFailure> handle(UpdateEquipmentCommand command);
}