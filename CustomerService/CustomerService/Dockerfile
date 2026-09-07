# =========================
# Build Stage
# =========================
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY mvnw .

# Make Maven wrapper executable
RUN chmod +x mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy source code
COPY src src

# Build application
RUN ./mvnw clean package -DskipTests


# =========================
# Runtime Stage
# =========================
FROM eclipse-temurin:21-jre

WORKDIR /app

# Copy Spring Boot JAR
COPY --from=build /app/target/*.jar app.jar

# Application port
EXPOSE 8081

# Start application
ENTRYPOINT ["java", "-jar", "app.jar"]