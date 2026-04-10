# Kotlin Rebuild Plan

Dieses Repository enthaelt jetzt zwei Richtungen:

- bestehender Expo-/React-Native-Prototyp im Projektstamm
- neuer nativer Kotlin-Neuaufbau in `native-kotlin/`

## Warum als Schwesterprojekt?

So behalten wir:

- die bestehende Produktlogik als Referenz
- die UX-Entscheidungen aus dem aktuellen Prototyp
- einen sauberen nativen Neustart ohne hektisches Zerlegen des laufenden Projekts

## Native Stack

- Sprache: Kotlin
- UI: Jetpack Compose
- Plattformfokus: Android, querformatiger Motorrad-Laptimer

## Erste native Ziele

1. Standortfreigabe
2. Streckenvorschlag aus aktueller Position
3. Fahrmodus im Querformat
4. manuelle Runden
5. Session-Zusammenfassung

## Migrationsreihenfolge

1. UI-Struktur und Navigationsfluss aus dem Expo-Prototyp spiegeln
2. Track-Daten und Nearby-Suggestion in Kotlin uebernehmen
3. GPS-Standort und Permissions nativ anbinden
4. Session-State und manuelle Laps in Kotlin ViewModels verlagern
5. Delta-Logik und Referenzrunde nativ portieren
6. erst danach Android-spezifische Features vertiefen

## Android Studio

Wenn Android Studio fertig installiert ist:

1. Ordner `C:\dev\LapTimer\native-kotlin` oeffnen
2. Gradle Sync abwarten
3. App auf Emulator oder Geraet starten

## Wichtige Annahme

Die native App nutzt aktuell das Paket `com.marcbajwa.laptimernative`, damit sie nicht mit der bestehenden Expo-/Android-Variante kollidiert.
