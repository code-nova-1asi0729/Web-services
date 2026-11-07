# Vigilia Platform API

Backend of **Vigilia**, the preventive maintenance platform by CodeNova for critical equipment in residential buildings, monitored with IoT sensors.

This repository exposes the RESTful API consumed by the Vigilia frontend application.

## Tech stack

- Java 27
- Spring Boot 4.1.2 (Spring Web, Spring Data JPA, Validation)
- MySQL
- SpringDoc OpenAPI (Swagger UI)
- Lombok
- Maven

## Project structure

Organized by bounded context under `com.codenova.vigilia`, following Domain-Driven Design. Each bounded context has `domain`, `application`, `infrastructure` and `interfaces` layers.

| Package | Bounded context |
| --- | --- |
| `assetmonitoring` | Asset Monitoring |
| `incidents` | Incidents |
| `maintenance` | Maintenance |
| `notifications` | Notifications |
| `iam` | Identity and Access Management |
| `shared` | Shared kernel: naming strategy, result pattern and error handling |

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md).

## Team

CodeNova, 1ASI0729 Open Source Application Development, UPC, 2026-20.

| Member | GitHub |
| --- | --- |
| Arturo Fernando Valladolid Jiménez (Team Leader) | [@artuvall](https://github.com/artuvall) |
| Fernanda Ysabella Diaz Vargas | [@Fern9901](https://github.com/Fern9901) |
| Matthias Alonso Romero Veliz | [@AlonsoVelizUpc](https://github.com/AlonsoVelizUpc) |

## License

MIT. See [LICENSE.md](LICENSE.md).