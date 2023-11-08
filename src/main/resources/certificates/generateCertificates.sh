#!/bin/bash

echo "Generating certificates" &&
SAN_VALUES=$(sed 's/.*/dns:&/' sanHosts.txt | tr '\n' ',' | sed 's/,$//') &&
SAN_CLIENT_VALUES=$(sed 's/.*/dns:&/' sanClientHosts.txt | tr '\n' ',' | sed 's/,$//') &&

name=test &&
password=123456 &&
org_unit=dev &&
org=org &&
city=city &&
state=state &&
country_code=gr &&

client_name=test &&
client_password=123456 &&
client_org_unit=dev &&
client_org=org &&
client_city=city &&
client_state=state &&
client_country_code=gr &&
rm local-ssl.p12 local-client-ssl.p12 local-cert.crt local-client-cert.crt
echo "Generating server certificate" &&
keytool -genkeypair -alias local_ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore local-ssl.p12 -storepass $password -dname "CN=$name, OU=$org_unit, O=$org, L=$city, ST=$state, C=$country_code" -validity 365 -ext "san=$SAN_VALUES" &&
openssl pkcs12 -in local-ssl.p12 -clcerts -nokeys -out local-cert.crt -passin pass:$password &&
echo "Generating client certificate" &&
keytool -genkeypair -alias local_client_ssl -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore local-client-ssl.p12 -storepass $client_password -dname "CN=$client_name, OU=$client_org_unit, O=$client_org, L=$client_city, ST=$client_state, C=$client_country_code" -validity 365 -ext "san=$SAN_CLIENT_VALUES" &&
openssl pkcs12 -in local-client-ssl.p12 -clcerts -nokeys -out local-client-cert.crt -passin pass:$client_password &&
echo "Done" &&
echo "Cleaning up..." &&
rm sanHosts.txt &&
rm sanClientHosts.txt &&
cd ../../../../../NWDAF_SUB_CLIENT/src/main/resources/certificates &&
rm local-ssl.p12 local-client-ssl.p12 local-cert.crt local-client-cert.crt
cd - &&
cp local-ssl.p12 local-client-ssl.p12 local-cert.crt local-client-cert.crt ../../../../../NWDAF_SUB_CLIENT/src/main/resources/certificates &&
echo "Finished"