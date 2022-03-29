FROM openjdk:11
# copy the packaged jar file into our docker image
COPY build/libs/Hash-1.0-SNAPSHOT.jar /demo.jar
# set the startup command to execute the jar
CMD ["java", "-jar", "/demo.jar"]