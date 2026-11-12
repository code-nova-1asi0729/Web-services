package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Device;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterDeviceCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ModifyDeviceCommand;
import com.codenova.vigilia.shared.application.patterns.Result;

public interface DeviceCommandService {

    Result<Device, DeviceCommandFailure> handle(RegisterDeviceCommand command);

    Result<Device, DeviceCommandFailure> handle(ModifyDeviceCommand command);
}