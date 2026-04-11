package com.marcbajwa.laptimernative.ui

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ActivityInfo
import androidx.core.content.FileProvider
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.PowerSettingsNew
import androidx.compose.material.icons.outlined.ScreenRotation
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcbajwa.laptimernative.data.LocalLapTimerStore
import com.marcbajwa.laptimernative.data.TrackRepository
import com.marcbajwa.laptimernative.location.NativeLocationTracker
import com.marcbajwa.laptimernative.model.CurrentPosition
import com.marcbajwa.laptimernative.model.LapTimingState
import com.marcbajwa.laptimernative.model.LiveSessionSnapshot
import com.marcbajwa.laptimernative.model.Screen
import com.marcbajwa.laptimernative.model.TrackPreset
import com.marcbajwa.laptimernative.sensor.LeanAngleTracker
import com.marcbajwa.laptimernative.sensor.normalizeDegrees
import com.marcbajwa.laptimernative.telemetry.TelemetryExporter
import com.marcbajwa.laptimernative.telemetry.TelemetrySample
import com.marcbajwa.laptimernative.timing.LapTimingEngine
import java.io.File
import kotlin.math.abs

private enum class AppLanguage {
    DE,
    EN,
}

private enum class OrientationMode {
    AUTO,
    LANDSCAPE,
    PORTRAIT,
}

private enum class SessionMode {
    RUNNING,
    PAUSED,
    ENDED,
}

private data class NativeCopy(
    val navHome: String,
    val navSetup: String,
    val navLive: String,
    val navSummary: String,
    val homeEyebrow: String,
    val homeTitle: String,
    val homeSubtitle: String,
    val nextStepTitle: String,
    val nextStepSubtitle: String,
    val openTrackSetup: String,
    val openLiveCockpit: String,
    val setupEyebrow: String,
    val setupTitle: String,
    val setupSubtitle: String,
    val nearbyTitle: String,
    val nearbySubtitle: String,
    val useSuggestedTrack: String,
    val setManualStartPoint: String,
    val trackLibraryTitle: String,
    val trackLibrarySubtitle: String,
    val goToLiveCockpit: String,
    val liveCurrentLap: String,
    val liveDelta: String,
    val liveSpeed: String,
    val liveLean: String,
    val leanLeft: String,
    val leanRight: String,
    val calibrateLean: String,
    val leanCalibrated: String,
    val leanWaiting: String,
    val telemetryOn: String,
    val telemetryOff: String,
    val telemetryEnabled: String,
    val telemetryDisabled: String,
    val telemetrySamples: String,
    val exportTelemetry: String,
    val telemetryExported: String,
    val telemetryNoData: String,
    val liveBestLap: String,
    val liveLastLap: String,
    val liveTotalLaps: String,
    val liveLapMark: String,
    val livePause: String,
    val liveResume: String,
    val liveEnd: String,
    val gpsLabel: String,
    val gpsWaiting: String,
    val gpsPermissionNeeded: String,
    val requestGpsPermission: String,
    val noNearbyTrack: String,
    val manualStartReady: String,
    val manualStartWaiting: String,
    val manualStartNeedsBetterGps: String,
    val manualTrackName: String,
    val manualTrackCountry: String,
    val manualTrackMarker: String,
    val summaryEyebrow: String,
    val summaryTitle: String,
    val summarySubtitle: String,
    val selectedTrackTitle: String,
    val selectedTrackSubtitle: String,
    val languageButton: String,
    val closeAppButton: String,
    val orientationAuto: String,
    val orientationLandscape: String,
    val orientationPortrait: String,
)

private val germanCopy = NativeCopy(
    navHome = "Start",
    navSetup = "Setup",
    navLive = "Live",
    navSummary = "Auswertung",
    homeEyebrow = "LapTimer Native",
    homeTitle = "Kotlin-Neuaufbau fuer einen echten Android-Laptimer",
    homeSubtitle = "Diese native Version behaelt den Produktfluss aus dem Expo-Prototyp, bringt uns aber in eine Android-first-Architektur mit Compose.",
    nextStepTitle = "Naechster Schritt",
    nextStepSubtitle = "Richte zuerst das Setup sauber ein, bevor du rausfaehrst.",
    openTrackSetup = "Track-Setup oeffnen",
    openLiveCockpit = "Live-Cockpit ansehen",
    setupEyebrow = "Track Setup",
    setupTitle = "Naechste Rennstrecke zuerst vorschlagen",
    setupSubtitle = "Der native Neuaufbau soll die wahrscheinlich naechste Strecke direkt aus GPS vorschlagen und sonst auf einen manuellen Startpunkt zurueckfallen.",
    nearbyTitle = "Streckenvorschlag in der Naehe",
    nearbySubtitle = "Das wird die erste Entscheidung im nativen Android-Setup.",
    useSuggestedTrack = "Vorgeschlagene Strecke nutzen",
    setManualStartPoint = "Manuellen Startpunkt setzen",
    trackLibraryTitle = "Streckenbibliothek",
    trackLibrarySubtitle = "Kuratierte Presets bleiben auch mit GPS-Vorschlaegen wertvoll.",
    goToLiveCockpit = "Zum Live-Cockpit",
    liveCurrentLap = "AKTUELLE RUNDE",
    liveDelta = "DELTA ZUR BESTZEIT",
    liveSpeed = "GESCHWINDIGKEIT",
    liveLean = "SCHRAEGLAGE",
    leanLeft = "MAX LINKS",
    leanRight = "MAX RECHTS",
    calibrateLean = "Schraeglage kalibrieren",
    leanCalibrated = "Schraeglage kalibriert",
    leanWaiting = "Sensor wartet",
    telemetryOn = "Telemetrie: Ein",
    telemetryOff = "Telemetrie: Aus",
    telemetryEnabled = "Telemetrie-Aufzeichnung aktiviert",
    telemetryDisabled = "Telemetrie-Aufzeichnung deaktiviert",
    telemetrySamples = "TELEMETRIE",
    exportTelemetry = "CSV exportieren",
    telemetryExported = "CSV gespeichert",
    telemetryNoData = "Noch keine Telemetrie-Daten",
    liveBestLap = "BESTE RUNDE",
    liveLastLap = "LETZTE RUNDE",
    liveTotalLaps = "RUNDEN",
    liveLapMark = "Lap markieren",
    livePause = "Pause",
    liveResume = "Fortsetzen",
    liveEnd = "Ende",
    gpsLabel = "GPS",
    gpsWaiting = "GPS wartet",
    gpsPermissionNeeded = "Standortfreigabe fehlt",
    requestGpsPermission = "GPS freigeben",
    noNearbyTrack = "Keine bekannte Strecke in der Naehe",
    manualStartReady = "Manueller Startpunkt gesetzt",
    manualStartWaiting = "Warte noch auf einen GPS-Fix fuer den manuellen Startpunkt",
    manualStartNeedsBetterGps = "GPS ist noch zu ungenau fuer den Startpunkt",
    manualTrackName = "Eigener Startpunkt",
    manualTrackCountry = "Eigene Strecke",
    manualTrackMarker = "Manuell gesetzte Start-/Ziellinie",
    summaryEyebrow = "Session-Auswertung",
    summaryTitle = "Die native Auswertung bleibt schnell lesbar",
    summarySubtitle = "Dieser Screen wird das Kotlin-Zuhause fuer Runden, Referenz-Qualitaet und Export-Aktionen.",
    selectedTrackTitle = "Aktive Strecke",
    selectedTrackSubtitle = "Aktuelles Ziel des nativen Umbaus",
    languageButton = "English",
    closeAppButton = "Beenden",
    orientationAuto = "Auto",
    orientationLandscape = "Quer",
    orientationPortrait = "Hoch",
)

private val englishCopy = NativeCopy(
    navHome = "Home",
    navSetup = "Setup",
    navLive = "Live",
    navSummary = "Summary",
    homeEyebrow = "LapTimer Native",
    homeTitle = "Kotlin rebuild for a real Android track tool",
    homeSubtitle = "This native version keeps the product flow from the Expo prototype, but moves us into an Android-first architecture with Compose.",
    nextStepTitle = "Next Step",
    nextStepSubtitle = "Get the setup right before you roll out.",
    openTrackSetup = "Open track setup",
    openLiveCockpit = "Open live cockpit",
    setupEyebrow = "Track Setup",
    setupTitle = "Suggest the nearest circuit first",
    setupSubtitle = "The native rebuild should immediately surface the likely circuit from GPS, then fall back to a manual start point when nothing matches nearby.",
    nearbyTitle = "Nearby suggestion",
    nearbySubtitle = "This becomes the first decision in the Android setup flow.",
    useSuggestedTrack = "Use suggested track",
    setManualStartPoint = "Set manual start point",
    trackLibraryTitle = "Track library",
    trackLibrarySubtitle = "Curated presets stay useful even after GPS suggestions land.",
    goToLiveCockpit = "Go to live cockpit",
    liveCurrentLap = "CURRENT LAP",
    liveDelta = "DELTA TO BEST",
    liveSpeed = "SPEED",
    liveLean = "LEAN ANGLE",
    leanLeft = "MAX LEFT",
    leanRight = "MAX RIGHT",
    calibrateLean = "Calibrate lean",
    leanCalibrated = "Lean calibrated",
    leanWaiting = "Waiting for sensor",
    telemetryOn = "Telemetry: On",
    telemetryOff = "Telemetry: Off",
    telemetryEnabled = "Telemetry recording enabled",
    telemetryDisabled = "Telemetry recording disabled",
    telemetrySamples = "TELEMETRY",
    exportTelemetry = "Export CSV",
    telemetryExported = "CSV saved",
    telemetryNoData = "No telemetry data yet",
    liveBestLap = "BEST LAP",
    liveLastLap = "LAST LAP",
    liveTotalLaps = "LAPS",
    liveLapMark = "Mark lap",
    livePause = "Pause",
    liveResume = "Resume",
    liveEnd = "End",
    gpsLabel = "GPS",
    gpsWaiting = "Waiting for GPS",
    gpsPermissionNeeded = "Location permission needed",
    requestGpsPermission = "Allow GPS",
    noNearbyTrack = "No known track nearby",
    manualStartReady = "Manual start point saved",
    manualStartWaiting = "Waiting for a GPS fix before setting the manual start point",
    manualStartNeedsBetterGps = "GPS is not accurate enough for the start point yet",
    manualTrackName = "Custom start point",
    manualTrackCountry = "Custom track",
    manualTrackMarker = "Manually saved start / finish line",
    summaryEyebrow = "Session Summary",
    summaryTitle = "Native summary will stay quick to scan",
    summarySubtitle = "This screen becomes the Kotlin home for laps, reference run health, and export actions.",
    selectedTrackTitle = "Selected track",
    selectedTrackSubtitle = "Current native migration target",
    languageButton = "Deutsch",
    closeAppButton = "Close",
    orientationAuto = "Auto",
    orientationLandscape = "Wide",
    orientationPortrait = "Tall",
)

@Composable
fun LapTimerNativeApp() {
    val context = LocalContext.current
    val activity = context.findActivity()
    val store = remember { LocalLapTimerStore(context.applicationContext) }
    val telemetryExporter = remember { TelemetryExporter(context.applicationContext) }
    val storedManualTrack = remember { store.loadManualTrack() }
    var activeScreen by remember { mutableStateOf(Screen.Home) }
    var selectedTrack by remember { mutableStateOf(storedManualTrack ?: TrackRepository.nearbySuggestion) }
    var language by remember { mutableStateOf(AppLanguage.DE) }
    var orientationMode by remember { mutableStateOf(OrientationMode.LANDSCAPE) }
    var currentPosition by remember { mutableStateOf<CurrentPosition?>(null) }
    var locationPermissionGranted by remember { mutableStateOf(context.hasLocationPermission()) }
    var manualTrack by remember { mutableStateOf(storedManualTrack) }
    var setupStatusMessage by remember { mutableStateOf<String?>(null) }
    val lapTimingEngine = remember { LapTimingEngine() }
    var lapTimingState by remember { mutableStateOf(LapTimingState()) }
    var sessionMode by remember { mutableStateOf(SessionMode.RUNNING) }
    var rawLeanDegrees by remember { mutableStateOf<Float?>(null) }
    var leanBaselineDegrees by remember { mutableStateOf<Float?>(null) }
    var currentLeanDegrees by remember { mutableStateOf<Float?>(null) }
    var maxLeftLeanDegrees by remember { mutableStateOf(0f) }
    var maxRightLeanDegrees by remember { mutableStateOf(0f) }
    var telemetryEnabled by remember { mutableStateOf(true) }
    var telemetrySamples by remember { mutableStateOf<List<TelemetrySample>>(emptyList()) }
    var lastTelemetryExportFile by remember { mutableStateOf<File?>(null) }

    val copy = if (language == AppLanguage.DE) germanCopy else englishCopy
    val nearbyTrack = TrackRepository.findNearbyTrack(currentPosition)
    val liveSnapshot = TrackRepository.liveSnapshot.copy(
        currentLap = lapTimingState.currentLapMillis.formatLapTime(),
        currentDelta = lapTimingState.currentDeltaMillis.formatDelta(),
        lastLap = lapTimingState.lastLapMillis.formatLapTime(),
        bestLap = lapTimingState.bestLapMillis.formatLapTime(),
        totalLaps = lapTimingState.totalLaps,
        gpsStatus = when {
            !locationPermissionGranted -> copy.gpsPermissionNeeded
            currentPosition == null -> copy.gpsWaiting
            else -> "${TrackRepository.formatAccuracy(currentPosition)} - ${lapTimingState.status}"
        },
        speedLabel = TrackRepository.formatSpeed(currentPosition),
        leanCurrentLabel = currentLeanDegrees.formatLean(),
        leanLeftLabel = maxLeftLeanDegrees.formatLean(),
        leanRightLabel = maxRightLeanDegrees.formatLean(),
    )
    val telemetryStatusLabel = "${telemetrySamples.size} samples"
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) { permissions ->
        locationPermissionGranted =
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true ||
            context.hasLocationPermission()
    }
    val showAppChrome = activeScreen != Screen.Live && activeScreen != Screen.Summary

    LaunchedEffect(orientationMode) {
        context.findActivity()?.requestedOrientation = when (orientationMode) {
            OrientationMode.AUTO -> ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            OrientationMode.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            OrientationMode.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    }

    LaunchedEffect(selectedTrack.id) {
        lapTimingState = lapTimingEngine.reset(
            track = selectedTrack,
            savedBestLapMillis = store.loadBestLapMillis(selectedTrack.id),
        )
        sessionMode = SessionMode.RUNNING
        maxLeftLeanDegrees = 0f
        maxRightLeanDegrees = 0f
        telemetrySamples = emptyList()
        lastTelemetryExportFile = null
    }

    DisposableEffect(Unit) {
        val tracker = LeanAngleTracker(context) { rawRollDegrees ->
            rawLeanDegrees = rawRollDegrees
            val baseline = leanBaselineDegrees
            if (baseline != null) {
                val relativeLean = normalizeDegrees(rawRollDegrees - baseline)
                currentLeanDegrees = relativeLean
                if (sessionMode == SessionMode.RUNNING) {
                    if (relativeLean < 0f && abs(relativeLean) > maxLeftLeanDegrees) {
                        maxLeftLeanDegrees = abs(relativeLean)
                    }
                    if (relativeLean > 0f && relativeLean > maxRightLeanDegrees) {
                        maxRightLeanDegrees = relativeLean
                    }
                }
            }
        }
        tracker.start()
        onDispose { tracker.stop() }
    }

    DisposableEffect(locationPermissionGranted, selectedTrack.id, sessionMode) {
        if (!locationPermissionGranted) {
            onDispose {}
        } else {
            val tracker = NativeLocationTracker(context) { position ->
                currentPosition = position
                if (sessionMode == SessionMode.RUNNING) {
                    lapTimingState = lapTimingEngine.update(position, selectedTrack).also { updatedState ->
                        updatedState.bestLapMillis?.let { bestLapMillis ->
                            store.saveBestLapMillis(selectedTrack.id, bestLapMillis)
                        }
                    }
                    if (telemetryEnabled) {
                        telemetrySamples = telemetrySamples + TelemetrySample(
                            timestampMillis = System.currentTimeMillis(),
                            latitude = position.latitude,
                            longitude = position.longitude,
                            speedKmh = position.speedKmh,
                            gpsAccuracyMeters = position.accuracyMeters,
                            leanDegrees = currentLeanDegrees,
                            lapIndex = lapTimingState.totalLaps,
                        )
                    }
                }
            }
            tracker.start()
            onDispose { tracker.stop() }
        }
    }

    Scaffold(
        containerColor = Color(0xFFF4EFE4),
        topBar = {
            if (showAppChrome) {
                TopControlsBar(
                    copy = copy,
                    orientationMode = orientationMode,
                    onCloseApp = { activity?.finishAffinity() },
                    onToggleLanguage = {
                        language = if (language == AppLanguage.DE) AppLanguage.EN else AppLanguage.DE
                    },
                    onCycleOrientation = {
                        orientationMode = when (orientationMode) {
                            OrientationMode.AUTO -> OrientationMode.LANDSCAPE
                            OrientationMode.LANDSCAPE -> OrientationMode.PORTRAIT
                            OrientationMode.PORTRAIT -> OrientationMode.AUTO
                        }
                    },
                )
            }
        },
        bottomBar = {
            if (showAppChrome) {
                BottomBar(
                    copy = copy,
                    activeScreen = activeScreen,
                    onSelect = { activeScreen = it },
                )
            }
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            color = Color(0xFFF4EFE4),
        ) {
            when (activeScreen) {
                Screen.Home -> HomeScreen(
                    copy = copy,
                    onGoSetup = { activeScreen = Screen.Setup },
                    onGoLive = { activeScreen = Screen.Live },
                )
                Screen.Setup -> SetupScreen(
                    copy = copy,
                    nearbyTrack = nearbyTrack,
                    presets = TrackRepository.presets,
                    manualTrack = manualTrack,
                    selectedTrack = selectedTrack,
                    locationPermissionGranted = locationPermissionGranted,
                    locationStatus = liveSnapshot.gpsStatus,
                    telemetryEnabled = telemetryEnabled,
                    onRequestLocationPermission = {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            ),
                        )
                    },
                    onSetManualStartPoint = {
                        when {
                            !locationPermissionGranted -> {
                                setupStatusMessage = copy.gpsPermissionNeeded
                                locationPermissionLauncher.launch(
                                    arrayOf(
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                    ),
                                )
                            }
                            currentPosition == null -> {
                                setupStatusMessage = copy.manualStartWaiting
                            }
                            !requireNotNull(currentPosition).hasManualStartAccuracy() -> {
                                setupStatusMessage = copy.manualStartNeedsBetterGps
                            }
                            else -> {
                                val position = requireNotNull(currentPosition)
                                val track = TrackPreset(
                                    id = "manual-start-${position.elapsedRealtimeMillis}",
                                    name = copy.manualTrackName,
                                    country = copy.manualTrackCountry,
                                    markerLabel = "${copy.manualTrackMarker} (${position.formatCoordinates()})",
                                    minimumLapSeconds = 10,
                                    latitude = position.latitude,
                                    longitude = position.longitude,
                                    suggestionRadiusMeters = 100,
                                    startHeadingDegrees = null,
                                    startLineHalfWidthMeters = 35.0,
                                    distanceLabel = TrackRepository.formatAccuracy(position),
                                )
                                manualTrack = track
                                selectedTrack = track
                                store.saveManualTrack(track)
                                setupStatusMessage = copy.manualStartReady
                            }
                        }
                    },
                    onCalibrateLean = {
                        val rawLean = rawLeanDegrees
                        if (rawLean == null) {
                            setupStatusMessage = copy.leanWaiting
                        } else {
                            leanBaselineDegrees = rawLean
                            currentLeanDegrees = 0f
                            maxLeftLeanDegrees = 0f
                            maxRightLeanDegrees = 0f
                            setupStatusMessage = copy.leanCalibrated
                        }
                    },
                    onToggleTelemetry = {
                        telemetryEnabled = !telemetryEnabled
                        setupStatusMessage = if (!telemetryEnabled) {
                            copy.telemetryEnabled
                        } else {
                            copy.telemetryDisabled
                        }
                    },
                    setupStatusMessage = setupStatusMessage,
                    onSelectTrack = { selectedTrack = it },
                    onGoLive = { activeScreen = Screen.Live },
                )
                Screen.Live -> LiveScreen(
                    copy = copy,
                    snapshot = liveSnapshot,
                    locationPermissionGranted = locationPermissionGranted,
                    sessionMode = sessionMode,
                    onRequestLocationPermission = {
                        locationPermissionLauncher.launch(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION,
                            ),
                        )
                    },
                    onMarkLap = {
                        lapTimingState = lapTimingEngine.markManualLap().also { updatedState ->
                            updatedState.bestLapMillis?.let { bestLapMillis ->
                                store.saveBestLapMillis(selectedTrack.id, bestLapMillis)
                            }
                        }
                    },
                    onTogglePause = {
                        if (sessionMode == SessionMode.PAUSED) {
                            lapTimingState = lapTimingEngine.resume()
                            sessionMode = SessionMode.RUNNING
                        } else if (sessionMode == SessionMode.RUNNING) {
                            lapTimingState = lapTimingEngine.pause()
                            sessionMode = SessionMode.PAUSED
                        }
                    },
                    onEndSession = {
                        val endedState = lapTimingEngine.end()
                        lapTimingState = endedState
                        lastTelemetryExportFile = telemetryExporter.exportCsv(selectedTrack, telemetrySamples)
                        store.saveLastSession(
                            track = selectedTrack,
                            totalLaps = endedState.totalLaps,
                            bestLapMillis = endedState.bestLapMillis,
                            lastLapMillis = endedState.lastLapMillis,
                            maxLeftLeanDegrees = maxLeftLeanDegrees,
                            maxRightLeanDegrees = maxRightLeanDegrees,
                        )
                        sessionMode = SessionMode.ENDED
                        activeScreen = Screen.Summary
                    },
                )
                Screen.Summary -> SummaryScreen(
                    copy = copy,
                    selectedTrack = selectedTrack,
                    snapshot = liveSnapshot,
                    telemetryStatus = telemetryStatusLabel,
                    lastTelemetryExportFile = lastTelemetryExportFile,
                    onGoSetup = { activeScreen = Screen.Setup },
                    onGoLive = { activeScreen = Screen.Live },
                    onExportTelemetry = {
                        val exportFile = lastTelemetryExportFile
                            ?: telemetryExporter.exportCsv(selectedTrack, telemetrySamples)
                        if (exportFile != null) {
                            lastTelemetryExportFile = exportFile
                            context.shareTelemetryCsv(exportFile)
                        }
                    },
                )
            }
        }
    }
}

@Composable
private fun TopControlsBar(
    copy: NativeCopy,
    orientationMode: OrientationMode,
    onCloseApp: () -> Unit,
    onToggleLanguage: () -> Unit,
    onCycleOrientation: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
        Button(
            onClick = onCloseApp,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF1E2D38),
                contentColor = Color.White,
            ),
        ) {
            Icon(Icons.Outlined.PowerSettingsNew, contentDescription = null)
            Text(text = " ${copy.closeAppButton}")
        }
        SecondaryAction(
            label = copy.languageButton,
            onClick = onToggleLanguage,
            modifier = Modifier,
        )
        Button(
            onClick = onCycleOrientation,
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFECE4D7),
                contentColor = Color(0xFF1D1B19),
            ),
        ) {
            Icon(Icons.Outlined.ScreenRotation, contentDescription = null)
            Text(
                text = " " + when (orientationMode) {
                    OrientationMode.AUTO -> copy.orientationAuto
                    OrientationMode.LANDSCAPE -> copy.orientationLandscape
                    OrientationMode.PORTRAIT -> copy.orientationPortrait
                },
            )
        }
    }
}

@Composable
private fun HomeScreen(
    copy: NativeCopy,
    onGoSetup: () -> Unit,
    onGoLive: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroCard(
                eyebrow = copy.homeEyebrow,
                title = copy.homeTitle,
                subtitle = copy.homeSubtitle,
            )
        }
        item {
            CardBlock(title = copy.nextStepTitle, subtitle = copy.nextStepSubtitle) {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrimaryAction(label = copy.openTrackSetup, onClick = onGoSetup)
                    SecondaryAction(label = copy.openLiveCockpit, onClick = onGoLive)
                }
            }
        }
    }
}

@Composable
private fun SetupScreen(
    copy: NativeCopy,
    nearbyTrack: TrackPreset?,
    presets: List<TrackPreset>,
    manualTrack: TrackPreset?,
    selectedTrack: TrackPreset,
    locationPermissionGranted: Boolean,
    locationStatus: String,
    telemetryEnabled: Boolean,
    onRequestLocationPermission: () -> Unit,
    onSetManualStartPoint: () -> Unit,
    onCalibrateLean: () -> Unit,
    onToggleTelemetry: () -> Unit,
    setupStatusMessage: String?,
    onSelectTrack: (TrackPreset) -> Unit,
    onGoLive: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroCard(
                eyebrow = copy.setupEyebrow,
                title = copy.setupTitle,
                subtitle = copy.setupSubtitle,
            )
        }
        item {
            CardBlock(title = copy.nearbyTitle, subtitle = copy.nearbySubtitle) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (nearbyTrack == null) {
                        Text(copy.noNearbyTrack, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (locationPermissionGranted) locationStatus else copy.gpsPermissionNeeded,
                            color = Color(0xFF5F5A52),
                        )
                    } else {
                        Text(nearbyTrack.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(nearbyTrack.markerLabel, color = Color(0xFF5F5A52))
                        Text(nearbyTrack.distanceLabel ?: "--", color = Color(0xFF345F49), fontWeight = FontWeight.Bold)
                    }
                    if (!locationPermissionGranted) {
                        SecondaryAction(label = copy.requestGpsPermission, onClick = onRequestLocationPermission)
                    }
                    if (nearbyTrack != null) {
                        PrimaryAction(label = copy.useSuggestedTrack, onClick = { onSelectTrack(nearbyTrack) })
                    }
                    SecondaryAction(label = copy.setManualStartPoint, onClick = onSetManualStartPoint)
                    SecondaryAction(label = copy.calibrateLean, onClick = onCalibrateLean)
                    SecondaryAction(
                        label = if (telemetryEnabled) copy.telemetryOn else copy.telemetryOff,
                        onClick = onToggleTelemetry,
                    )
                    if (setupStatusMessage != null) {
                        Text(setupStatusMessage, color = Color(0xFF345F49), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        item {
            CardBlock(title = copy.trackLibraryTitle, subtitle = copy.trackLibrarySubtitle) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (manualTrack != null) {
                        SelectableTrackCard(
                            track = manualTrack,
                            selected = manualTrack.id == selectedTrack.id,
                            onSelect = { onSelectTrack(manualTrack) },
                        )
                    }
                    presets.forEach { preset ->
                        SelectableTrackCard(
                            track = preset,
                            selected = preset.id == selectedTrack.id,
                            onSelect = { onSelectTrack(preset) },
                        )
                    }
                }
            }
        }
        item {
            PrimaryAction(label = copy.goToLiveCockpit, onClick = onGoLive)
        }
    }
}

@Composable
private fun BottomBar(
    copy: NativeCopy,
    activeScreen: Screen,
    onSelect: (Screen) -> Unit,
) {
    Card(
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EF)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            BottomBarButton(
                modifier = Modifier.weight(1f),
                label = copy.navHome,
                selected = activeScreen == Screen.Home,
                onClick = { onSelect(Screen.Home) },
                icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
            )
            BottomBarButton(
                modifier = Modifier.weight(1f),
                label = copy.navSetup,
                selected = activeScreen == Screen.Setup,
                onClick = { onSelect(Screen.Setup) },
                icon = { Icon(Icons.Outlined.Map, contentDescription = null) },
            )
            BottomBarButton(
                modifier = Modifier.weight(1f),
                label = copy.navLive,
                selected = activeScreen == Screen.Live,
                onClick = { onSelect(Screen.Live) },
                icon = { Icon(Icons.Outlined.Speed, contentDescription = null) },
            )
            BottomBarButton(
                modifier = Modifier.weight(1f),
                label = copy.navSummary,
                selected = activeScreen == Screen.Summary,
                onClick = { onSelect(Screen.Summary) },
                icon = { Icon(Icons.Outlined.Dashboard, contentDescription = null) },
            )
        }
    }
}

@Composable
private fun BottomBarButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
) {
    val containerColor = if (selected) Color(0xFF1E2D38) else Color(0xFFECE4D7)
    val contentColor = if (selected) Color.White else Color(0xFF1D1B19)

    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
        ),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            icon()
            Text(label, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun LiveScreen(
    copy: NativeCopy,
    snapshot: LiveSessionSnapshot,
    locationPermissionGranted: Boolean,
    sessionMode: SessionMode,
    onRequestLocationPermission: () -> Unit,
    onMarkLap: () -> Unit,
    onTogglePause: () -> Unit,
    onEndSession: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = copy.gpsLabel,
                value = snapshot.gpsStatus,
            )
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = copy.liveSpeed,
                value = snapshot.speedLabel,
            )
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = copy.liveLean,
                value = snapshot.leanCurrentLabel,
            )
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = copy.liveTotalLaps,
                value = snapshot.totalLaps.toString(),
            )
        }

        Row(
            modifier = Modifier.weight(1f),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Card(
                modifier = Modifier.weight(1.4f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2D38)),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(copy.liveCurrentLap, color = Color(0xFFEAD8B7), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(
                        text = snapshot.currentLap,
                        color = Color.White,
                        fontSize = 50.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 54.sp,
                    )
                }
            }

            Card(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(28.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (snapshot.currentDelta.startsWith("+")) Color(0xFFF2C5B5) else Color(0xFFCFE6D4),
                ),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(22.dp),
                    verticalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(copy.liveDelta, color = Color(0xFF5F5A52), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(
                        text = snapshot.currentDelta,
                        color = Color(0xFF1D1B19),
                        fontSize = 36.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 40.sp,
                    )
                    Text("${copy.liveBestLap} ${snapshot.bestLap}", color = Color(0xFF5F5A52))
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricChip(modifier = Modifier.weight(1f), label = copy.liveLastLap, value = snapshot.lastLap, tone = Color(0xFFDFB392))
            MetricChip(modifier = Modifier.weight(1f), label = copy.liveBestLap, value = snapshot.bestLap, tone = Color(0xFFB9D6BD))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccentAction(label = copy.liveLapMark, onClick = onMarkLap, modifier = Modifier.weight(1f))
            if (!locationPermissionGranted) {
                SecondaryAction(label = copy.requestGpsPermission, onClick = onRequestLocationPermission, modifier = Modifier.weight(1f))
            }
            SecondaryAction(
                label = if (sessionMode == SessionMode.PAUSED) copy.liveResume else copy.livePause,
                onClick = onTogglePause,
                modifier = Modifier.weight(1f),
            )
            SecondaryAction(label = copy.liveEnd, onClick = onEndSession, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryScreen(
    copy: NativeCopy,
    selectedTrack: TrackPreset,
    snapshot: LiveSessionSnapshot,
    telemetryStatus: String,
    lastTelemetryExportFile: File?,
    onGoSetup: () -> Unit,
    onGoLive: () -> Unit,
    onExportTelemetry: () -> Unit,
) {
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
    ) {
        val isWide = maxWidth > maxHeight
        if (isWide) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SummaryHeaderCard(
                    modifier = Modifier.weight(1f),
                    copy = copy,
                    selectedTrack = selectedTrack,
                    snapshot = snapshot,
                    telemetryStatus = telemetryStatus,
                    lastTelemetryExportFile = lastTelemetryExportFile,
                    onGoSetup = onGoSetup,
                    onGoLive = onGoLive,
                    onExportTelemetry = onExportTelemetry,
                )
                SummaryStatsGrid(
                    modifier = Modifier.weight(1.2f),
                    copy = copy,
                    snapshot = snapshot,
                    stacked = false,
                )
            }
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SummaryHeaderCard(
                    modifier = Modifier.weight(0.9f),
                    copy = copy,
                    selectedTrack = selectedTrack,
                    snapshot = snapshot,
                    telemetryStatus = telemetryStatus,
                    lastTelemetryExportFile = lastTelemetryExportFile,
                    onGoSetup = onGoSetup,
                    onGoLive = onGoLive,
                    onExportTelemetry = onExportTelemetry,
                )
                SummaryStatsGrid(
                    modifier = Modifier.weight(1.1f),
                    copy = copy,
                    snapshot = snapshot,
                    stacked = true,
                )
            }
        }
    }
}

@Composable
private fun SummaryHeaderCard(
    modifier: Modifier = Modifier,
    copy: NativeCopy,
    selectedTrack: TrackPreset,
    snapshot: LiveSessionSnapshot,
    telemetryStatus: String,
    lastTelemetryExportFile: File?,
    onGoSetup: () -> Unit,
    onGoLive: () -> Unit,
    onExportTelemetry: () -> Unit,
) {
    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EF)),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(copy.summaryEyebrow.uppercase(), color = Color(0xFF345F49), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(copy.navSummary, color = Color(0xFF1D1B19), fontSize = 34.sp, fontWeight = FontWeight.Black, lineHeight = 36.sp)
                Text(selectedTrack.name, color = Color(0xFF5F5A52), fontWeight = FontWeight.Bold)
                Text(selectedTrack.markerLabel, color = Color(0xFF5F5A52), fontSize = 13.sp, lineHeight = 16.sp)
                Text(snapshot.gpsStatus, color = Color(0xFF345F49), fontSize = 13.sp, lineHeight = 16.sp)
                Text("${copy.telemetrySamples}: $telemetryStatus", color = Color(0xFF5F5A52), fontSize = 13.sp, lineHeight = 16.sp)
                if (lastTelemetryExportFile != null) {
                    Text(copy.telemetryExported, color = Color(0xFF345F49), fontSize = 13.sp, lineHeight = 16.sp)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SecondaryAction(label = copy.navSetup, onClick = onGoSetup, modifier = Modifier.weight(1f))
                PrimaryAction(label = copy.navLive, onClick = onGoLive, modifier = Modifier.weight(1f))
                SecondaryAction(label = copy.exportTelemetry, onClick = onExportTelemetry, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryStatsGrid(
    modifier: Modifier = Modifier,
    copy: NativeCopy,
    snapshot: LiveSessionSnapshot,
    stacked: Boolean,
) {
    if (stacked) {
        Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SummaryStatCard(modifier = Modifier.weight(1f), label = copy.liveBestLap, value = snapshot.bestLap, tone = Color(0xFFB9D6BD))
                SummaryStatCard(modifier = Modifier.weight(1f), label = copy.liveLastLap, value = snapshot.lastLap, tone = Color(0xFFDFB392))
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                SummaryStatCard(modifier = Modifier.weight(1f), label = copy.liveTotalLaps, value = snapshot.totalLaps.toString(), tone = Color(0xFFECE4D7))
                SummaryStatCard(modifier = Modifier.weight(1f), label = copy.leanLeft, value = snapshot.leanLeftLabel, tone = Color(0xFFD5E5F0))
                SummaryStatCard(modifier = Modifier.weight(1f), label = copy.leanRight, value = snapshot.leanRightLabel, tone = Color(0xFFF1D0B7))
            }
        }
    } else {
        Row(
            modifier = modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            SummaryStatCard(modifier = Modifier.weight(1f), label = copy.liveBestLap, value = snapshot.bestLap, tone = Color(0xFFB9D6BD))
            SummaryStatCard(modifier = Modifier.weight(1f), label = copy.liveLastLap, value = snapshot.lastLap, tone = Color(0xFFDFB392))
            SummaryStatCard(modifier = Modifier.weight(1f), label = copy.liveTotalLaps, value = snapshot.totalLaps.toString(), tone = Color(0xFFECE4D7))
            SummaryStatCard(modifier = Modifier.weight(1f), label = copy.leanLeft, value = snapshot.leanLeftLabel, tone = Color(0xFFD5E5F0))
            SummaryStatCard(modifier = Modifier.weight(1f), label = copy.leanRight, value = snapshot.leanRightLabel, tone = Color(0xFFF1D0B7))
        }
    }
}

@Composable
private fun SummaryStatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    tone: Color,
) {
    Card(
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(26.dp),
        colors = CardDefaults.cardColors(containerColor = tone),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF1D1B19), letterSpacing = 1.sp)
            Text(value, fontWeight = FontWeight.Black, fontSize = 34.sp, lineHeight = 36.sp)
        }
    }
}

@Composable
private fun HeroCard(
    eyebrow: String,
    title: String,
    subtitle: String,
) {
    CardBlock(title = eyebrow, subtitle = subtitle) {
        Text(
            text = title,
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Black,
            lineHeight = 42.sp,
        )
    }
}

@Composable
private fun CardBlock(
    title: String,
    subtitle: String,
    content: @Composable () -> Unit,
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EF)),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(title.uppercase(), color = Color(0xFF345F49), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                Text(subtitle, color = Color(0xFF5F5A52))
            }
            content()
        }
    }
}

@Composable
private fun SelectableTrackCard(
    track: TrackPreset,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    val background = if (selected) Color(0xFFECE4D7) else Color(0xFFF4EFE4)
    val border = if (selected) Color(0xFFC65D3B) else Color.Transparent

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = BorderStroke(2.dp, border),
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(track.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(track.country, color = Color(0xFF5F5A52))
            Text(track.markerLabel, color = Color(0xFF5F5A52))
        }
    }
}

@Composable
private fun CompactHeaderCard(
    modifier: Modifier = Modifier,
    title: String,
    value: String,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF9EF)),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(title, color = Color(0xFF5F5A52), fontWeight = FontWeight.Bold)
            Text(value, color = Color(0xFF1D1B19), fontWeight = FontWeight.Black)
        }
    }
}

@Composable
private fun MetricChip(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    tone: Color,
) {
    Card(
        modifier = modifier.height(76.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = tone),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(label, fontWeight = FontWeight.Bold, color = Color(0xFF1D1B19))
            Text(value, fontWeight = FontWeight.Black, fontSize = 24.sp)
        }
    }
}

@Composable
private fun PrimaryAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth(),
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2D38)),
        modifier = modifier,
    ) {
        Text(label, modifier = Modifier.padding(vertical = 4.dp), textAlign = TextAlign.Center)
    }
}

@Composable
private fun SecondaryAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFECE4D7), contentColor = Color(0xFF1D1B19)),
        modifier = modifier,
    ) {
        Text(label, modifier = Modifier.padding(vertical = 4.dp), textAlign = TextAlign.Center)
    }
}

@Composable
private fun AccentAction(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC65D3B)),
        modifier = modifier,
    ) {
        Text(label, modifier = Modifier.padding(vertical = 4.dp), textAlign = TextAlign.Center)
    }
}

private fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

private fun Context.hasLocationPermission(): Boolean {
    return checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
        checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

private fun Context.shareTelemetryCsv(file: File) {
    val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/csv"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    startActivity(Intent.createChooser(shareIntent, "Telemetry CSV exportieren"))
}

private fun CurrentPosition.formatCoordinates(): String {
    return String.format(java.util.Locale.US, "%.5f, %.5f", latitude, longitude)
}

private fun CurrentPosition.hasManualStartAccuracy(): Boolean {
    val accuracy = accuracyMeters ?: return false
    return accuracy <= 20f
}

private fun Float?.formatLean(): String {
    if (this == null) {
        return "--°"
    }
    return "${abs(this).toInt()}°"
}

private fun Long?.formatLapTime(): String {
    if (this == null) {
        return "--:--.--"
    }

    val totalCentiseconds = this / 10L
    val minutes = totalCentiseconds / 6_000L
    val seconds = (totalCentiseconds % 6_000L) / 100L
    val centiseconds = totalCentiseconds % 100L
    return String.format(java.util.Locale.US, "%02d:%02d.%02d", minutes, seconds, centiseconds)
}

private fun Long?.formatDelta(): String {
    if (this == null) {
        return "--:--.--"
    }

    val sign = if (this >= 0L) "+" else "-"
    val absoluteMillis = kotlin.math.abs(this)
    return "$sign${absoluteMillis.formatLapTime()}"
}
