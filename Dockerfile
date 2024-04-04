FROM maven:3.8.7-openjdk-18-slim AS mavenBuild

WORKDIR /

COPY . .

RUN mvn install -DskipTests

FROM eclipse-temurin:18-jre-alpine

COPY --from=mavenBuild /target/napkin-exchange-server-2.0.jar /napkin-exchange-server.jar

COPY --from=mavenBuild /src/main/resources/application.properties /application.properties

CMD ["java", "-jar", "/napkin-exchange-server.jar"] 
