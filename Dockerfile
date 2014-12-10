FROM dockerfile/java:oracle-java8

MAINTAINER  Driss Amri

ADD target/linkshortener-1.0.0-SNAPSHOT.jar /app/linkshortener.jar

CMD ["java", "-jar", "/app/linkshortener.jar"]