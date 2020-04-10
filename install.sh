#!/bien/bash
adb wait-for-device
echo "Device detected!"
adb shell "pm uninstall me.micrusa.amaztimer"
adb install ./app/build/outputs/apk/debug/app-debug.apk
adb shell "am force-stop com.huami.watch.launcher"
