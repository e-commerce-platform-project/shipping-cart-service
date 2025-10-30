FROM openjdk:17-alpine
WORKDIR /app
RUN apk add --no-cache curl
COPY target/cart-service-0.0.1-SNAPSHOT.jar /app/cart-service.jar
ENTRYPOINT ["java", "-jar", "cart-service.jar"]