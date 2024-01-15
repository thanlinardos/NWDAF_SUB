git clone https://github.com/stefanprodan/dockprom &&
cd dockprom &&
patch -p1 < ../scripts/customize_grafana_and_prom_label.patch &&
echo "Changes applied locally from the patch file."
rm -rf .git