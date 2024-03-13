FROM --platform=linux/amd64 maven:3-eclipse-temurin-20-alpine AS build
WORKDIR /app
COPY . .
RUN mvn clean install -DskipTests

FROM eclipse-temurin:20-jdk
WORKDIR /app

COPY --from=build /app/api/target/api-*.jar api.jar
COPY --from=build /app/db/target/db-*.jar db.jar
COPY --from=build /app/impl/target/impl-*.jar impl.jar

EXPOSE 8051
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "impl.jar"]