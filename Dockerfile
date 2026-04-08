FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY api-sample/build/libs/api-sample.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
