package com.codenova.vigilia.assetmonitoring.application.commandservices;

public sealed interface EquipmentCommandFailure permits EquipmentCommandFailure.DuplicateCode,
        EquipmentCommandFailure.NotFound, EquipmentCommandFailure.BuildingNotFound,
        EquipmentCommandFailure.AlreadyDecommissioned {

    String messageKey();

    record DuplicateCode() implements EquipmentCommandFailure {
        @Override
        public String messageKey() {
            return "equipment.error.duplicateCode";
        }
    }

    record NotFound() implements EquipmentCommandFailure {
        @Override
        public String messageKey() {
            return "equipment.error.notFound";
        }
    }

    record BuildingNotFound() implements EquipmentCommandFailure {
        @Override
        public String messageKey() {
            return "equipment.error.buildingNotFound";
        }
    }

    record AlreadyDecommissioned() implements EquipmentCommandFailure {
        @Override
        public String messageKey() {
            return "equipment.error.alreadyDecommissioned";
        }
    }
}