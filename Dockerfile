#for windows:
# FROM eclipse-temurin:17-jdk-alpine as build
# WORKDIR /workspace/app

# COPY mvnw .
# COPY .mvn .mvn
# COPY pom.xml .
# COPY src src

# RUN ./mvnw install -DskipTests
# RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# FROM eclipse-temurin:17-jre-alpine
# VOLUME /tmp
# ARG DEPENDENCY=/workspace/app/target/dependency
# COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
# COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
# COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
# ENTRYPOINT ["java","-cp","app:app/lib/*","io.nwdaf.eventsubscription.NwdafSubApplication"]

#for ubuntu:
FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
EXPOSE 8080 5005
ARG JAR_FILE=target/NWDAF_SUB.jar
ARG CERT_FILE=src/main/resources/certificates/local-cert.crt
ARG SSL_FILE=src/main/resources/certificates/local-ssl.p12
ARG CLIENT_SSL_FILE=src/main/resources/certificates/local-client-ssl.p12
ARG CLIENT_CERT_FILE=src/main/resources/certificates/local-client-cert.crt
ADD ${JAR_FILE} app.jar
ADD test.json test.json
ADD notifTest.json notifTest.json
ADD ${CERT_FILE} /usr/local/share/ca-certificates/local-cert.crt
ADD ${SSL_FILE} src/main/resources/certificates/local-ssl.p12
ADD ${CLIENT_CERT_FILE} /usr/local/share/ca-certificates/local-client-cert.crt
ADD ${CLIENT_SSL_FILE} src/main/resources/certificates/local-client-ssl.p12
RUN chmod 644 /usr/local/share/ca-certificates/local-cert.crt && chmod 644 /usr/local/share/ca-certificates/local-client-cert.crt && update-ca-certificates
ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar"]