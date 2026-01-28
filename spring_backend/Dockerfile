# Set up the build environment using the Gradle image with JDK 17
FROM gradle:8.10.2-jdk17 AS build
# Copy the project files to the /home/gradle/src directory
COPY --chown=gradle:gradle . /home/gradle/src
# Set the working directory to /home/gradle/src
WORKDIR /home/gradle/src
# Build the Spring Boot application using Gradle
RUN gradle --no-daemon bootJar
# Create a new image with the OpenJDK 17 base image
FROM amazoncorretto:17
# Create a directory named /app
RUN mkdir /app
# Copy the built JAR file from the build stage to /app in the new image
COPY --from=build /home/gradle/src/build/libs/*.jar /app/spring-boot-application.jar
# Expose port 8080 for the Spring Boot application
EXPOSE 8080
# Run the Spring Boot application as a Java application with specified memory options
CMD ["java", "-jar", "-Xmx4g", "/app/spring-boot-application.jar"]
