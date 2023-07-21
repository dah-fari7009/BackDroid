
#!/bin/bash

aapt list -a $1 | sed -n '/ '"$2"' /{:loop n;s/^.*android:name.*="\([^"]\{1,\}\)".*/\1/;T loop;p;t}'
