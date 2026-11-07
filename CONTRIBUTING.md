# Contributing to Vigilia Platform API

## Branching model

We use GitFlow:

- `main`: released versions only.
- `develop`: integration branch.
- `feature/<name>`: new work, created from `develop`.
- `release/x.y.z`: release preparation, created from `develop`.
- `hotfix/x.y.z`: urgent fixes, created from `main`.

## Commit messages

We follow [Conventional Commits](https://www.conventionalcommits.org/) in English, for example `feat(buildings): add building aggregate root`.

## Pull requests

Open pull requests against `develop`. Keep them small and describe what changed.

## Versioning

We use [Semantic Versioning](https://semver.org/).

## Code conventions

We follow Domain-Driven Design with four layers per bounded context: `domain`, `application`, `infrastructure` and `interfaces`.

- **Commands** use the ubiquitous language of our Event Storming (section 4.6 of the report), not CRUD verbs: `RegisterBuildingCommand`, `RegisterEquipmentCommand`, `ReportIncidentCommand`, `AcknowledgeAlertCommand`.
- **Queries** start with `Get` (`GetAlertsByStatusQuery`). Repository methods use `find`, `exists` and `save`.
- **Domain layer** has no JPA or Spring Data annotations. Each aggregate has one repository contract in `domain/repositories`.
- **Persistence** lives in `infrastructure/persistence/jpa`: `XPersistenceEntity` (with `@Table`), `XPersistenceRepository`, `XPersistenceAssembler`, the adapter `XRepositoryImpl` and `XPersistenceConverter` for single-attribute value objects.
- **Application layer** exposes `XCommandService` and `XQueryService`; implementations go in `application/internal`. Command services return `Result<Aggregate, XCommandFailure>`.
- **Resources**: the request of a command is `<Command>Resource` (`RegisterBuildingResource`); the response is `<Aggregate>Resource`. We do not use the `DTO` suffix.
- **Assemblers** are named `<Target>From<Source>Assembler`, with methods `to<Target>From<Source>`.
- **Controllers** name each action after its command or query (`registerBuilding`, `getAlertById`). Business actions that are not a creation are exposed as plural sub-resources with `POST`, for example `POST /api/v1/alerts/{alertId}/acknowledgements`.
- **Messages**: keys follow `<aggregate>.error.<field>.<rule>` and must exist in `messages.properties` and `messages_es.properties`.
- **Before merging into `develop`**: merge `develop` into your branch, build and run the application.
- **Profiles**: `dev` is the default; `prod` reads every connection value from environment variables. Passwords are never committed.
