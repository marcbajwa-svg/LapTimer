# Android Studio Workflow

Dieses Projekt hat jetzt ein natives Android-Projekt im Ordner `android/`.

## Projekt in Android Studio oeffnen

1. Android Studio starten
2. `Open` waehlen
3. Den Ordner `C:\dev\LapTimer\android` oeffnen

## Wichtige Hinweise

- Die App bleibt aktuell fachlich eine React-Native-/Expo-App.
- Android Studio uebernimmt jetzt den nativen Android-Build.
- JavaScript- und UI-Logik bleiben im Projektstamm, vor allem in `App.tsx` und `src/`.

## Lokaler Android-Run

Im Projektstamm:

```powershell
cd C:\dev\LapTimer
& "C:\Program Files\nodejs\npm.cmd" run android
```

Dieser Befehl nutzt jetzt `expo run:android` und das native Android-Projekt.

## Wenn native Android-Aenderungen noetig werden

- Android Manifest: `android/app/src/main/AndroidManifest.xml`
- Gradle-Config: `android/build.gradle`
- App-Gradle: `android/app/build.gradle`

## Wichtig fuer spaetere Expo-Config-Aenderungen

Wenn wir spaeter `app.json`-Einstellungen aendern, kann es sein, dass wir die nativen Dateien neu synchronisieren muessen.
