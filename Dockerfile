# Step 1: Use official lightweight base image
FROM eclipse-temurin:17-jdk-alpine

# Step 2: Set working directory
WORKDIR /app

# Step 3: Copy the JAR into the container
COPY target/todo-list-0.0.1-SNAPSHOT.jar app.jar

# Step 4: Expose the application port
EXPOSE 8080

# Step 5: Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
