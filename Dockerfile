
FROM openjdk:17-jdk-slim

WORKDIR /home/Spring-FTP-Server-Example

COPY build/libs/Spring-FTP-Server-Example-0.0.1-SNAPSHOT.jar /home/Spring-FTP-Server-Example/application.jar

CMD ["java", "-jar", "application.jar"]

EXPOSE 10000