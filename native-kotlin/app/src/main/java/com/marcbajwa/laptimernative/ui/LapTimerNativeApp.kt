package com.marcbajwa.laptimernative.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.pm.ActivityInfo
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import com.marcbajwa.laptimernative.data.TrackRepository
import com.marcbajwa.laptimernative.model.LiveSessionSnapshot
import com.marcbajwa.laptimernative.model.Screen
import com.marcbajwa.laptimernative.model.TrackPreset

private enum class AppLanguage {
    DE,
    EN,
}

private enum class OrientationMode {
    AUTO,
    LANDSCAPE,
    PORTRAIT,
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
    val liveBestLap: String,
    val liveLastLap: String,
    val liveTotalLaps: String,
    val liveLapMark: String,
    val livePause: String,
    val liveEnd: String,
    val gpsLabel: String,
    val summaryEyebrow: String,
    val summaryTitle: String,
    val summarySubtitle: String,
    val selectedTrackTitle: String,
    val selectedTrackSubtitle: String,
    val languageButton: String,
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
    liveBestLap = "BESTE RUNDE",
    liveLastLap = "LETZTE RUNDE",
    liveTotalLaps = "RUNDEN",
    liveLapMark = "Lap markieren",
    livePause = "Pause",
    liveEnd = "Ende",
    gpsLabel = "GPS",
    summaryEyebrow = "Session-Auswertung",
    summaryTitle = "Die native Auswertung bleibt schnell lesbar",
    summarySubtitle = "Dieser Screen wird das Kotlin-Zuhause fuer Runden, Referenz-Qualitaet und Export-Aktionen.",
    selectedTrackTitle = "Aktive Strecke",
    selectedTrackSubtitle = "Aktuelles Ziel des nativen Umbaus",
    languageButton = "English",
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
    liveBestLap = "BEST LAP",
    liveLastLap = "LAST LAP",
    liveTotalLaps = "LAPS",
    liveLapMark = "Mark lap",
    livePause = "Pause",
    liveEnd = "End",
    gpsLabel = "GPS",
    summaryEyebrow = "Session Summary",
    summaryTitle = "Native summary will stay quick to scan",
    summarySubtitle = "This screen becomes the Kotlin home for laps, reference run health, and export actions.",
    selectedTrackTitle = "Selected track",
    selectedTrackSubtitle = "Current native migration target",
    languageButton = "Deutsch",
    orientationAuto = "Auto",
    orientationLandscape = "Wide",
    orientationPortrait = "Tall",
)

@Composable
fun LapTimerNativeApp() {
    val context = LocalContext.current
    var activeScreen by remember { mutableStateOf(Screen.Home) }
    var selectedTrack by remember { mutableStateOf(TrackRepository.nearbySuggestion) }
    var language by remember { mutableStateOf(AppLanguage.DE) }
    var orientationMode by remember { mutableStateOf(OrientationMode.LANDSCAPE) }

    val copy = if (language == AppLanguage.DE) germanCopy else englishCopy

    LaunchedEffect(orientationMode) {
        context.findActivity()?.requestedOrientation = when (orientationMode) {
            OrientationMode.AUTO -> ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            OrientationMode.LANDSCAPE -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
            OrientationMode.PORTRAIT -> ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT
        }
    }

    Scaffold(
        containerColor = Color(0xFFF4EFE4),
        topBar = {
            TopControlsBar(
                copy = copy,
                orientationMode = orientationMode,
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
        },
        bottomBar = {
            BottomBar(
                copy = copy,
                activeScreen = activeScreen,
                onSelect = { activeScreen = it },
            )
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
                    nearbyTrack = TrackRepository.nearbySuggestion,
                    presets = TrackRepository.presets,
                    selectedTrack = selectedTrack,
                    onSelectTrack = { selectedTrack = it },
                    onGoLive = { activeScreen = Screen.Live },
                )
                Screen.Live -> LiveScreen(copy = copy, snapshot = TrackRepository.liveSnapshot)
                Screen.Summary -> SummaryScreen(copy = copy, selectedTrack = selectedTrack)
            }
        }
    }
}

@Composable
private fun TopControlsBar(
    copy: NativeCopy,
    orientationMode: OrientationMode,
    onToggleLanguage: () -> Unit,
    onCycleOrientation: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End),
    ) {
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
    nearbyTrack: TrackPreset,
    presets: List<TrackPreset>,
    selectedTrack: TrackPreset,
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
                    Text(nearbyTrack.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(nearbyTrack.markerLabel, color = Color(0xFF5F5A52))
                    Text(nearbyTrack.distanceLabel ?: "--", color = Color(0xFF345F49), fontWeight = FontWeight.Bold)
                    PrimaryAction(label = copy.useSuggestedTrack, onClick = { onSelectTrack(nearbyTrack) })
                    SecondaryAction(label = copy.setManualStartPoint, onClick = {})
                }
            }
        }
        item {
            CardBlock(title = copy.trackLibraryTitle, subtitle = copy.trackLibrarySubtitle) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
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
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = copy.gpsLabel,
                value = snapshot.gpsStatus,
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
                        fontSize = 58.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 62.sp,
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
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 46.sp,
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
            AccentAction(label = copy.liveLapMark, modifier = Modifier.weight(1f))
            SecondaryAction(label = copy.livePause, onClick = {}, modifier = Modifier.weight(1f))
            SecondaryAction(label = copy.liveEnd, onClick = {}, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun SummaryScreen(
    copy: NativeCopy,
    selectedTrack: TrackPreset,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroCard(
                eyebrow = copy.summaryEyebrow,
                title = copy.summaryTitle,
                subtitle = copy.summarySubtitle,
            )
        }
        item {
            CardBlock(title = copy.selectedTrackTitle, subtitle = copy.selectedTrackSubtitle) {
                Text(selectedTrack.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(selectedTrack.markerLabel, color = Color(0xFF5F5A52))
            }
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
            modifier = Modifier.padding(16.dp),
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
        modifier = modifier.height(96.dp),
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
            Text(value, fontWeight = FontWeight.Black, fontSize = 28.sp)
        }
    }
}

@Composable
private fun PrimaryAction(
    label: String,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E2D38)),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(label, modifier = Modifier.padding(vertical = 6.dp), textAlign = TextAlign.Center)
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
        Text(label, modifier = Modifier.padding(vertical = 6.dp), textAlign = TextAlign.Center)
    }
}

@Composable
private fun AccentAction(
    label: String,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = {},
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC65D3B)),
        modifier = modifier,
    ) {
        Text(label, modifier = Modifier.padding(vertical = 6.dp), textAlign = TextAlign.Center)
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
