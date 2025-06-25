# ----------------------------------------------------------
# ---- 1. Build stage – compiles the JAR with Maven --------
# ----------------------------------------------------------
FROM maven:3.9.7-eclipse-temurin-21 AS build
WORKDIR /workspace

# Copy the Maven descriptor first so that dependency download
# is cached unless pom.xml actually changes
COPY pom.xml .
RUN mvn -ntp dependency:go-offline

# Copy source and build the runnable spring‑boot JAR
COPY src ./src
RUN mvn -ntp -DskipTests spring-boot:repackage

# ----------------------------------------------------------
# ---- 2. Runtime stage – slim JRE image -------------------
# ----------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy

# A couple of JVM flags that keep memory footprint low
ENV JAVA_TOOL_OPTIONS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75"

WORKDIR /app
COPY --from=build /workspace/target/*.jar app.jar

# Tell Docker (and Render’s port‑auto‑detector) what we intend
EXPOSE 8080

# Render sets $PORT at runtime (often 10000); forward it
# to Spring Boot if it exists, otherwise default to 8080.
ENTRYPOINT ["sh", "-c", "exec java $JAVA_TOOL_OPTIONS -jar app.jar --server.port=${PORT:-8080}"]
