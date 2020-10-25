# using multistage docker build
# ref: https://docs.docker.com/develop/develop-images/multistage-build/

# temp container to build using gradle
#FROM gradle:6.7.0-jdk8 AS TEMP_BUILD_IMAGE
#ENV APP_HOME=/usr/app/
#WORKDIR $APP_HOME
#COPY build.gradle settings.gradle $APP_HOME
#
#COPY gradle $APP_HOME/gradle
#COPY --chown=gradle:gradle . /home/gradle/src
#USER root
#RUN chown -R gradle /home/gradle/src
#
#RUN gradle build || return 0
#COPY . .
#RUN gradle clean build
#
## actual container
#FROM adoptopenjdk/openjdk8:jdk8u265-b01-alpine-slim
#ENV APP_HOME=/usr/app/
#
#WORKDIR $APP_HOME
#COPY --from=TEMP_BUILD_IMAGE $APP_HOME/build/libs/*.jar app.jar
#
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/app.jar"]

FROM gradle:6.7.0-jdk8-hotspot AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon

FROM adoptopenjdk/openjdk8:jdk8u265-b01-alpine-slim

EXPOSE 8080

RUN mkdir /app

COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
ENTRYPOINT ["java","-jar","/app/spring-boot-application.jar"]



