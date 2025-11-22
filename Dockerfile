# Utiliser une image de construction Maven
FROM maven:3.8.1-openjdk-21 as builder

WORKDIR /build

# Copier les fichiers de configuration Maven
COPY pom.xml .

# Télécharger les dépendances
RUN mvn dependency:resolve

# Copier le code source
COPY src ./src

# Compiler l'application
RUN mvn clean package -DskipTests

# Utiliser une image Java légère pour l'exécution
FROM openjdk:21-slim

WORKDIR /app

# Copier le JAR compilé depuis l'étape de construction
COPY --from=builder /build/target/*.jar app.jar

# Exposer le port
EXPOSE 8080

# Variables d'environnement par défaut
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/plateforme_medicale
ENV SPRING_DATASOURCE_USERNAME=medical_user
ENV SPRING_DATASOURCE_PASSWORD=medical_password
ENV JWT_SECRET=MyVeryLongSecretKeyForJWTTokenGenerationThatIsAtLeast256bits
ENV JWT_EXPIRATION=86400000

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "app.jar"]

