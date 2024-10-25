# Use a base image for Java
FROM openjdk:21-jdk

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file to the container
COPY build/libs/crypto-recommendation-service-0.0.1-SNAPSHOT.jar /app/crypto-recommendation-service.jar
COPY /csv /app/csv

# Set the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "/app/crypto-recommendation-service.jar"]

# Expose the application port
EXPOSE 8080