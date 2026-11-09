package com.codenova.vigilia.assetmonitoring.application.commandservices;

public sealed interface SensorReadingCommandFailure permits SensorReadingCommandFailure.SensorNotFound,
        SensorReadingCommandFailure.SensorNotAssigned {

    String messageKey();

    record SensorNotFound() implements SensorReadingCommandFailure {
        @Override
        public String messageKey() {
            return "reading.error.sensorNotFound";
        }
    }

    record SensorNotAssigned() implements SensorReadingCommandFailure {
        @Override
        public String messageKey() {
            return "reading.error.sensorNotAssigned";
        }
    }
}
