package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RecordReadingCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RecordReadingResource;

public class RecordReadingCommandFromResourceAssembler {

    public static RecordReadingCommand toCommandFromResource(RecordReadingResource resource) {
        return new RecordReadingCommand(resource.sensorId(), resource.metric(), resource.value(), resource.unit(),
                resource.recordedAt());
    }
}
