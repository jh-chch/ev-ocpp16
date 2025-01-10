FROM openjdk:17-jdk-alpine

WORKDIR /app

RUN apk add --no-cache fontconfig freetype ttf-dejavu

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "-Dspring.profiles.active=prod", "-jar", "app.jar"]
