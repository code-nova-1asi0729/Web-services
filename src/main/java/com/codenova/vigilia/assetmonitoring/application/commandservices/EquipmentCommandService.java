package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.CriticalEquipment;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterEquipmentCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyEquipmentCommand;
import com.codenova.vigilia.shared.application.patterns.Result;

public interface EquipmentCommandService {

    Result<CriticalEquipment, EquipmentCommandFailure> handle(RegisterEquipmentCommand command);

    Result<CriticalEquipment, EquipmentCommandFailure> handle(ModifyEquipmentCommand command);
}