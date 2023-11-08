echo "Installing the certificates to local machine & chrome..." &&

sudo chmod +777 local-cert.crt &&
sudo chmod +777 local-client-cert.crt &&
cd /usr/local/share/ca-certificates/ &&
rm local-client-cert.crt local-cert.crt -y
cd - &&
sudo cp local-client-cert.crt local-cert.crt /usr/local/share/ca-certificates/ && 
sudo update-ca-certificates && 
sudo apt-get update && 
sudo apt-get install libnss3-tools &&

while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -D -n local_cert; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') &&
while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -D -n local_client_cert; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') &&

echo "Removed existing certificates from chrome" &&

while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_cert -i local-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') && 
while read -r -d $'\0' i ; do    sudo certutil -d 'sql:'"$i" -A -t "C,," -n local_client_cert -i local-client-cert.crt; done < <(sudo find "$HOME" -type f -iregex '.*[/]cert[89][.]db' -printf '%h\0') &&

echo "Added new certificates"