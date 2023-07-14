#!/bin/bash

mainClass=../lib/classes-2.3.4_r1.jar
mainGAPI=../lib/gapi-16.jar
mainSDK=../lib/android_v30.jar

otherClass=../lib/android_v30.jar:../lib/android_v28.jar:../lib/android_v27.jar:../lib/android_v23.jar:../lib/android_v22.jar:../lib/android_v19.jar:../lib/android_v18.jar:../lib/android_v15.jar:../lib/android_v13.jar:../lib/android_v10.jar:../lib/android_v8.jar:../lib/android_v7.jar:../lib/android_v4.jar:../lib/classes-2.2_r1.jar
otherSDK=../lib/android-support-v13.jar:../lib/android-support-v7-appcompat.jar:../lib/android-support-v7-cardview.jar:../lib/android-support-v7-gridlayout.jar:../lib/android-support-v7-mediarouter.jar:../lib/android-support-v7-palette.jar:../lib/android-support-v7-preference.jar:../lib/android-support-v7-recyclerview.jar:../lib/android-support-v4.jar

infoflow=../lib/soot-infoflow.jar:../lib/soot-infoflow-android.jar
otherJar=../lib/slf4j-api-1.7.5.jar:../lib/slf4j-simple-1.7.5.jar:../lib/axml-2.0.jar
sootJar=../lib/soot-trunk.jar

# $1: -a, the apk prefix name
# $2: -p, the pkg name 
# $ ../bin/backdroid.sh com.samremote.view-16 com.samremote.view
if [ $1 ] && [ $2 ] && [ $3 ] #&& [ $4 ]
then
time java -Xmx4g -Xss100m -cp ../BackDroid/bin:$sootJar:../lib/commons-cli-1.2.jar:$infoflow:$otherJar:$mainSDK:$mainGAPI:$mainClass:$otherSDK:$otherClass edu.smu.backdroid.PortDetector -a $1 -p $2 -t $3 #-r $4
# $1: -a, the apk prefix name
# backDroid/test$ ../bin/backdroid.sh com.samremote.view-16
else
time java -Xmx4g -Xss100m -cp ../BackDroid/bin:$sootJar:../lib/commons-cli-1.2.jar:$infoflow:$otherJar:$mainSDK:$mainGAPI:$mainClass:$otherSDK:$otherClass edu.smu.backdroid.PortDetector -a $1
fi
