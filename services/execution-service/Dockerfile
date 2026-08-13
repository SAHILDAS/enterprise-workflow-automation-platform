FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY services ./services

RUN mvn -pl services/execution-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre

RUN addgroup --system spring && adduser --system spring --ingroup spring

WORKDIR /app

COPY --from=builder /build/services/execution-service/target/*.jar app.jar

RUN chown -R spring:spring /app

USER spring

EXPOSE 8082

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]