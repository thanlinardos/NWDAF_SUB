cd src/main/resources/certificates &&
./generateHosts.sh &&
./generateCertificates.sh &&
./installCertificatesChrome.sh &&
cd ../../../../ &&
./build.sh &&
./installDockprom.sh &&
./installNef.sh
