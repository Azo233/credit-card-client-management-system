Servisi
Credit Card Client Management Service (8081)

CRUD operacije za klijente
REST API s H2 bazom podataka
Integracija s Card Validation Service

Card Validation Service (8082)

Validacija klijentskih podataka
Business logic za kartica requests
Kafka producer za status eventi

Tehnologije

Java 21 + Spring Boot 3.x
Spring Data JPA + H2 Database
Apache Kafka za messaging
Docker & Docker Compose
OpenAPI/Swagger za API dokumentaciju
Maven za dependency management

Pokretanje s Docker Compose 
bash# Clone repository
git clone <repo-url>
cd Credit-Card-Client-Management-System

# Pokreni sve servise (Kafka + mikroservisi)
docker-compose up --build

# Prati logove
docker-compose logs -f

Swagger UI: http://localhost:8081/swagger-ui.html
