# Event Planner

A basic app where you can add or delete events and edit their dates/time/title/category with an user friendly interface. The events' data is saved in an array, alongside with a SQLite database for persistence.

## NOTE

For general users, open the project in android studio and run through gradle runtime.

## DEV NOTE

For linux users, android studio emulator is broken on my machine (also, I want a more streamlined development environment). Use the following commands to run the app:

---

## On the 1st Terminal

### 1. Start emulator
```bash
QT_QPA_PLATFORM=xcb ~/Android/Sdk/emulator/emulator -avd Pixel_7_Pro -gpu software -no-snapshot-load
```

## On the 2nd terminal

### 2. Build and install app
```bash
./gradle installDebug
```

### 3. Launch app in emulator
```bash
adb shell am start -n com.example.myapplication/.MainActivity
```
