package com.codenova.vigilia.assetmonitoring.interfaces.rest.transform;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.RaiseAlertCommand;
import com.codenova.vigilia.assetmonitoring.interfaces.rest.resources.RaiseAlertResource;
/**
 * Builds a RaiseAlertCommand from its REST resource.
 */
public class RaiseAlertCommandFromResourceAssembler {
    public static RaiseAlertCommand toCommandFromResource(RaiseAlertResource resource) {
        return new RaiseAlertCommand(
                resource.equipmentId(),
                resource.metric(),
                resource.severity(),
                resource.lastValue());
    }
}
