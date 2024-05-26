cd src/main/resources/certificates &&
../compose_files/createEnvFiles.sh &&
./generateHosts.sh &&
./generateCertificates.sh &&
./installCertificatesChrome.sh &&
cd ../../../../ &&
./build.sh &&
./installDockprom.sh &&
./installNef.sh
