# Guía de Arturo · Sprint 3 · Jueves 5/11 (noche)

**Objetivo de hoy:** pasar `Building` e `Incident` al estilo learning-center (aggregate sin JPA + repositorio del dominio + adapter), sin cambiar el JSON ni las tablas, y dejarlo mergeado en `develop` para que Fernanda y Matthias trabajen sobre esa base.

## Estado real del repo (revisado hoy)

| Revisado | Resultado |
|---|---|
| Rama local actual | `feature/incidents` (ya mergeada en GitHub, PR #4) |
| `develop` local | **Atrasado**: está en `5f8f803`; GitHub está en `24ef5ce` |
| Archivos `.lock` en `.git` | Ninguno ✅ |
| Refactor a learning-center | **No empezado**: `Building` e `Incident` todavía tienen `@Entity`; los repositorios siguen en `infrastructure/.../repositories` |
| Ramas en GitHub | `main`, `develop`, `feature/asset-monitoring-equipment` (Fernanda). Matthias aún sin rama |

## Plan del día

| # | Tarea | Tiempo |
|---|---|---|
| 0 | Traer lo último y crear la rama | 5 min |
| 1 | Commit 1: clases base en `shared` | 15 min |
| 2 | Commit 2: contrato `BuildingRepository` del dominio | 10 min |
| 3 | Commit 3: persistence entity, repository y assembler de `Building` | 25 min |
| 4 | Commit 4: aggregate `Building` limpio + adapter + borrar repo viejo | 30 min |
| 5 | Verificar buildings (app, `.http`, MySQL) | 20 min |
| 6 | Commits 5 a 7: lo mismo para `Incident` | 50 min |
| 7 | Verificación final, PR y merge | 25 min |
| 8 | Mensaje al grupo | 5 min |
| | **Total** | **~3 h** |

> ⚠️ **No ejecutes la app entre el commit 3 y el 4** (ni entre el 6 y el 7): en ese momento hay dos clases `@Entity` mapeando la misma tabla. Compilar (Build → Build Project, ⌘F9) sí puedes y debes.

---

## Paso 0 · Traer lo último y crear la rama (5 min)

En la terminal de IntelliJ:

```bash
git checkout develop
git pull
git log --oneline -1          # debe mostrar 24ef5ce Merge pull request #4 ...
git flow feature start persistence-adapters
```

Opcional (limpieza): `git branch -d feature/incidents` (ya está mergeada).

---

## Paso 1 · Clases base en `shared` (15 min)

**Qué y por qué.** El profesor dijo que el aggregate no debe saber nada de JPA. Por eso separamos dos clases base: una del **dominio** (solo para registrar domain events más adelante) y otra de **infraestructura** que tiene el `id` y la auditoría (`createdAt`, `updatedAt`). Así `@CreatedDate` y `@LastModifiedDate` se mueven a la capa de persistencia.

**IntelliJ.** Clic derecho en `com.codenova.vigilia.shared` → New → Package → `domain.model.aggregates`. Luego clic derecho en el paquete nuevo → New → Java Class → `AbstractDomainAggregateRoot`. Repite con `shared.infrastructure.persistence.jpa` → New → Package → `entities` → clase `AuditableAbstractPersistenceEntity`.

`shared/domain/model/aggregates/AbstractDomainAggregateRoot.java`

```java
package com.codenova.vigilia.shared.domain.model.aggregates;

import org.springframework.data.domain.AbstractAggregateRoot;

/**
 * Base class for domain aggregate roots. It has no JPA annotations:
 * identity and auditing belong to the persistence entities.
 */
public abstract class AbstractDomainAggregateRoot<T extends AbstractDomainAggregateRoot<T>>
        extends AbstractAggregateRoot<T> {

    protected void registerDomainEvent(Object event) {
        super.registerEvent(event);
    }
}
```

`shared/infrastructure/persistence/jpa/entities/AuditableAbstractPersistenceEntity.java`

```java
package com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * Base JPA entity with id and audit columns for all persistence entities.
 */
@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableAbstractPersistenceEntity {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private Instant updatedAt;
}
```

> Usamos `Instant` (no `Date` como learning-center) porque ya es nuestra convención de fechas.

**Verificar:** ⌘F9 compila sin errores.

```bash
git add .
git commit -m "refactor(shared): add domain aggregate root and auditable persistence entity"
```

---

## Paso 2 · Contrato `BuildingRepository` del dominio (10 min)

**Qué y por qué.** El repositorio pasa a ser un **contrato del dominio**: una interfaz pura, sin Spring Data, que habla en términos de `Building`. La infraestructura la implementará (adapter). Un repositorio por aggregate.

**IntelliJ.** Clic derecho en `assetmonitoring.domain` → New → Package → `repositories` → New → Java Class → *Interface* → `BuildingRepository`. (El viejo `BuildingRepository` de `infrastructure` sigue existiendo hasta el paso 4; están en paquetes distintos, no choca.)

`assetmonitoring/domain/repositories/BuildingRepository.java`

```java
package com.codenova.vigilia.assetmonitoring.domain.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve buildings.
 */
public interface BuildingRepository {

    List<Building> findAll();

    Optional<Building> findById(Long id);

    boolean existsByCode(BuildingCode code);

    Building save(Building building);

    void deleteById(Long id);
}
```

```bash
git add .
git commit -m "refactor(buildings): add building domain repository contract"
```

---

## Paso 3 · Persistence entity, repository y assembler (25 min)

**Qué y por qué.** `BuildingPersistenceEntity` es la clase que sí conoce JPA y mapea la tabla `buildings` (mismas columnas que hoy). `BuildingPersistenceRepository` es el Spring Data de esa entidad. `BuildingPersistenceAssembler` traduce entre el aggregate y la entidad. Para pasar de la entidad al aggregate se usa `Building.reconstitute(...)`: un método que **rearma** un edificio que ya existe, sin pasar por las reglas de creación (por ejemplo, sin forzar `status = ACTIVE`).

> El `@Table(name = "buildings")` es obligatorio: sin él, el naming strategy generaría `building_persistence_entities`.

**3.1 · Agregar `reconstitute` a `Building`.** Por ahora el aggregate todavía tiene JPA; solo agrega este método al final de la clase (antes de la última `}`), para que el assembler compile:

```java
    public static Building reconstitute(Long id, String name, BuildingCode code, String address,
                                        String district, Integer totalUnits, BuildingStatus status) {
        var building = new Building();
        building.id = id;
        building.name = name;
        building.code = code;
        building.address = address;
        building.district = district;
        building.totalUnits = totalUnits;
        building.status = status;
        return building;
    }
```

**3.2 · IntelliJ.** En `assetmonitoring.infrastructure.persistence.jpa` crea los paquetes `entities` y `assemblers`. El paquete `repositories` ya existe.

`assetmonitoring/infrastructure/persistence/jpa/entities/BuildingPersistenceEntity.java`

```java
package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters.BuildingCodeAttributeConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * JPA entity that maps the buildings table.
 */
@Entity
@Table(name = "buildings")
@Getter
@Setter
@NoArgsConstructor
public class BuildingPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @Convert(converter = BuildingCodeAttributeConverter.class)
    private BuildingCode code;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String district;

    @Column(nullable = false)
    private Integer totalUnits;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private BuildingStatus status;
}
```

> En el paso 4 renombras el converter con Shift+F6 y este import se actualiza solo.

`assetmonitoring/infrastructure/persistence/jpa/repositories/BuildingPersistenceRepository.java`

```java
package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.BuildingPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data repository for building persistence entities.
 */
@Repository
public interface BuildingPersistenceRepository extends JpaRepository<BuildingPersistenceEntity, Long> {
    boolean existsByCode(BuildingCode code);
}
```

`assetmonitoring/infrastructure/persistence/jpa/assemblers/BuildingPersistenceAssembler.java`

```java
package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.entities.BuildingPersistenceEntity;

/**
 * Converts between the Building aggregate and its persistence entity.
 */
public final class BuildingPersistenceAssembler {

    private BuildingPersistenceAssembler() {
    }

    public static Building toDomainFromPersistence(BuildingPersistenceEntity entity) {
        return Building.reconstitute(
                entity.getId(),
                entity.getName(),
                entity.getCode(),
                entity.getAddress(),
                entity.getDistrict(),
                entity.getTotalUnits(),
                entity.getStatus());
    }

    public static BuildingPersistenceEntity toPersistenceFromDomain(Building building) {
        var entity = new BuildingPersistenceEntity();
        // A new building has no id yet, so JPA generates it
        if (building.getId() != null) entity.setId(building.getId());
        entity.setName(building.getName());
        entity.setCode(building.getCode());
        entity.setAddress(building.getAddress());
        entity.setDistrict(building.getDistrict());
        entity.setTotalUnits(building.getTotalUnits());
        entity.setStatus(building.getStatus());
        return entity;
    }
}
```

**Verificar:** ⌘F9 compila. **No ejecutes la app.**

```bash
git add .
git commit -m "refactor(buildings): add building persistence entity, repository and assembler"
```

---

## Paso 4 · Desacoplar `Building` de JPA con el adapter (30 min)

**Qué y por qué.** Ahora el aggregate queda como Java puro. `BuildingRepositoryImpl` (el **adapter**) implementa el contrato del dominio usando el Spring Data de la persistence entity y el assembler. Los services solo cambian el `import`.

Hazlo en este orden:

**4.1 · Renombrar el converter.** En el árbol del proyecto, clic en `BuildingCodeAttributeConverter` → **Shift+F6** → `BuildingCodePersistenceConverter` → Refactor. IntelliJ actualiza el import en `Building` y en `BuildingPersistenceEntity`. Queda así:

```java
package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.converters;

import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
/**
 * Persists BuildingCode as a plain string column.
 */
@Converter
public class BuildingCodePersistenceConverter implements AttributeConverter<BuildingCode, String> {
    @Override
    public String convertToDatabaseColumn(BuildingCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public BuildingCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new BuildingCode(dbData);
    }
}
```

**4.2 · Reemplazar `Building.java` completo:**

```java
package com.codenova.vigilia.assetmonitoring.domain.model.aggregates;

import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingStatus;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

/**
 * Residential building whose critical equipment Vigilia monitors.
 */
@Getter
public class Building extends AbstractDomainAggregateRoot<Building> {

    private Long id;
    private String name;
    private BuildingCode code;
    private String address;
    private String district;
    private Integer totalUnits;
    private BuildingStatus status;

    private Building() {}

    public Building(CreateBuildingCommand command) {
        this.name = command.name();
        this.code = command.code();
        this.address = command.address();
        this.district = command.district();
        this.totalUnits = command.totalUnits();
        this.status = BuildingStatus.ACTIVE;
    }

    public static Building reconstitute(Long id, String name, BuildingCode code, String address,
                                        String district, Integer totalUnits, BuildingStatus status) {
        var building = new Building();
        building.id = id;
        building.name = name;
        building.code = code;
        building.address = address;
        building.district = district;
        building.totalUnits = totalUnits;
        building.status = status;
        return building;
    }

    public void update(UpdateBuildingCommand command) {
        this.name = command.name();
        this.address = command.address();
        this.district = command.district();
        this.totalUnits = command.totalUnits();
        this.status = command.status();
    }
}
```

**4.3 · Crear el adapter.** En `assetmonitoring.infrastructure.persistence.jpa` → New → Package → `adapters` → clase `BuildingRepositoryImpl`.

`assetmonitoring/infrastructure/persistence/jpa/adapters/BuildingRepositoryImpl.java`

```java
package com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.valueobjects.BuildingCode;
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.assemblers.BuildingPersistenceAssembler;
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.BuildingPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the building domain repository with Spring Data JPA.
 */
@Repository
public class BuildingRepositoryImpl implements BuildingRepository {

    private final BuildingPersistenceRepository buildingPersistenceRepository;

    public BuildingRepositoryImpl(BuildingPersistenceRepository buildingPersistenceRepository) {
        this.buildingPersistenceRepository = buildingPersistenceRepository;
    }

    @Override
    public List<Building> findAll() {
        return buildingPersistenceRepository.findAll().stream()
                .map(BuildingPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Building> findById(Long id) {
        return buildingPersistenceRepository.findById(id)
                .map(BuildingPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public boolean existsByCode(BuildingCode code) {
        return buildingPersistenceRepository.existsByCode(code);
    }

    @Override
    public Building save(Building building) {
        var saved = buildingPersistenceRepository.save(BuildingPersistenceAssembler.toPersistenceFromDomain(building));
        return BuildingPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public void deleteById(Long id) {
        buildingPersistenceRepository.deleteById(id);
    }
}
```

**4.4 · Actualizar los services.**

En `BuildingQueryServiceImpl` solo cambia **un import**:

```java
// antes
import com.codenova.vigilia.assetmonitoring.infrastructure.persistence.jpa.repositories.BuildingRepository;
// ahora
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
```

En `BuildingCommandServiceImpl` cambia el mismo import y **el delete** (el contrato del dominio tiene `deleteById`, no `delete`). Archivo completo:

```java
package com.codenova.vigilia.assetmonitoring.application.internal.commandservices;

import com.codenova.vigilia.assetmonitoring.application.commandservices.BuildingCommandFailure;
import com.codenova.vigilia.assetmonitoring.application.commandservices.BuildingCommandService;
import com.codenova.vigilia.assetmonitoring.domain.model.aggregates.Building;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.CreateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.DeleteBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.model.commands.UpdateBuildingCommand;
import com.codenova.vigilia.assetmonitoring.domain.repositories.BuildingRepository;
import com.codenova.vigilia.shared.application.patterns.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
/**
 * Default implementation of BuildingCommandService.
 */
@Slf4j
@Service
public class BuildingCommandServiceImpl implements BuildingCommandService {
    private final BuildingRepository buildingRepository;

    public BuildingCommandServiceImpl(BuildingRepository buildingRepository) {
        this.buildingRepository = buildingRepository;
    }

    @Override
    @Transactional
    public Result<Building, BuildingCommandFailure> handle(CreateBuildingCommand command) {
        if (buildingRepository.existsByCode(command.code())) {
            log.warn("Duplicate building code: {}", command.code().value());
            return Result.failure(new BuildingCommandFailure.DuplicateCode());
        }
        try {
            var building = buildingRepository.save(new Building(command));
            log.info("Building created: id={}, code={}", building.getId(), building.getCode().value());
            return Result.success(building);
        } catch (DataIntegrityViolationException exception) {
            log.warn("Duplicate building code: {}", command.code().value());
            return Result.failure(new BuildingCommandFailure.DuplicateCode());
        }
    }

    @Override
    @Transactional
    public Result<Building, BuildingCommandFailure> handle(UpdateBuildingCommand command) {
        var building = buildingRepository.findById(command.buildingId());
        if (building.isEmpty()) {
            log.warn("Building not found: id={}", command.buildingId());
            return Result.failure(new BuildingCommandFailure.NotFound());
        }
        building.get().update(command);
        var updated = buildingRepository.save(building.get());
        log.info("Building updated: id={}", updated.getId());
        return Result.success(updated);
    }

    @Override
    @Transactional
    public Result<Building, BuildingCommandFailure> handle(DeleteBuildingCommand command) {
        var building = buildingRepository.findById(command.buildingId());
        if (building.isEmpty()) {
            log.warn("Building not found: id={}", command.buildingId());
            return Result.failure(new BuildingCommandFailure.NotFound());
        }
        buildingRepository.deleteById(command.buildingId());
        log.info("Building deleted: id={}", command.buildingId());
        return Result.success(building.get());
    }
}
```

> El `save(...)` después de `update(...)` ahora es **obligatorio**: el aggregate ya no lo vigila JPA, así que si no se guarda, el cambio se pierde. Ya lo tenías.

**4.5 · Borrar el repositorio viejo.** Clic derecho en `infrastructure/persistence/jpa/repositories/BuildingRepository.java` → Delete → *Safe delete* (no debe encontrar usos). **Es imprescindible** borrarlo en este commit: Spring Data trata cualquier clase `<NombreDeRepositorio>Impl` como implementación extra de ese repositorio, y `BuildingRepositoryImpl` chocaría con el viejo `BuildingRepository` de Spring Data.

**Verificar:** ⌘F9 compila. Revisa que en `Building.java` ya no haya ningún import de `jakarta.persistence` ni de `org.springframework.data.annotation`.

```bash
git add -A
git commit -m "refactor(buildings): decouple building aggregate from jpa through repository adapter"
```

> `git add -A` (no `git add .`) para que el archivo borrado y el renombrado entren al commit. Revisa con `git status` antes.

---

## Paso 5 · Verificar buildings (20 min)

1. **MySQL antes de correr** (para comparar). En tu cliente MySQL:
   ```sql
   USE vigilia;
   DESCRIBE buildings;
   SHOW INDEX FROM buildings;
   ```
   Toma una captura o copia el resultado.
2. Ejecuta `VigiliaApplication` (con `DATABASE_USER` y `DATABASE_PASSWORD`). En el log **no** debe aparecer `create table building_persistence_entities`. Si aparece un `alter table ... add constraint` para el `code`, no pasa nada.
3. Abre `http/buildings.http` y corre las peticiones de **lectura** primero (GET todos, GET por id, 404). Después un PUT y un DELETE de un edificio de prueba. Los POST de LP-01/MA-02 deben dar **409** si ya existen (eso confirma que `existsByCode` funciona).
4. Vuelve a MySQL:
   ```sql
   SHOW TABLES;            -- no debe haber tablas nuevas
   DESCRIBE buildings;     -- igual que antes
   SELECT id, code, created_at, updated_at FROM buildings;
   ```
   Tras el PUT, `updated_at` cambia y `created_at` **no** (porque la columna es `updatable = false`).
5. Swagger: http://localhost:8080/swagger-ui/index.html → los mismos 5 endpoints de Buildings.

Si todo va bien, detén la app.

---

## Paso 6 · `Incident`, commits 5 a 7 (50 min)

Mismo patrón. No vuelvas a tocar `shared`.

### Commit 5 · Contrato del dominio

`incidents/domain/repositories/IncidentRepository.java` (paquete nuevo `incidents.domain.repositories`)

```java
package com.codenova.vigilia.incidents.domain.repositories;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;

import java.util.List;
import java.util.Optional;

/**
 * Domain contract to store and retrieve incidents.
 */
public interface IncidentRepository {

    List<Incident> findAll();

    Optional<Incident> findById(Long id);

    List<Incident> findAllByBuildingId(Long buildingId);

    Incident save(Incident incident);
}
```

```bash
git add .
git commit -m "refactor(incidents): add incident domain repository contract"
```

### Commit 6 · Persistence entity, repository y assembler

**6.1 · Agrega `reconstitute` al final de `Incident.java`** (todavía con JPA):

```java
    public static Incident reconstitute(Long id, Long buildingId, Long residentId, Long equipmentId,
                                        IncidentCategory category, String description, IncidentStatus status,
                                        RatingScore ratingScore, String ratingComment, Instant reportedAt,
                                        Instant startedAt, Instant resolvedAt, Instant ratedAt) {
        var incident = new Incident();
        incident.id = id;
        incident.buildingId = buildingId;
        incident.residentId = residentId;
        incident.equipmentId = equipmentId;
        incident.category = category;
        incident.description = description;
        incident.status = status;
        incident.ratingScore = ratingScore;
        incident.ratingComment = ratingComment;
        incident.reportedAt = reportedAt;
        incident.startedAt = startedAt;
        incident.resolvedAt = resolvedAt;
        incident.ratedAt = ratedAt;
        return incident;
    }
```

**6.2 · Crea los paquetes** `entities` y `assemblers` en `incidents.infrastructure.persistence.jpa`.

`incidents/infrastructure/persistence/jpa/entities/IncidentPersistenceEntity.java`

```java
package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.entities;

import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.converters.RatingScoreAttributeConverter;
import com.codenova.vigilia.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that maps the incidents table.
 */
@Entity
@Table(name = "incidents")
@Getter
@Setter
@NoArgsConstructor
public class IncidentPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false)
    private Long buildingId;

    @Column(nullable = false)
    private Long residentId;

    private Long equipmentId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentCategory category;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IncidentStatus status;

    @Convert(converter = RatingScoreAttributeConverter.class)
    private RatingScore ratingScore;

    @Column(length = 300)
    private String ratingComment;

    @Column(nullable = false)
    private Instant reportedAt;

    private Instant startedAt;

    private Instant resolvedAt;

    private Instant ratedAt;
}
```

`incidents/infrastructure/persistence/jpa/repositories/IncidentPersistenceRepository.java`

```java
package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.repositories;

import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data repository for incident persistence entities.
 */
@Repository
public interface IncidentPersistenceRepository extends JpaRepository<IncidentPersistenceEntity, Long> {
    List<IncidentPersistenceEntity> findAllByBuildingId(Long buildingId);
}
```

`incidents/infrastructure/persistence/jpa/assemblers/IncidentPersistenceAssembler.java`

```java
package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.assemblers;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.entities.IncidentPersistenceEntity;

/**
 * Converts between the Incident aggregate and its persistence entity.
 */
public final class IncidentPersistenceAssembler {

    private IncidentPersistenceAssembler() {
    }

    public static Incident toDomainFromPersistence(IncidentPersistenceEntity entity) {
        return Incident.reconstitute(
                entity.getId(),
                entity.getBuildingId(),
                entity.getResidentId(),
                entity.getEquipmentId(),
                entity.getCategory(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getRatingScore(),
                entity.getRatingComment(),
                entity.getReportedAt(),
                entity.getStartedAt(),
                entity.getResolvedAt(),
                entity.getRatedAt());
    }

    public static IncidentPersistenceEntity toPersistenceFromDomain(Incident incident) {
        var entity = new IncidentPersistenceEntity();
        // A new incident has no id yet, so JPA generates it
        if (incident.getId() != null) entity.setId(incident.getId());
        entity.setBuildingId(incident.getBuildingId());
        entity.setResidentId(incident.getResidentId());
        entity.setEquipmentId(incident.getEquipmentId());
        entity.setCategory(incident.getCategory());
        entity.setDescription(incident.getDescription());
        entity.setStatus(incident.getStatus());
        entity.setRatingScore(incident.getRatingScore());
        entity.setRatingComment(incident.getRatingComment());
        entity.setReportedAt(incident.getReportedAt());
        entity.setStartedAt(incident.getStartedAt());
        entity.setResolvedAt(incident.getResolvedAt());
        entity.setRatedAt(incident.getRatedAt());
        return entity;
    }
}
```

**Verificar:** ⌘F9. **No ejecutes la app.**

```bash
git add .
git commit -m "refactor(incidents): add incident persistence entity, repository and assembler"
```

### Commit 7 · Desacoplar `Incident` de JPA

**7.1 · Renombrar el converter:** `RatingScoreAttributeConverter` → Shift+F6 → `RatingScorePersistenceConverter`. Solo cambia el nombre de la clase; el contenido sigue igual.

**7.2 · Reemplazar `Incident.java` completo:**

```java
package com.codenova.vigilia.incidents.domain.model.aggregates;

import com.codenova.vigilia.incidents.domain.model.commands.ReportIncidentCommand;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentCategory;
import com.codenova.vigilia.incidents.domain.model.valueobjects.IncidentStatus;
import com.codenova.vigilia.incidents.domain.model.valueobjects.RatingScore;
import com.codenova.vigilia.shared.domain.model.aggregates.AbstractDomainAggregateRoot;
import lombok.Getter;

import java.time.Instant;
/**
 * Problem reported by a resident and followed until it is resolved and rated
 */
@Getter
public class Incident extends AbstractDomainAggregateRoot<Incident> {

    private Long id;
    private Long buildingId;
    private Long residentId;
    private Long equipmentId;
    private IncidentCategory category;
    private String description;
    private IncidentStatus status;
    private RatingScore ratingScore;
    private String ratingComment;
    private Instant reportedAt;
    private Instant startedAt;
    private Instant resolvedAt;
    private Instant ratedAt;

    private Incident() {}

    public Incident(ReportIncidentCommand command) {
        this.buildingId = command.buildingId();
        this.residentId = command.residentId();
        this.equipmentId = command.equipmentId();
        this.category = command.category();
        this.description = command.description();
        this.status = IncidentStatus.REPORTED;
        this.reportedAt = Instant.now();
    }

    public static Incident reconstitute(Long id, Long buildingId, Long residentId, Long equipmentId,
                                        IncidentCategory category, String description, IncidentStatus status,
                                        RatingScore ratingScore, String ratingComment, Instant reportedAt,
                                        Instant startedAt, Instant resolvedAt, Instant ratedAt) {
        var incident = new Incident();
        incident.id = id;
        incident.buildingId = buildingId;
        incident.residentId = residentId;
        incident.equipmentId = equipmentId;
        incident.category = category;
        incident.description = description;
        incident.status = status;
        incident.ratingScore = ratingScore;
        incident.ratingComment = ratingComment;
        incident.reportedAt = reportedAt;
        incident.startedAt = startedAt;
        incident.resolvedAt = resolvedAt;
        incident.ratedAt = ratedAt;
        return incident;
    }

    public boolean start() {
        if (status != IncidentStatus.REPORTED) return false;
        this.status = IncidentStatus.IN_PROGRESS;
        this.startedAt = Instant.now();
        return true;
    }

    public boolean resolve() {
        if (status == IncidentStatus.RESOLVED) return false;
        this.status = IncidentStatus.RESOLVED;
        this.resolvedAt = Instant.now();
        return true;
    }

    public boolean changeStatus(IncidentStatus newStatus) {
        if (newStatus == status) return true;
        return switch (newStatus) {
            case REPORTED -> false;
            case IN_PROGRESS -> start();
            case RESOLVED -> resolve();
        };
    }

    public boolean rate(RatingScore score, String comment) {
        if (status != IncidentStatus.RESOLVED || isRated()) return false;
        this.ratingScore = score;
        this.ratingComment = comment;
        this.ratedAt = Instant.now();
        return true;
    }

    public boolean isRated() {
        return ratingScore != null;
    }
}
```

**7.3 · Adapter** (paquete nuevo `incidents.infrastructure.persistence.jpa.adapters`):

`incidents/infrastructure/persistence/jpa/adapters/IncidentRepositoryImpl.java`

```java
package com.codenova.vigilia.incidents.infrastructure.persistence.jpa.adapters;

import com.codenova.vigilia.incidents.domain.model.aggregates.Incident;
import com.codenova.vigilia.incidents.domain.repositories.IncidentRepository;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.assemblers.IncidentPersistenceAssembler;
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.repositories.IncidentPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements the incident domain repository with Spring Data JPA.
 */
@Repository
public class IncidentRepositoryImpl implements IncidentRepository {

    private final IncidentPersistenceRepository incidentPersistenceRepository;

    public IncidentRepositoryImpl(IncidentPersistenceRepository incidentPersistenceRepository) {
        this.incidentPersistenceRepository = incidentPersistenceRepository;
    }

    @Override
    public List<Incident> findAll() {
        return incidentPersistenceRepository.findAll().stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Optional<Incident> findById(Long id) {
        return incidentPersistenceRepository.findById(id)
                .map(IncidentPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Incident> findAllByBuildingId(Long buildingId) {
        return incidentPersistenceRepository.findAllByBuildingId(buildingId).stream()
                .map(IncidentPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Incident save(Incident incident) {
        var saved = incidentPersistenceRepository.save(IncidentPersistenceAssembler.toPersistenceFromDomain(incident));
        return IncidentPersistenceAssembler.toDomainFromPersistence(saved);
    }
}
```

**7.4 · Services.** En `IncidentCommandServiceImpl` y en `IncidentQueryServiceImpl` cambia **solo el import** (los métodos que usan ya existen en el contrato):

```java
// antes
import com.codenova.vigilia.incidents.infrastructure.persistence.jpa.repositories.IncidentRepository;
// ahora
import com.codenova.vigilia.incidents.domain.repositories.IncidentRepository;
```

La validación previa de la calificación en `UpdateIncident` (antes de `changeStatus`) se queda como está.

**7.5 · Borrar** `incidents/infrastructure/persistence/jpa/repositories/IncidentRepository.java` con *Safe delete* (mismo motivo que en 4.5).

**Verificar:** ⌘F9. Además corre los tests de dominio: clic derecho en `src/test/java/.../incidents` → **Run 'Tests in incidents'**. `IncidentTest` y `RatingScoreTest` deben salir en verde **sin cambios** (usan el constructor del aggregate, no JPA).

```bash
git add -A
git commit -m "refactor(incidents): decouple incident aggregate from jpa through repository adapter"
```

---

## Paso 7 · Verificación final, PR y merge (25 min)

1. Comprobar que ya no queda JPA en el dominio. En la terminal:
   ```bash
   grep -rn "jakarta.persistence" src/main/java/com/codenova/vigilia/*/domain
   ```
   Debe salir **vacío**.
2. Ejecuta la app. Corre **completos** `http/buildings.http` y `http/incidents.http` (POST, GET, `?buildingId`, PUT con cambio de estado, el 409 de transición inválida y la calificación).
3. MySQL:
   ```sql
   SHOW TABLES;            -- solo buildings, incidents (sin *_persistence_entities)
   DESCRIBE buildings;
   DESCRIBE incidents;     -- mismas columnas que antes, incluidas created_at y updated_at
   ```
4. Swagger: mismos endpoints en Buildings e Incidents.
5. Publicar y abrir PR:
   ```bash
   git flow feature publish persistence-adapters
   ```
   En GitHub → *Compare & pull request* → **cambia la base a `develop`** (GitHub propone `main`).
   - Título: `refactor: decouple aggregates from jpa through repository adapters`
   - Descripción sugerida:
     > Applies the learning-center convention shown in class (5/11): domain aggregates without JPA annotations, domain repository contracts in `domain/repositories`, and persistence entities + assemblers + repository adapters in `infrastructure/persistence/jpa`. JSON contract and table names are unchanged. Verified with `buildings.http`, `incidents.http`, Swagger and `DESCRIBE` in MySQL.
   - Reviewer: Fernanda o Matthias.
6. Cuando lo aprueben → *Merge* → luego en local:
   ```bash
   git checkout develop
   git pull
   git branch -d feature/persistence-adapters
   ```
   Si nadie revisa esta noche, déjalo abierto y mergea mañana a primera hora, **antes** de crear `feature/asset-monitoring-alerts`.

---

## Paso 8 · Mensaje al grupo (5 min)

Envíalo cuando el PR esté mergeado (o abierto, si lo van a revisar ellos):

> En la clase del jueves el profe dijo que, para su proyecto, el repositorio tiene que ser un contrato del dominio y que el aggregate no debe llevar anotaciones JPA (learning-center-platform). Ya pasé `Building` e `Incident` a ese estilo en `develop`: miren la carpeta `assetmonitoring` (`domain/repositories`, `infrastructure/persistence/jpa/entities`, `adapters` y `assemblers`). El JSON y las tablas no cambian.
>
> 1. **Fernanda:** haz `git checkout develop && git pull`, luego `git checkout feature/asset-monitoring-equipment && git merge develop`. Pasa `CriticalEquipment` al mismo patrón que `Building` antes de seguir con resources y controller.
> 2. **Matthias:** crea `feature/asset-monitoring-sensors` desde `develop` actualizado y sigue el patrón de `Building` desde el inicio. Usa el `MetricType` que ya está en `develop`.
> 3. **Los dos:** necesito sus PR a `develop` a más tardar el sábado a mediodía para desplegar. Cualquier duda, me escriben.

---

## Preguntas para la clase (repásalas mientras corre la app)

- **¿Por qué el dominio no tiene `@Entity`?** Porque el mapeo objeto-relacional es un detalle de infraestructura; el dominio solo expresa reglas de negocio y no debe depender de JPA.
- **¿Qué hace el adapter (`BuildingRepositoryImpl`)?** Implementa el contrato del dominio usando Spring Data, y con el assembler traduce entre aggregate y persistence entity.
- **¿Para qué sirve `reconstitute`?** Para rearmar un aggregate que ya existe en la base de datos, sin aplicar las reglas de creación (por ejemplo, no reinicia el estado ni la fecha de reporte).
- **¿Por qué dos `Repository`?** El del dominio es el contrato (qué necesita el negocio); el de persistencia es la herramienta técnica (Spring Data sobre la tabla).
- **¿Por qué uno por aggregate?** Porque el aggregate es la unidad de consistencia: se guarda y se carga completo, aunque use varias tablas.
- **¿Por qué la persistence entity lleva `@Table`?** Porque sin ella el naming strategy generaría `building_persistence_entities`.

---

## Antes de cerrar el día

- [ ] 7 commits en `feature/persistence-adapters`, con los mensajes exactos
- [ ] `grep jakarta.persistence` en `*/domain` sale vacío
- [ ] `buildings.http` e `incidents.http` funcionan completos
- [ ] `SHOW TABLES` y `DESCRIBE` iguales que antes, sin tablas nuevas
- [ ] `IncidentTest` y `RatingScoreTest` en verde
- [ ] PR hacia **`develop`** (no `main`), con reviewer asignado
- [ ] Mensaje enviado al grupo
- [ ] `find .git -name '*.lock'` vacío

**Mañana (vie 6/11):** `Alert` completo en el estilo nuevo (rama `feature/asset-monitoring-alerts`, desde `develop` ya con este refactor), perfiles dev/prod + Dockerfile, environments del frontend e intercambiar a Fernanda y Matthias en 5.2.3.2 y 5.2.3.3.
