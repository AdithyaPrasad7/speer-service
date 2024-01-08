FROM gradle:8.5-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM openjdk:17-jdk-alpine3.14
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/speer-service-0.0.1-SNAPSHOT.jar /app/application.jar
ENTRYPOINT ["java", "-jar", "/app/application.jar"]