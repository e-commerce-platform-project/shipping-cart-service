FROM openjdk:17-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY target/*.jar /app/app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]