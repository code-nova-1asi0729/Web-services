# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [0.3.0] - 2026-11-13

### Added

- Alerts: `thresholdValue`, the normal limit of the metric, required when an alert is raised.
- Sensor readings: `CURRENT` and `PRESSURE` metrics.
- Tests for the device aggregate and for the alert threshold.

### Changed

- **Breaking:** critical equipment is now called device, as part of the ubiquitous language: `/api/v1/devices` endpoint, `devices` table and `deviceId` field in sensors, sensor readings, alerts and incidents.
- The README uses the new repository names.

### Removed

- `/api/v1/equipment` endpoint.

## [0.2.0] - 2026-11-09

### Added

- Asset Monitoring: register, assign, release and query IoT sensors.
- Asset Monitoring: record and query sensor readings, marked as invalid when the value is physically impossible.
- Incidents: start the management of, resolve and rate incidents through the `management-starts`, `resolutions` and `ratings` endpoints.
- HTTP requests for sensors, sensor readings, sensor demo data and validation interview data.

### Changed

- An incident can be resolved only after its management starts.

### Removed

- Generic `PUT /api/v1/incidents/{incidentId}` endpoint.

## [0.1.0] - 2026-11-07

### Added

- Shared kernel: naming strategy, result pattern, localized problem details, CORS, domain aggregate root and auditable persistence entity.
- Asset Monitoring: register, update, delete and query buildings.
- Asset Monitoring: register, modify and query critical equipment.
- Asset Monitoring: raise, acknowledge, resolve and query alerts.
- Incidents: report, follow up and rate incidents.
- Development and production profiles, and Dockerfile.
- Code conventions in the contributing guide.

### Changed

- Domain aggregates no longer depend on JPA: persistence goes through repository adapters.
- Building creation is now the `RegisterBuilding` command.
