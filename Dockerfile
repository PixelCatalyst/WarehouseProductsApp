FROM openjdk:15-jdk-alpine

ADD backend/build/libs/backend-0.0.1-SNAPSHOT.jar /opt

CMD java -jar /opt/backend-0.0.1-SNAPSHOT.jar
