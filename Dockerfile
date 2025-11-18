FROM amazoncorretto:17-alpine
COPY ./build/libs/order-service.jar order-service.jar
ENTRYPOINT ["java", "-XX:+UseG1GC", "-XX:MaxGCPauseMillis=200", "-jar", "order-service.jar"]