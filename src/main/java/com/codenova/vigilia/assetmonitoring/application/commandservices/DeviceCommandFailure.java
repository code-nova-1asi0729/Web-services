package com.codenova.vigilia.assetmonitoring.application.commandservices;

public sealed interface DeviceCommandFailure permits DeviceCommandFailure.DuplicateCode,
        DeviceCommandFailure.NotFound, DeviceCommandFailure.BuildingNotFound,
        DeviceCommandFailure.AlreadyDecommissioned {

    String messageKey();

    record DuplicateCode() implements DeviceCommandFailure {
        @Override
        public String messageKey() {
            return "device.error.duplicateCode";
        }
    }

    record NotFound() implements DeviceCommandFailure {
        @Override
        public String messageKey() {
            return "device.error.notFound";
        }
    }

    record BuildingNotFound() implements DeviceCommandFailure {
        @Override
        public String messageKey() {
            return "device.error.buildingNotFound";
        }
    }

    record AlreadyDecommissioned() implements DeviceCommandFailure {
        @Override
        public String messageKey() {
            return "device.error.alreadyDecommissioned";
        }
    }
}