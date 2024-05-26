FROM eclipse-temurin:21-jre-alpine
VOLUME /tmp
ARG JAR_FILE=target/NWDAF_SUB.jar
ARG CERT_FILE=src/main/resources/certificates/local-cert.crt
ARG SSL_FILE=src/main/resources/certificates/local-ssl.p12
ARG CLIENT_SSL_FILE=src/main/resources/certificates/local-client-ssl.p12
ARG CLIENT_CERT_FILE=src/main/resources/certificates/local-client-cert.crt
ADD ${JAR_FILE} app.jar
ADD ue_mobility_test.json ue_mobility_test.json
ADD ue_communication_test.json ue_communication_test.json
ADD nf_load_test.json nf_load_test.json
ADD notifTest.json notifTest.json
ADD ${CERT_FILE} /usr/local/share/ca-certificates/local-cert.crt
ADD ${SSL_FILE} src/main/resources/certificates/local-ssl.p12
ADD ${CLIENT_CERT_FILE} /usr/local/share/ca-certificates/local-client-cert.crt
ADD ${CLIENT_SSL_FILE} src/main/resources/certificates/local-client-ssl.p12
RUN chmod 644 /usr/local/share/ca-certificates/local-cert.crt && chmod 644 /usr/local/share/ca-certificates/local-client-cert.crt && update-ca-certificates
#ENTRYPOINT ["java","-Xdebug","-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005","-jar","/app.jar"]