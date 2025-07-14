# Use OpenJDK 11 base image
FROM openjdk:11-jre-slim

# Set the working directory
WORKDIR /app

# Install necessary packages
RUN apt-get update && apt-get install -y \
    wget \
    curl \
    && rm -rf /var/lib/apt/lists/*

# Copy all JAR dependencies
COPY lib/*.jar /app/lib/

# Copy application JAR (fat jar with dependencies)
COPY target/vacuandes-1.0.0-jar-with-dependencies.jar /app/app.jar

# Copy resources
COPY src/main/resources/ /app/resources/

# Set environment variables with defaults
ENV DB_HOST=${DB_HOST:-localhost}
ENV DB_PORT=${DB_PORT:-1521}
ENV DB_NAME=${DB_NAME:-vacuandes}
ENV DB_USERNAME=${DB_USERNAME:-vacuandes_user}
ENV DB_PASSWORD=${DB_PASSWORD:-password}
ENV SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE:-production}

# Build the classpath
ENV CLASSPATH=/app/app.jar:/app/resources:/app/lib/*

# Expose port 8080
EXPOSE 8080

# Create health check endpoint script
RUN echo '#!/bin/bash\ncurl -f http://localhost:8080/health || exit 1' > /app/health-check.sh && \
    chmod +x /app/health-check.sh

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD /app/health-check.sh

# Create start script
RUN echo '#!/bin/bash\n\
# Update persistence.xml with environment variables\n\
sed -i "s|jdbc:oracle:thin:@localhost:1521/FREEPDB1|jdbc:oracle:thin:@${DB_HOST}:${DB_PORT}/${DB_NAME}|g" /app/resources/META-INF/persistence.xml\n\
sed -i "s|<property name=\"javax.jdo.option.ConnectionUserName\" value=\"ISIS2304B05202110\"/>|<property name=\"javax.jdo.option.ConnectionUserName\" value=\"${DB_USERNAME}\"/>|g" /app/resources/META-INF/persistence.xml\n\
sed -i "s|<property name=\"javax.jdo.option.ConnectionPassword\" value=\"MXJgcEkeOLBb\"/>|<property name=\"javax.jdo.option.ConnectionPassword\" value=\"${DB_PASSWORD}\"/>|g" /app/resources/META-INF/persistence.xml\n\
\n\
# Start the application\n\
java -jar /app/app.jar \\\n\
  -Xmx512m \\\n\
  -Xms512m \\\n\
  -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} \\\n\
  -Dfile.encoding=UTF-8 \\\n\
  -Djava.awt.headless=true' > /app/start.sh && \
    chmod +x /app/start.sh

# Use the start script as entry point
CMD ["/app/start.sh"] 