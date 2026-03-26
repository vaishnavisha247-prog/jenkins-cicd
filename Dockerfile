FROM eclipse-temurin:21-jdk
EXPOSE 8082
ADD target/StudentMangement-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]