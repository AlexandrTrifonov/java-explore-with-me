FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar http-service.jar
ENTRYPOINT ["java","-jar","/http-service.jar"]