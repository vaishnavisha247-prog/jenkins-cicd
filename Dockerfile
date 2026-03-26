FROM openjdk:21
EXPOSE 8082
ADD target/StudentMangement-0.0.1-SNAPSHOT.jar StudentMangement.jar
ENTRYPOINT ["java","-jar","/StudentMangement.jar"]