# Dockerfile for Vigilia Platform API
# Multi-stage build: Maven builds the jar, a JRE image runs it with the prod profile.

# Step 1: Build the application with Maven and Temurin JDK 27
FROM maven:3.10.0-eclipse-temurin-27 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Step 2: Runtime image
FROM eclipse-temurin:27-jre AS runtime
ENV SPRING_PROFILES_ACTIVE=prod
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Step 3: Run the application
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

# Environment variables required in the hosting provider:
# DATABASE_URL, DATABASE_PORT, DATABASE_NAME, DATABASE_USER, DATABASE_PASSWORD, PORT (optional)
