# Etapa 1: Build da aplicação (com JDK 21)
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /startup-udemy
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagem final (rodando com JDK 21)
FROM eclipse-temurin:21-jdk
WORKDIR /startup-udemy
COPY --from=build /startup-udemy/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
