#!/bin/bash

./gradlew android:assembleRelease && 
jarsigner -verbose -sigalg SHA1withRSA -digestalg SHA1 -keystore ./doremi-release-key.keystore ./android/build/outputs/apk/android-release-unsigned.apk doremi &&
jarsigner -verify -verbose -certs ./android/build/outputs/apk/android-release-unsigned.apk &&
./zipalign -v -f 4 ./android/build/outputs/apk/android-release-unaligned.apk ./android/build/outputs/apk/doremi.apk


