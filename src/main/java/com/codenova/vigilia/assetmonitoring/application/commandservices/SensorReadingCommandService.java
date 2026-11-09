package com.codenova.vigilia.assetmonitoring.application.commandservices;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.SensorReading;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.RecordReadingCommand;
import com.codenova.vigilia.shared.application.patterns.Result;

public interface SensorReadingCommandService {

    Result<SensorReading, SensorReadingCommandFailure> handle(RecordReadingCommand command);
}
