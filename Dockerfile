FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q -DskipTests dependency:go-offline
COPY src ./src
RUN mvn -q -DskipTests package





FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 10001 feaster
COPY --from=build --chown=10001:10001 /app/target/feaster-api-0.1.0.jar app.jar
USER 10001
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
