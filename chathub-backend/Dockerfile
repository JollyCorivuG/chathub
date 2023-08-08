FROM openjdk:17-slim

# 安装 Maven
RUN apt-get update && apt-get install -y maven

# Copy local code to the container image.
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Build a release artifact.
RUN mvn package -DskipTests

# Run the web service on container startup.
CMD ["java","-jar","/app/target/chathub-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
