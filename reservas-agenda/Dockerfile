# ============================
# ETAPA 1: BUILD
# ============================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiar archivos necesarios para dependencias
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Dar permisos y descargar dependencias
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copiar código fuente
COPY src ./src

# Construir el JAR
RUN ./mvnw clean package -DskipTests

# Copiar el jar generado a un nombre fijo
RUN cp target/*.jar app.jar

# ============================
# ETAPA 2: RUNTIME
# ============================
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Crear usuario sin privilegios
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Copiar el JAR desde el builder
COPY --from=builder /app/app.jar app.jar

# Crear carpeta para secretos
USER root
RUN mkdir -p /etc/secrets && chown spring:spring /etc/secrets
USER spring:spring

# Exponer puerto
EXPOSE 8084

# Ejecutar
ENTRYPOINT ["java", "-jar", "app.jar"]