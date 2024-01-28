cd ../nwdaf_sub_collector &&
git clone https://github.com/medianetlab/NEF_emulator &&
cd NEF_emulator &&
patch -p1 < ../../NWDAF_SUB/scripts/customize_nef_emulator.patch &&
rm -rf .git &&
cd -