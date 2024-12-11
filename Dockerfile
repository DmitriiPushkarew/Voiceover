FROM openjdk:21-jdk-slim

WORKDIR /app

COPY target/Voiceover-1.0-SNAPSHOT.jar app.jar

# Проверка содержимого папки /app
RUN ls -l /app
RUN jar tf /app/app.jar

COPY src/main/resources/application.yml /app/application.yml

EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=/app/application.yml"]
