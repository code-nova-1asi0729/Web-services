package com.codenova.vigilia.assetmonitoring.application.commandservices;
/**
 * Expected failures when handling alert commands.
 */
public sealed interface AlertCommandFailure permits AlertCommandFailure.NotFound,
        AlertCommandFailure.InvalidStatusTransition {
    String messageKey();

    record NotFound() implements AlertCommandFailure {
        @Override
        public String messageKey() {
            return "alert.error.notFound";
        }
    }

    record InvalidStatusTransition() implements AlertCommandFailure {
        @Override
        public String messageKey() {
            return "alert.error.invalidTransition";
        }
    }
}
