FROM openjdk:11-jre
ENTRYPOINT ["java", "-jar", "/opt/app.jar"]
ARG JAR_FILE
ADD @project.build.finalName@.jar /opt/app.jar