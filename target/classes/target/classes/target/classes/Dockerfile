#FROM eclipse-temurin:17-jdk
#
#WORKDIR /data-segmentation
#
#COPY target/Data-Segmentation-0.0.1-SNAPSHOT.jar Data-Segmentation-0.0.1-SNAPSHOT.jar
#
##ENTRYPOINT ["java", "-jar", "Data-Segmentation-0.0.1-SNAPSHOT.jar"]
#CMD ["java", "-jar", "Data-Segmentation-0.0.1-SNAPSHOT.jar"]
##CMD java -jar Data-Segmentation-0.0.1-SNAPSHOT.jar
FROM openjdk:17
VOLUME /tmp
COPY target/Data-Segmentation-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
