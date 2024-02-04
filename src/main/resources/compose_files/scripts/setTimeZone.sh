tz=${TIMEZONE:-"UTC"}
echo "Setting timezone to $tz"
rm -rf /etc/localtime
ln -s /usr/share/zoneinfo/"$tz" /etc/localtime &&
echo "Success! Date: $(date)"