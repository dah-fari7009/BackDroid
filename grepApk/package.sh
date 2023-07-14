
#!/bin/bash

#aapt dump badging $1 | grep package: | awk '{print $2}' | sed s/name=//g | sed s/\'//g
aapt dump badging $1 | sed -n "s/package: name='\([^']*\).*/\1/p"
