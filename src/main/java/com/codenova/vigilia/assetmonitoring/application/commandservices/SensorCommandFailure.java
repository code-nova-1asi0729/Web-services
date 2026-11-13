package com.codenova.vigilia.assetmonitoring.application.commandservices;

public sealed interface SensorCommandFailure permits SensorCommandFailure.DuplicateSerialNumber,
        SensorCommandFailure.NotFound, SensorCommandFailure.DeviceNotFound,
        SensorCommandFailure.DeviceDecommissioned, SensorCommandFailure.AlreadyAssigned,
        SensorCommandFailure.NotAssigned {

    String messageKey();

    record DuplicateSerialNumber() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.duplicateSerialNumber";
        }
    }

    record NotFound() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.notFound";
        }
    }

    record DeviceNotFound() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.deviceNotFound";
        }
    }

    record DeviceDecommissioned() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.deviceDecommissioned";
        }
    }

    record AlreadyAssigned() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.alreadyAssigned";
        }
    }

    record NotAssigned() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.notAssigned";
        }
    }
}
