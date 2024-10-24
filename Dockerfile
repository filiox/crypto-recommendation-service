# Use a base image for Java
FROM openjdk:21-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file to the container
COPY target/recommendation-service.jar /app/recommendation-service.jar

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/recommendation-service.jar"]

# Expose the application port
EXPOSE 8080