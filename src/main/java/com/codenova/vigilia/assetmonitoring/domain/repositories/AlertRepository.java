package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Alert;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve alerts.
 */
public interface AlertRepository {

    List<Alert> findAll();

    Optional<Alert> findById(Long id);

    List<Alert> findAllByStatus(AlertStatus status);

    Alert save(Alert alert);
}
