FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/otus-1.0.0.jar app.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","/app.jar"]