FROM gradle:7-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
ENV JWT_SECRET=password

RUN gradle build --no-daemon

FROM openjdk:17-alpine
EXPOSE 8080
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*SNAPSHOT.jar /app/kanban.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "kanban.jar"]