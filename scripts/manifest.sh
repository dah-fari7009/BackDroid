
#!/bin/bash

aapt d xmltree $1 AndroidManifest.xml | grep -e "E: activity" -e "E: action" -e "(0x01010003)"