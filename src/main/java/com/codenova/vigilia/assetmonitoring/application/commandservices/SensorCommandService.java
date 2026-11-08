package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Sensor;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.AssignSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RegisterSensorCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.ReleaseSensorCommand;
import com.codenova.vigilia.shared.application.patterns.Result;

public interface SensorCommandService {

    Result<Sensor, SensorCommandFailure> handle(RegisterSensorCommand command);

    Result<Sensor, SensorCommandFailure> handle(AssignSensorCommand command);

    Result<Sensor, SensorCommandFailure> handle(ReleaseSensorCommand command);
}
