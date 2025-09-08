# Multi-stage Docker build optimized for resource-constrained environments
FROM eclipse-temurin:21-jdk-alpine as builder

# Set working directory
WORKDIR /app

# Install curl for health checks (needed for Alpine)
RUN apk add --no-cache curl

# Copy Maven wrapper and pom.xml
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY pom.xml .

# Download dependencies
RUN chmod +x ./mvnw && ./mvnw dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application with optimizations
RUN ./mvnw clean package -DskipTests -Dspring-boot.build-image.skip=true

# Production stage with minimal Alpine image
FROM eclipse-temurin:21-jre-alpine

# Install curl for health checks and create app user
RUN apk add --no-cache curl && \
    addgroup -g 1001 -S appuser && \
    adduser -S appuser -G appuser

# Set working directory
WORKDIR /app

# Copy the jar file from builder stage
COPY --from=builder /app/target/ecomapp-*.jar app.jar

# Change ownership to app user
RUN chown -R appuser:appuser /app
USER appuser

# Expose port
EXPOSE 8080

# Optimized health check with reduced frequency
HEALTHCHECK --interval=60s --timeout=5s --start-period=90s --retries=2 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Run the application with JVM optimizations for resource constraints
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+UseStringDeduplication", \
    "-Xss256k", \
    "-XX:MetaspaceSize=64m", \
    "-XX:MaxMetaspaceSize=128m", \
    "-jar", "app.jar"]
