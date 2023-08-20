FROM openjdk

# Copy local code to the container image.
WORKDIR /app

# Copy local code to the container image.
COPY chathub-0.0.1-SNAPSHOT.jar /app

# Change timezone
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
RUN echo "Asia/Shanghai" > /etc/timezone

# Run the web service on container startup.
CMD ["java","-jar","/app/chathub-0.0.1-SNAPSHOT.jar","--spring.profiles.active=prod"]
