# Fase de construcción con Maven y OpenJDK 21
FROM maven:3.9.4-eclipse-temurin-21 AS build
 
WORKDIR /app
 
COPY pom.xml .
COPY src ./src
 
RUN mvn clean package -DskipTests
 
FROM eclipse-temurin:21-jdk AS runtime
 
WORKDIR /app
 
COPY --from=build /app/target/pdf-reports-0.0.1-SNAPSHOT.jar /app/app.jar
COPY builder/wallet /app/wallet
 
# Placeholder de las variables de entorno para ser configuradas en tiempo de ejecución
ENV DB_URL=DB_URL
ENV DB_USERNAME=DB_USERNAME
ENV DB_PASSWORD=DB_PASSWORD
ENV SPRING_CONTEXT_PATH=SPRING_CONTEXT_PATH
ENV SPRING_PORT=SPRING_PORT
 
EXPOSE 8096
 
ENTRYPOINT ["java", "-jar", "/app/app.jar"]