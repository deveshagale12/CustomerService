# Build stage
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY CustomerService/pom.xml .
COPY CustomerService/.mvn .mvn
COPY CustomerService/mvnw .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline -B

COPY CustomerService/src src

RUN ./mvnw clean package -DskipTests


# Runtime stage
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]