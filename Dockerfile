FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache fontconfig freetype ttf-dejavu

COPY build/libs/ocpp-0.0.1-SNAPSHOT.jar app.jar

ENV PROFILES=local
ENV SERVER_PORT=8080

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=local", "-Dserver.port=8080", "-jar", "app.jar"]
