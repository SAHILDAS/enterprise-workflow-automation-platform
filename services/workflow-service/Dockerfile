FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .
COPY services/workflow-service/pom.xml services/workflow-service/pom.xml

RUN mkdir -p services/workflow-service

COPY services/workflow-service services/workflow-service

RUN ./mvnw -pl services/workflow-service -am clean package -DskipTests || true

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /app/services/workflow-service/target/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","app.jar"]