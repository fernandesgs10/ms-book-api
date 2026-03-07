# Workflow Engine Example (Quarkus + Camunda)

This project demonstrates a simple workflow orchestration using:

* Quarkus REST API
* Camunda 8 / Zeebe
* BPMN process
* Job Workers
* Docker environment
* MySQL database

## Architecture

Client
↓
REST API (Quarkus)
↓
Workflow Engine (Zeebe)
↓
Workers
↓
External Services

## Technologies

* Java 17
* Quarkus
* Camunda 8
* Zeebe
* Docker
* MySQL

## Running the Project

Start infrastructure:

docker compose up -d

Run the application:

mvn quarkus:dev

Start workflow:

curl -X POST http://localhost:8080/workflow/start

## Example Workflow

Start Event
↓
Service Task (send-email)
↓
End Event
