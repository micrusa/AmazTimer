# AmazTimer
[![Maintainability](https://api.codeclimate.com/v1/badges/9389ce1c8136678546c2/maintainability)](https://codeclimate.com/github/micrusa/AmazTimer/maintainability) [![Actions Status](https://github.com/micrusa/amaztimer/workflows/CI/badge.svg)](https://github.com/micrusa/amaztimer/actions) [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.me/migueelcs) [![latest release](https://img.shields.io/github/release/micrusa/AmazTimer.svg?label=latest%20release&style=flat)](https://github.com/micrusa/AmazTimer/releases/latest) [![commit activity](https://img.shields.io/github/commit-activity/m/micrusa/AmazTimer)](https://github.com/micrusa/AmazTimer/commits/master)

## Description
This app is an interval/reps/workout timer for amazfit devices to do trainings with HR/kcal monitoring and TCX exports, default values are tabata training's but you can use any times and sets you want

## Bugs and suggestions
This app is in continuous development and I don't have every device to test, so you may find bugs, if you find any please create an issue with logcat and I'll try to fix it as fast as I can. Don't report bugs that are already reported.
If you have any suggestions open an issue or answer the XDA thread

## Compatible devices
Any amazfit device running android 5.1 MIPS (Pace, stratos 1/2/3, verge)

## Notes
- TCX files are saved in "Internal Storage/AmazTimer", for some reason windows sometimes can't read it, so you can get them using `adb pull /sdcard/AmazTimer` or through AmazMod

## HW Buttons
- Pace/Verge/S2 with new layout: Single click to end set, long click to start/stop timer. Center button on S2
- Stratos with old key layout: lower button to start, center button for settings and upper button to end sets
- Stratos 3: Upper button to start activity, middle upper button to end sets and middle lower button to open settings
- You can invert start/finish set keys by enabling invert keys preference

## Installation
Install using `adb install AmazTimer-X.X.apk` and update using `adb install -r AmazTimer-X.X.apk`

You can also use installer or install/update it through AmazMod.

## Uninstallation
`adb uninstall me.micrusa.amaztimer`
You can also use installer or uninstall it through AmazMod

## Screenshots
<img src="https://github.com/micrusa/AmazTimer/raw/master/screen1.png"/><img src="https://github.com/micrusa/AmazTimer/raw/master/screen2.png"/><img src="https://github.com/micrusa/AmazTimer/raw/master/screen3.png"/><img src="https://github.com/micrusa/AmazTimer/raw/master/screen4.png"/>

## Thanks to
- [@GreatApo](https://github.com/GreatApo) for [Amazfit Calendar Widget](https://github.com/GreatApo/AmazfitPaceCalendarWidget)
- [AmazMod](https://github.com/AmazMod/AmazMod) team
- [@1immortal](https://github.com/1immortal) for creating app installer
- All contributors
