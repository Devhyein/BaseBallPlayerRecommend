FROM openjdk:8-jdk-alpine

EXPOSE 8902

ARG JAR_FILE=target/NoBBaguBack.jar

COPY ${JAR_FILE} app.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=production" ,"-jar","/app.jar"]