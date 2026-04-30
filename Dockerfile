# =========================
# Build stage
# =========================
FROM maven:3.9.9-eclipse-temurin-21-alpine AS build

WORKDIR /app

ENV MAVEN_OPTS="-Dmaven.wagon.http.retryHandler.count=5 \
-Dmaven.wagon.http.connectionTimeout=60000 \
-Dmaven.wagon.http.readTimeout=60000"

COPY pom.xml .
COPY src ./src

RUN mvn -B -ntp clean package -DskipTests

# =========================
# Runtime stage
# =========================
FROM bellsoft/liberica-openjre-alpine:21

WORKDIR /app

COPY --from=build /app/target/*-runner.jar /app/app.jar

EXPOSE 8080

# Logs mais visíveis
ENTRYPOINT ["java", "-Dquarkus.log.level=DEBUG", "-jar", "/app/app.jar"]