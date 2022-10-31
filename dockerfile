FROM openjdk:17-alpine
ARG JAR_FILE=/build/libs/winwin-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /win-win.jar
ENTRYPOINT ["java","-jar","/win-win.jar"]
