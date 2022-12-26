FROM amazoncorretto:17
ARG JAR_FILE=./winwin-be-api/build/libs/winwin-be-api-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} /winwin-api.jar
ENTRYPOINT ["java","-jar","/winwin-api.jar"]
