FROM openjdk:21-jdk-slim

# Install curl for healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Make mvnw executable
RUN chmod +x ./mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build application
RUN ./mvnw package -DskipTests

# Create data directory for H2 database
RUN mkdir -p /app/data

# Run application
EXPOSE 8081
CMD ["java", "-jar", "target/Credit-Card-Client-Management-System-0.0.1-SNAPSHOT.jar"]