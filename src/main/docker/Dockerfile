FROM anapsix/alpine-java:8
VOLUME /tmp
ADD spring-cloud-stream-sample-source-1.0.0.jar app.jar
RUN sh -c 'touch /app.jar'
ENV JAVA_OPTS=""
ENTRYPOINT [ "java", "-jar", "/app.jar" ]
