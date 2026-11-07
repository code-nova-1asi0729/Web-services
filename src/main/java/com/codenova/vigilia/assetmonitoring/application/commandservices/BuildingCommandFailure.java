package com.codenova.vigilia.assetmonitoring.application.commandservices;
/**
 * Expected failures when handling building commands.
 */
public sealed interface BuildingCommandFailure
        permits BuildingCommandFailure.DuplicateCode, BuildingCommandFailure.NotFound {
    String messageKey();

    record DuplicateCode() implements BuildingCommandFailure {
        @Override
        public String messageKey() {
            return "building.error.duplicateCode";
        }
    }

    record NotFound() implements BuildingCommandFailure {
        @Override
        public String messageKey() {
            return "building.error.notFound";
        }
    }
}