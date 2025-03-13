# Multistage build proccess

# Stage 1: Build the application
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the source code and build the application
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the final image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/ShelfMasterBE-0.0.1-SNAPSHOT.jar app.jar

# Copy the .env file into the container
COPY .env /config/.env

# Expose the port the app runs on
EXPOSE 8080

# Run the application
#ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["sh", "-c", "export $(grep -v '^#' /config/.env | xargs) && java -jar app.jar"]
#ENTRYPOINT ["sh", "-c", ". /config/.env && exec java -jar app.jar"]
ENTRYPOINT ["sh", "-c", "set -o allexport && source /config/.env && set +o allexport && exec java -jar app.jar"]

