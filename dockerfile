# --- Stage 1: Build the application with Maven ---
FROM maven:3.8-openjdk-11 AS build

WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src

# --- NEW DEBUGGING STEP ---
# This command searches for the critical line of code.
# If the line is NOT found, grep will exit with an error, and the build will FAIL.
# This is a definitive test to see if the correct code is being used.
RUN grep "SubstitutingSourceProvider" /app/src/main/java/com/udbhav/sherlock/SherlockApplication.java
# --- END OF DEBUGGING STEP ---

RUN mvn package -DskipTests

# --- Stage 2: Create the final, lightweight image ---
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/sherlock-1.0-SNAPSHOT.jar /app/app.jar
COPY config.yml .
EXPOSE 8080
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar", "server", "config.yml"]
