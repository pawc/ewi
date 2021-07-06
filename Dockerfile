FROM maven:3.8.1-jdk-8 AS build
COPY src /home/app/src
COPY pom.xml /home/app
RUN mvn -f /home/app/pom.xml clean package -DskipTests

FROM openjdk:8-jdk-alpine
COPY --from=build /home/app/target/ewi-0.0.1-SNAPSHOT.jar /home/spring/app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
ENTRYPOINT ["java","-jar","/home/spring/app.jar"]
EXPOSE 8080