FROM        maven:3.2-jdk-8

MAINTAINER  Driss Amri

WORKDIR /linkshortener

# Retrieve dependencies during docker build
ADD pom.xml /linkshortener/pom.xml
RUN ["mvn", "dependency:resolve"]
RUN ["mvn", "verify"]

# Add source and package
ADD src /linkshortener/src
RUN ["mvn", "package"]

CMD ["java", "-jar", "/linkshortener/target/linkshortener-1.0.0-SNAPSHOT.jar"]