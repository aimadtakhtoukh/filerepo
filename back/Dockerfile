FROM openjdk:8-jdk-alpine
VOLUME /tmp
VOLUME /data
ADD target/filerepo.jar app.jar
ENV JAVA_OPTS=""
EXPOSE 80
EXPOSE 443
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/urandom -jar /app.jar" ]