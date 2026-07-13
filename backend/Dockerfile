# --- Stage 1: build ---
# Uses a full JDK + Maven image, only needed to compile the jar.
# This entire layer is discarded from the final image.
FROM maven:3.9-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml first and download dependencies BEFORE copying source code.
# Docker caches layers - if pom.xml hasn't changed, this dependency download
# layer is reused on rebuilds instead of re-downloading everything, which is
# a real speed difference once your dependency list gets large.
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Now copy the actual source and build.
COPY src ./src
RUN mvn clean package -DskipTests -B

# --- Stage 2: runtime ---
# Minimal JRE image - no compiler, no Maven, no source code.
# This is what actually ships and runs in production.
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Run as a non-root user - a real security practice, not optional in
# production. Running containers as root is a common finding in
# security audits/Trivy scans, and you'll be adding Trivy scanning
# to this same pipeline in a later phase.
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

COPY --from=build /app/target/deploytracker-0.1.0.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
