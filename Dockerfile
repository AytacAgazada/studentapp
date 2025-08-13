#FROM openjdk:17-jdk-slim
#
#WORKDIR /app
#
#COPY build/libs/studentapp-0.0.1-SNAPSHOT.jar app.jar
#
#EXPOSE 9091
#
#ENTRYPOINT ["java", "-jar", "app.jar"]

FROM openjdk:17-jdk-slim

WORKDIR /app


COPY build/libs/studentapp-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9091

ENTRYPOINT ["java", "-jar", "app.jar"]