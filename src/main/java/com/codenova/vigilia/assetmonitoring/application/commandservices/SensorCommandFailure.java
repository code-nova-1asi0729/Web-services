package com.codenova.vigilia.assetmonitoring.application.commandservices;

public sealed interface SensorCommandFailure permits SensorCommandFailure.DuplicateSerialNumber,
        SensorCommandFailure.NotFound, SensorCommandFailure.EquipmentNotFound,
        SensorCommandFailure.EquipmentDecommissioned, SensorCommandFailure.AlreadyAssigned,
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

    record EquipmentNotFound() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.equipmentNotFound";
        }
    }

    record EquipmentDecommissioned() implements SensorCommandFailure {
        @Override
        public String messageKey() {
            return "sensor.error.equipmentDecommissioned";
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
