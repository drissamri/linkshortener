FROM drissamri/java:jre8

MAINTAINER  Driss Amri

ADD target/linkshortener-1.0.0-SNAPSHOT.jar /app/linkshortener.jar

EXPOSE 9080

CMD ["java", "-jar", "/app/linkshortener.jar"]