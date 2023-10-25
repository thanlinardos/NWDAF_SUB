echo "Installing the certificates to local machine & chrome..." &&

sudo chmoc +777 local-cert.crt &&
sudo chmoc +777 local-client-cert.crt &&
sudo cp local-cert.crt /usr/local/share/ca-certificates/ && 
sudo cp local-client-cert.crt /usr/local/share/ca-certificates/ && 
sudo update-ca-certificates && 
sudo apt-get update && 
sudo apt-get install libnss3-tools &&
while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_cert -i local-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') && 
while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_client_cert -i local-client-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') &&

echo "Done"