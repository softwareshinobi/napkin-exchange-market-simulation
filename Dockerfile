FROM maven:3.8.7-openjdk-18-slim AS MAVEN_BUILD

MAINTAINER Software Shinobi "the.software.shinobi@gmail.com"

ARG JAR_FILE_NAME napkin-exchange-server-2.0.jar

WORKDIR /

COPY ./ ./

RUN mvn install

FROM eclipse-temurin:18-jre-alpine

COPY --from=MAVEN_BUILD /target/napkin-exchange-server-2.0.jar /napkin-exchange-server-2.0.jar

COPY --from=MAVEN_BUILD /src/main/resources/application.properties /application.properties

CMD ["java", "-jar", "/napkin-exchange-server-2.0.jar"] 
