FROM openjdk:17-jdk-alpine
COPY target/MonkSite-1.0-SNAPSHOT.jar MonkSite-1.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/MonkSite-1.0-SNAPSHOT.jar"]