FROM eclipse-temurin:21-jdk
EXPOSE 8082
ADD target/StudentManagement.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]