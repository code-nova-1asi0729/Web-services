package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.AlertStatus;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.AlertPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for alert persistence entities.
 */
@Repository
public interface AlertPersistenceRepository extends JpaRepository<AlertPersistenceEntity, Long> {
    List<AlertPersistenceEntity> findAllByStatus(AlertStatus status);
}
