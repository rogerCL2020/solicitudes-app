
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew

COPY src src

RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jre

WORKDIR /app

RUN useradd --system --uid 10001 appuser

COPY --from=build /app/build/libs/*.jar app.jar

USER appuser

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
