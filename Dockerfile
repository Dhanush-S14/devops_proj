FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY target/library-management-system-1.0.0-jar-with-dependencies.jar /app/library-management-system.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "library-management-system.jar"]
