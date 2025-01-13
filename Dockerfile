FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache fontconfig freetype ttf-dejavu

ARG JAR_FILE=build/libs/*.jar
ARG PROFILES
ARG PORT

COPY ${JAR_FILE} app.jar

ENV PROFILES=${PROFILES}
ENV SERVER_PORT=${PORT}

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=${PROFILES}", "-Dserver.port=${SERVER_PORT}", "-jar", "app.jar"]
