FROM eclipse-temurin:22-jdk-alpine
WORKDIR app/
COPY . .
RUN  ./mvnw  clean  package  -DskipTests
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "target/backend-event.jar"]

## Étape 1 : Construire l'application Spring Boot avec Maven
#FROM maven:3.9.6-eclipse-temurin-21-alpine AS builder
#WORKDIR /app
#COPY pom.xml .
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Étape 2 : Créer une image Docker avec l'application construite
#FROM eclipse-temurin:22-jdk-alpine
#
## Copier le fichier JAR généré dans l'étape précédente dans le conteneur
#COPY --from=builder /app/target/backend-event.jar /app/backend-event.jar
#
## Définir le port sur lequel l'application Spring Boot écoutera (par défaut : 8080)
#EXPOSE 8080
#
## Créer un utilisateur non privilégié pour exécuter l'application
#RUN adduser -D user
#USER user
#
## Commande pour exécuter l'application lors du démarrage du conteneur
#CMD ["java", "-jar", "target/backend-event.jar"]