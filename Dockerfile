FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

COPY . .

RUN mvn clean install

FROM eclipse-temurin:17-jdk
WORKDIR /app

COPY --from=build /app/target/HubSpotIntegration-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080 5005
ENTRYPOINT ["java", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005", "-jar", "app.jar"]