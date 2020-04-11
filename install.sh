#!/bien/bash
adb wait-for-device
echo "Device detected!"
adb shell "pm uninstall me.micrusa.amaztimer"
adb install ./app/release/app-release.apk
adb shell "am force-stop com.huami.watch.launcher"
