#!/bin/bash

echo "Generating certificates" &&
SAN_VALUES=$(sed 's/.*/dns:&/' sanHosts.txt | tr '\n' ',' | sed 's/,$//') &&
SAN_CLIENT_VALUES=$(sed 's/.*/dns:&/' sanClientHosts.txt | tr '\n' ',' | sed 's/,$//') &&
echo "Generating server certificate" &&
keytool -genkeypair -alias local_ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore local-ssl.p12 -validity 365 -ext "san=$SAN_VALUES" &&
openssl pkcs12 -in local-ssl.p12 -clcerts -nokeys -out local-ssl.crt &&
echo "Generating client certificate" &&
keytool -genkeypair -alias local_client_ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore local-client-ssl.p12 -validity 365 -ext "san=$SAN_CLIENT_VALUES" &&
openssl pkcs12 -in local-client-ssl.p12 -clcerts -nokeys -out local-client-ssl.crt &&
echo "Done" &&
echo "Cleaning up..." &&
rm sanHosts.txt &&
rm sanClientHosts.txt &&
cp local-ssl.p12 local-client-ssl.p12 local-ssl.crt local-client-ssl.crt ../../../../../NWDAF_SUB_CLIENT/src/main/resources/certificates
echo "Finished"