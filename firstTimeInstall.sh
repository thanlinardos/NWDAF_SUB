cd src/main/resources/certificates &&
../compose_files/createEnvFiles.sh &&
./generateHosts.sh &&
./generateCertificates.sh &&
./installCertificatesChrome.sh &&
cd ../../../../ &&
cp superproject/build.sh ../ &&
cp superproject/pom.xml ../ &&
chmod +777 ../build.sh &&
chmod +777 ../pom.xml &&
./installDockprom.sh &&
./installNef.sh
