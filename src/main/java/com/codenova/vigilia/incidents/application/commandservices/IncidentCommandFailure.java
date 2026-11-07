package com.codenova.vigilia.incidents.application.commandservices;
/**
 * Expected failures when handling incident commands
 */
public sealed interface IncidentCommandFailure permits IncidentCommandFailure.NotFound,
        IncidentCommandFailure.InvalidStatusTransition, IncidentCommandFailure.RatingNotAllowed {
    String messageKey();

    record NotFound() implements IncidentCommandFailure {
        @Override
        public String messageKey() {
            return "incident.error.notFound";
        }
    }

    record InvalidStatusTransition() implements IncidentCommandFailure {
        @Override
        public String messageKey() {
            return "incident.error.invalidTransition";
        }
    }

    record RatingNotAllowed() implements IncidentCommandFailure {
        @Override
        public String messageKey() {
            return "incident.error.ratingNotAllowed";
        }
    }
}