# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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
