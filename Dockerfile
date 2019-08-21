FROM openjdk:8-jdk-alpine
VOLUME /tmp
VOLUME /pharbers_config
VOLUME /logs
COPY target/*.jar app.jar
ENV PHA_CONF_HOME /pharbers_config
ENTRYPOINT ["java","-jar","/app.jar"]