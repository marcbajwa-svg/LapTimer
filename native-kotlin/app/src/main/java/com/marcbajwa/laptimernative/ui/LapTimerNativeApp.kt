package com.marcbajwa.laptimernative.ui

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
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marcbajwa.laptimernative.data.TrackRepository
import com.marcbajwa.laptimernative.model.LiveSessionSnapshot
import com.marcbajwa.laptimernative.model.Screen
import com.marcbajwa.laptimernative.model.TrackPreset

@Composable
fun LapTimerNativeApp() {
    var activeScreen by remember { mutableStateOf(Screen.Home) }
    var selectedTrack by remember { mutableStateOf(TrackRepository.nearbySuggestion) }

    Scaffold(
        containerColor = Color(0xFFF4EFE4),
        bottomBar = {
            NavigationBar(containerColor = Color(0xFFFFF9EF)) {
                BottomNavigationItem(
                    screen = Screen.Home,
                    activeScreen = activeScreen,
                    label = "Start",
                    onSelect = { activeScreen = Screen.Home },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                )
                BottomNavigationItem(
                    screen = Screen.Setup,
                    activeScreen = activeScreen,
                    label = "Setup",
                    onSelect = { activeScreen = Screen.Setup },
                    icon = { Icon(Icons.Outlined.Map, contentDescription = null) },
                )
                BottomNavigationItem(
                    screen = Screen.Live,
                    activeScreen = activeScreen,
                    label = "Live",
                    onSelect = { activeScreen = Screen.Live },
                    icon = { Icon(Icons.Outlined.Speed, contentDescription = null) },
                )
                BottomNavigationItem(
                    screen = Screen.Summary,
                    activeScreen = activeScreen,
                    label = "Auswertung",
                    onSelect = { activeScreen = Screen.Summary },
                    icon = { Icon(Icons.Outlined.Dashboard, contentDescription = null) },
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
                    onGoSetup = { activeScreen = Screen.Setup },
                    onGoLive = { activeScreen = Screen.Live },
                )
                Screen.Setup -> SetupScreen(
                    nearbyTrack = TrackRepository.nearbySuggestion,
                    presets = TrackRepository.presets,
                    selectedTrack = selectedTrack,
                    onSelectTrack = { selectedTrack = it },
                    onGoLive = { activeScreen = Screen.Live },
                )
                Screen.Live -> LiveScreen(snapshot = TrackRepository.liveSnapshot)
                Screen.Summary -> SummaryScreen(selectedTrack = selectedTrack)
            }
        }
    }
}

@Composable
private fun BottomNavigationItem(
    screen: Screen,
    activeScreen: Screen,
    label: String,
    onSelect: () -> Unit,
    icon: @Composable () -> Unit,
) {
    NavigationBarItem(
        selected = screen == activeScreen,
        onClick = onSelect,
        icon = icon,
        label = { Text(label) },
    )
}

@Composable
private fun HomeScreen(
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
                eyebrow = "LapTimer Native",
                title = "Kotlin rebuild for a real Android track tool",
                subtitle = "This native version keeps the product flow from the Expo prototype, but moves us into an Android-first architecture with Compose.",
            )
        }
        item {
            CardBlock(title = "Next Step", subtitle = "Get the setup right before you roll out.") {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    PrimaryAction("Track setup oeffnen", onGoSetup)
                    SecondaryAction("Live cockpit ansehen", onGoLive)
                }
            }
        }
    }
}

@Composable
private fun SetupScreen(
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
                eyebrow = "Track Setup",
                title = "Suggest the nearest circuit first",
                subtitle = "The native rebuild should immediately surface the likely circuit from GPS, then fall back to a manual start point when nothing matches nearby.",
            )
        }
        item {
            CardBlock(title = "Nearby suggestion", subtitle = "This becomes the first decision in the Android setup flow.") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(nearbyTrack.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                    Text(nearbyTrack.markerLabel, color = Color(0xFF5F5A52))
                    Text(nearbyTrack.distanceLabel ?: "--", color = Color(0xFF345F49), fontWeight = FontWeight.Bold)
                    PrimaryAction("Use suggested track") { onSelectTrack(nearbyTrack) }
                    SecondaryAction("Set manual start point") {}
                }
            }
        }
        item {
            CardBlock(title = "Track library", subtitle = "Curated presets stay useful even after GPS suggestions land.") {
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
            PrimaryAction("Go to live cockpit", onGoLive)
        }
    }
}

@Composable
private fun LiveScreen(snapshot: LiveSessionSnapshot) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = "GPS",
                value = snapshot.gpsStatus,
            )
            CompactHeaderCard(
                modifier = Modifier.weight(1f),
                title = "Laps",
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
                    Text("AKTUELLE RUNDE", color = Color(0xFFEAD8B7), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
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
                    Text("DELTA ZUR BESTZEIT", color = Color(0xFF5F5A52), fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
                    Text(
                        text = snapshot.currentDelta,
                        color = Color(0xFF1D1B19),
                        fontSize = 42.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 46.sp,
                    )
                    Text("Best lap ${snapshot.bestLap}", color = Color(0xFF5F5A52))
                }
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            MetricChip(modifier = Modifier.weight(1f), label = "LAST LAP", value = snapshot.lastLap, tone = Color(0xFFDFB392))
            MetricChip(modifier = Modifier.weight(1f), label = "BEST LAP", value = snapshot.bestLap, tone = Color(0xFFB9D6BD))
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AccentAction(modifier = Modifier.weight(1f), label = "Lap markieren")
            SecondaryAction(modifier = Modifier.weight(1f), label = "Pause") {}
            SecondaryAction(modifier = Modifier.weight(1f), label = "Ende") {}
        }
    }
}

@Composable
private fun SummaryScreen(selectedTrack: TrackPreset) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            HeroCard(
                eyebrow = "Session Summary",
                title = "Native summary will stay quick to scan",
                subtitle = "This screen becomes the Kotlin home for laps, reference run health, and export actions.",
            )
        }
        item {
            CardBlock(title = "Selected track", subtitle = "Current native migration target") {
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
        modifier = Modifier.fillMaxWidth(),
        onClick = onSelect,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = background),
        border = androidx.compose.foundation.BorderStroke(2.dp, border),
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
private fun PrimaryAction(label: String, onClick: () -> Unit) {
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
