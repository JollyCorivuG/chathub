FROM openjdk

# Copy local code to the container image.
WORKDIR /app

# Copy local code to the container image.
COPY chathub-0.0.1-SNAPSHOT.jar /app

# Run the web service on container startup.
CMD ["java","-jar","/app/chathub-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
