# AmazTimer

## Description
This app is an interval timer made from scratch for amazfit devices to do interval trainings with HR monitoring and TCX exports, default values are tabata training's but you can use any times and sets you want

## Bugs and suggestions
This app is in development so you may find bugs, if you have any please create an issue with logcat and I'll try to fix it as fast as I can. Don't report bugs that are already reported or listed in release's description
If you have any suggestions open an issue and explain it

## Compatible devices
Any amazfit device running android (Pace, stratos 1/2/3, verge)

## Notes
- It may work on Nexo devices, but I won't give support to them.
- Stratos 3 changed a lot of things so there might be lags which I can't fix, blame huami
- TCX files are saved in "Internal Storage/AmazTimer", for some weird reason windows sometimes can't read it, so you can get them using `adb pull /sdcard/AmazTimer` or through AmazMod

## Installation
Install using
`adb install AmazTimer-X.X.apk && adb shell am force-stop com.huami.watch.launcher`
Update using
`adb install -r AmazTimer-X.X.apk && adb shell am force-stop com.huami.watch.launcher`
You can also use installer or install it through AmazMod.

## Uninstallation
`adb uninstall me.micrusa.amaztimer`
You can also use installer or uninstall it through AmazMod

## Screenshots
<img src="https://github.com/micrusa/AmazTimer/raw/master/screen1.png"/><img src="https://github.com/micrusa/AmazTimer/raw/master/screen2.png"/><img src="https://github.com/micrusa/AmazTimer/raw/master/screen3.png"/><img src="https://github.com/micrusa/AmazTimer/raw/master/screen4.png"/>

## Thanks to
- [@KieronQuinn](https://github.com/KieronQuinn) for [Springboard Plugin Example](https://github.com/KieronQuinn/AmazfitSpringboardPluginExample)
- [@GreatApo](https://github.com/GreatApo) for [Amazfit Calendar Widget](https://github.com/GreatApo/AmazfitPaceCalendarWidget)
- [AmazMod](https://github.com/AmazMod/AmazMod) team
- [@1immortal](https://github.com/1immortal) for creating app installer

## How to move or disable widget
- If you have AmazMod, you can do it through mobile app or watch app
- If you don't have AmazMod, you can use [@KieronQuinn](https://github.com/KieronQuinn)'s [springboard settings app](https://github.com/KieronQuinn/AmazfitSpringboardSettings)