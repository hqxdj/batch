FROM myharbor.com/alpine/oraclejre8
VOLUME /tmp
ARG JAR_FILE
ADD ${JAR_FILE} /app/app.jar
WORKDIR /app/
ENV JAVA_OPTS $JAVA_OPTS
ENV APP_OPTS $APP_OPTS
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar $APP_OPTS"]