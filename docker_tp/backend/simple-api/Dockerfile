# Étape 1 : Build (avec Maven)
FROM eclipse-temurin:21-jdk-alpine AS myapp-build
ENV MYAPP_HOME=/opt/myapp
WORKDIR $MYAPP_HOME

# Installer Maven
RUN apk add --no-cache maven

# Copier les fichiers du projet
COPY pom.xml .
COPY src ./src

# Compiler le projet et générer le .jar
RUN mvn package -DskipTests

# Étape 2 : Run (image légère sans Maven)
FROM eclipse-temurin:21-jre-alpine
ENV MYAPP_HOME=/opt/myapp
WORKDIR $MYAPP_HOME

# Copier le .jar depuis l'étape de build
COPY --from=myapp-build $MYAPP_HOME/target/*.jar $MYAPP_HOME/myapp.jar

# Commande de lancement
ENTRYPOINT ["java", "-jar", "myapp.jar"]
