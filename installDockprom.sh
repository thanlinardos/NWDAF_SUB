git clone https://github.com/stefanprodan/dockprom &&
cd dockprom &&
patch -p1 < ../scripts/0001-fix-filesystem-add-amf-label-cadvisor-port.patch &&
echo "Changes applied locally from the patch file."