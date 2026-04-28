# =========================
# Stage 1: Build (Maven)
# =========================
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 1️⃣ Copy only pom first (better caching)
COPY pom.xml .

# Download dependencies first (cached layer)
RUN mvn dependency:go-offline -B

# 2️⃣ Now copy source
COPY src ./src

# Build jar
RUN mvn clean package -DskipTests -B


# =========================
# Stage 2: Runtime (Slim JDK)
# =========================
FROM eclipse-temurin:21-jdk-jammy

WORKDIR /app

# Create non-root user (security best practice)
RUN useradd -m appuser
USER appuser

# Copy jar from builder stage
COPY --from=builder /app/target/*.jar app.jar

# JVM optimizations (important for production)
ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

EXPOSE 8080

# Run app
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]