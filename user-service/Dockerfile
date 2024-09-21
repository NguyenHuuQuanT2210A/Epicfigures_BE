# Use a base image for Java
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/user-service-0.0.1-SNAPSHOT.jar /app/myapp.jar

# Expose the application port
EXPOSE 8081

# Command to run the application
ENTRYPOINT ["java", "-jar", "myapp.jar"]
