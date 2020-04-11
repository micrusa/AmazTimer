#!/bien/bash
adb wait-for-device
echo "Device detected!"
adb install ./app/release/app-release.apk
adb shell "am force-stop com.huami.watch.launcher"