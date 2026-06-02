package vaibhav.all.apps.launcher

import android.content.Context
import android.content.Intent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.CornerRadius
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.input.key.*
import android.view.KeyEvent
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import android.net.Uri
import android.widget.Toast
import vaibhav.all.apps.launcher.ui.theme.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.absoluteValue

// DATA MODEL representation
data class AppItem(
    val id: String,
    val name: String,
    val packageName: String,
    val isSystem: Boolean,
    val brandColor: Color = Color.Gray,
    val description: String = "",
    val category: String = "Streaming",
    val rating: String = "4.5 ⭐",
    val specs: List<String> = listOf("UHD", "HDR", "Stereo"),
    val mockShow: String = "Featured Live Stream",
    val launchIntent: Intent? = null,
    val iconDrawable: Drawable? = null
)

data class CalendarFields(
    val hour: Int,
    val minute: Int,
    val day: Int,
    val month: Int,
    val year: Int
)

// List of customized curated streaming applications mimicking a real Fire Stick experience
val CuratedApps = listOf(
    AppItem(
        id = "prime_video",
        name = "Prime Video",
        packageName = "com.amazon.amazonvideo.livingroom",
        isSystem = false,
        brandColor = Color(0xFF00A8E1),
        description = "Stream Prime Originals, Amazon Exclusives, and award-winning blockbusters. Includes live sports and rich 4K HDR Atmos soundtracks.",
        category = "Streaming",
        rating = "4.9 ⭐",
        specs = listOf("UHD", "HDR10+", "Dolby Atmos"),
        mockShow = "Featured: The Lord of the Rings: The Rings of Power"
    ),
    AppItem(
        id = "netflix",
        name = "Netflix",
        packageName = "com.netflix.mediaclient",
        isSystem = false,
        brandColor = Color(0xFFE50914),
        description = "Unlimited streaming of blockbuster hits, critically acclaimed series, and stunning anime. Multiple cinematic audio presets available.",
        category = "Streaming",
        rating = "4.8 ⭐",
        specs = listOf("UHD", "Dolby Vision", "5.1 Digital"),
        mockShow = "Season 5: Stranger Things (Now Streaming Worldwide)"
    ),
    AppItem(
        id = "youtube",
        name = "YouTube TV",
        packageName = "com.google.android.youtube.tv",
        isSystem = false,
        brandColor = Color(0xFFFF0000),
        description = "Instant gateway to a universe of user creations, musical performances, gaming livestreams, and hot news coverage from around the globe.",
        category = "Social Media",
        rating = "4.7 ⭐",
        specs = listOf("4K", "Stereo Feed", "LIVE Streams"),
        mockShow = "Broadcast: NASA Deep-Space ISS Earth Orbit Camera"
    ),
    AppItem(
        id = "disneyplus",
        name = "Disney+",
        packageName = "com.disney.disneyplus",
        isSystem = false,
        brandColor = Color(0xFF113CCF),
        description = "Unlock the marvelous vault of Disney stories, Pixar classics, Marvel warfare, Star Wars sager templates, and breathtaking National Geographic explorations.",
        category = "Streaming",
        rating = "4.8 ⭐",
        specs = listOf("UHD", "HDR", "Dolby Vision"),
        mockShow = "Saga Launch: Star Wars - The Mandalorian Season 3"
    ),
    AppItem(
        id = "max",
        name = "HBO Max",
        packageName = "com.hbo.hbonow",
        isSystem = false,
        brandColor = Color(0xFF9933FF),
        description = "The absolute premium home of iconic HBO series, Warner Bros. box-office hits, and mind-bending epic original narratives.",
        category = "Streaming",
        rating = "4.6 ⭐",
        specs = listOf("UHD", "HDR", "Dolby Atmos"),
        mockShow = "Drama Peak: House of the Dragon Season 3 (Pre-Release)"
    ),
    AppItem(
        id = "twitch",
        name = "Twitch",
        packageName = "tv.twitch.android.app",
        isSystem = false,
        brandColor = Color(0xFF9146FF),
        description = "Connect with gaming communities, live streams, developer hangouts, artistic design workspaces, and esports global matches in real-time.",
        category = "Live Streaming",
        rating = "4.5 ⭐",
        specs = listOf("FHD", "60 FPS", "Live Interaction"),
        mockShow = "Championships: Pro League Esports Tournament Mainstage"
    ),
    AppItem(
        id = "apple_tv",
        name = "Apple TV+",
        packageName = "com.apple.atve.amazon.appletv",
        isSystem = false,
        brandColor = Color(0xFF1E1E24),
        description = "Home to critically praised Apple Originals, edge-of-your-seat psychological thrillers, and Friday night double-header baseball games.",
        category = "Streaming",
        rating = "4.6 ⭐",
        specs = listOf("UHD", "Dolby Vision", "Atmos"),
        mockShow = "Mystery Thriller: Severance Season 2 (Trending Now)"
    ),
    AppItem(
        id = "spotify",
        name = "Spotify Music",
        packageName = "com.spotify.tv.android",
        isSystem = false,
        brandColor = Color(0xFF1DB954),
        description = "Turn your living room screen into a majestic music canvas. Browse millions of sync-scrolling lyrics and ambient audio podcasts.",
        category = "Music & Podcasts",
        rating = "4.7 ⭐",
        specs = listOf("Hi-Fi", "Synced Lyrics", "Background Audio"),
        mockShow = "Cozen Playlist: Midnight Lofi Mix for Night Studies"
    ),
    AppItem(
        id = "pluto_tv",
        name = "Pluto TV",
        packageName = "mx.pluto.tv",
        isSystem = false,
        brandColor = Color(0xFFFFA000),
        description = "The leading free television network. Surf hundreds of thematic live entertainment channels and thousands of popular on-demand options.",
        category = "Live TV",
        rating = "4.4 ⭐",
        specs = listOf("100% Free", "SD/HD Channels", "Electronic Guide"),
        mockShow = "Classic TV: CSI Miami & Drama Live Broadcast channels"
    )
)

// APP CATEGORY AUTOMATIC DETECTION UTILITY
object AppCategoryClassifier {
    fun getCategory(packageName: String, label: String, appInfo: android.content.pm.ApplicationInfo?): String {
        if (appInfo != null) {
            // Check flags for game
            val isGame = (appInfo.flags and android.content.pm.ApplicationInfo.FLAG_IS_GAME) != 0
            if (isGame) return "Games"

            // API 26+ Application Category check
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                try {
                    val cat = appInfo.category
                    val mapped = when (cat) {
                        android.content.pm.ApplicationInfo.CATEGORY_GAME -> "Games"
                        android.content.pm.ApplicationInfo.CATEGORY_AUDIO -> "Entertainment"
                        android.content.pm.ApplicationInfo.CATEGORY_VIDEO -> "Entertainment"
                        android.content.pm.ApplicationInfo.CATEGORY_IMAGE -> "Entertainment"
                        android.content.pm.ApplicationInfo.CATEGORY_SOCIAL -> "Social"
                        android.content.pm.ApplicationInfo.CATEGORY_NEWS -> "Entertainment"
                        android.content.pm.ApplicationInfo.CATEGORY_MAPS -> "Utilities"
                        android.content.pm.ApplicationInfo.CATEGORY_PRODUCTIVITY -> "Productivity"
                        // Accessibility is 8 in API 31+
                        8 -> "Utilities"
                        else -> null
                    }
                    if (mapped != null) return mapped
                } catch (t: Throwable) {
                    // Fallback on any runtime linkage/compat issues
                }
            }
        }

        // Exact match overrides or standard heuristics
        val pkg = packageName.lowercase()
        val name = label.lowercase()

        // Match Games
        if (pkg.contains("game") || pkg.contains("arcade") || pkg.contains("playgames") ||
            name.contains("game") || name.contains("arcade") || name.contains("puzzle") ||
            pkg.contains("retroarch") || pkg.contains("steam") || pkg.contains("xbox")
        ) {
            return "Games"
        }

        // Match Productivity
        if (pkg.contains("productivity") || pkg.contains("office") || pkg.contains("word") ||
            pkg.contains("excel") || pkg.contains("sheet") || pkg.contains("slide") ||
            pkg.contains("calendar") || pkg.contains("mail") || pkg.contains("gmail") ||
            pkg.contains("notes") || pkg.contains("document") || pkg.contains("pdf") ||
            name.contains("office") || name.contains("word") || name.contains("excel") ||
            name.contains("sheet") || name.contains("slide") || name.contains("calendar") ||
            name.contains("notes") || name.contains("mail") || name.contains("pdf") ||
            pkg.contains("editor") || name.contains("editor") || pkg.contains("print")
        ) {
            return "Productivity"
        }

        // Match Social
        if (pkg.contains("social") || pkg.contains("chat") || pkg.contains("messenger") ||
            pkg.contains("whatsapp") || pkg.contains("instagram") || pkg.contains("facebook") ||
            pkg.contains("twitter") || pkg.contains("discord") || pkg.contains("reddit") ||
            pkg.contains("linkedin") || pkg.contains("tiktok") || pkg.contains("snapchat") ||
            pkg.contains("telegram") || name.contains("chat") || name.contains("messenger") ||
            name.contains("social") || name.contains("whatsapp") || name.contains("hangouts") ||
            name.contains("contacts") || pkg.contains("telephony") || pkg.contains("contact")
        ) {
            return "Social"
        }

        // Match Entertainment / Streaming
        if (pkg.contains("netflix") || pkg.contains("youtube") || pkg.contains("disney") ||
            pkg.contains("hbo") || pkg.contains("spotify") || pkg.contains("player") ||
            pkg.contains("video") || pkg.contains("tv") || pkg.contains("music") ||
            pkg.contains("prime") || pkg.contains("twitch") || pkg.contains("stream") ||
            pkg.contains("cinema") || pkg.contains("movie") || pkg.contains("audio") ||
            pkg.contains("sound") || pkg.contains("gallery") || pkg.contains("photos") ||
            name.contains("netflix") || name.contains("youtube") || name.contains("player") ||
            name.contains("tv") || name.contains("video") || name.contains("music") ||
            name.contains("twitch") || name.contains("spotify") ||
            name.contains("stream") || name.contains("cinema") || name.contains("movie") ||
            name.contains("gallery") || name.contains("photos") || name.contains("podcast")
        ) {
            return "Entertainment"
        }

        // Match Utilities & Tools
        if (pkg.contains("settings") || pkg.contains("system") || pkg.contains("tool") ||
            pkg.contains("utility") || pkg.contains("manager") || pkg.contains("calculator") ||
            pkg.contains("clock") || pkg.contains("weather") || pkg.contains("map") ||
            pkg.contains("navigation") || pkg.contains("files") || pkg.contains("download") ||
            pkg.contains("installer") || pkg.contains("backup") || pkg.contains("cleaner") ||
            pkg.contains("assistant") || pkg.contains("chrome") || pkg.contains("browser") ||
            pkg.contains("vending") || pkg.contains("launcher") || pkg.contains("provider") ||
            pkg.contains("search") || name.contains("settings") || name.contains("clock") ||
            name.contains("calculator") || name.contains("weather") || name.contains("map") ||
            name.contains("files") || name.contains("browser") || name.contains("cleaner") ||
            name.contains("tool") || name.contains("system") || name.contains("launcher") ||
            name.contains("updater") || name.contains("manager")
        ) {
            return "Utilities"
        }

        return "Other"
    }
}

private val SPECS_GAMES = listOf("INTERACTIVE", "LOCAL GAME", "60 FPS")
private val SPECS_PRODUCTIVITY = listOf("WORKSPACE", "PRODUCTIVITY", "CLOUD DATA")
private val SPECS_SOCIAL = listOf("SOCIAL", "MESSAGING", "COMMUNITY")
private val SPECS_ENTERTAINMENT = listOf("CINEMATIC", "MEDIA FEED", "UHD/HD")
private val SPECS_UTILITIES = listOf("CORE UTILITY", "SYSTEM RUNTIME")
private val SPECS_OTHER = listOf("DEVICE SERVICE", "LOCAL SHORTCUT")

// STATE CONTROLLER ViewModel
class FireAppsViewModel(private val context: Context) : ViewModel() {
    private val sharedPrefs = context.getSharedPreferences("fire_apps_preferences", Context.MODE_PRIVATE)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    private val _selectedTab = MutableStateFlow("All Apps")
    val selectedTab = _selectedTab.asStateFlow()

    private val _systemApps = MutableStateFlow<List<AppItem>>(emptyList())
    val systemApps = _systemApps.asStateFlow()

    private val _favorites = MutableStateFlow<Set<String>>(emptySet())
    val favorites = _favorites.asStateFlow()

    private val _selectedApp = MutableStateFlow<AppItem?>(null)
    val selectedApp = _selectedApp.asStateFlow()

    private val _isStreamingFeedActive = MutableStateFlow(false)
    val isStreamingFeedActive = _isStreamingFeedActive.asStateFlow()

    // Customizable Clock & Date States
    private val _useSystemTime = MutableStateFlow(true)
    val useSystemTime = _useSystemTime.asStateFlow()

    private val _timeFormat24h = MutableStateFlow(false)
    val timeFormat24h = _timeFormat24h.asStateFlow()

    private val _timeOffset = MutableStateFlow(0L)
    val timeOffset = _timeOffset.asStateFlow()

    fun setUseSystemTime(value: Boolean) {
        _useSystemTime.value = value
        if (value) {
            _timeOffset.value = 0L
        }
    }

    fun setTimeFormat24h(value: Boolean) {
        _timeFormat24h.value = value
    }

    fun setCustomDateTime(hour: Int, minute: Int, day: Int, month: Int, year: Int) {
        try {
            val calendar = java.util.Calendar.getInstance()
            calendar.set(java.util.Calendar.YEAR, year)
            calendar.set(java.util.Calendar.MONTH, month - 1)
            calendar.set(java.util.Calendar.DAY_OF_MONTH, day)
            calendar.set(java.util.Calendar.HOUR_OF_DAY, hour)
            calendar.set(java.util.Calendar.MINUTE, minute)
            calendar.set(java.util.Calendar.SECOND, 0)
            calendar.set(java.util.Calendar.MILLISECOND, 0)

            val targetMillis = calendar.timeInMillis
            _timeOffset.value = targetMillis - System.currentTimeMillis()
            _useSystemTime.value = false
        } catch (e: Exception) {
            // Fallback
        }
    }

    fun getCalendarFields(): CalendarFields {
        val millis = System.currentTimeMillis() + if (_useSystemTime.value) 0L else _timeOffset.value
        val cal = java.util.Calendar.getInstance()
        cal.timeInMillis = millis
        return CalendarFields(
            hour = cal.get(java.util.Calendar.HOUR_OF_DAY),
            minute = cal.get(java.util.Calendar.MINUTE),
            day = cal.get(java.util.Calendar.DAY_OF_MONTH),
            month = cal.get(java.util.Calendar.MONTH) + 1, // 1-based
            year = cal.get(java.util.Calendar.YEAR)
        )
    }

    init {
        loadFavorites()
        loadSystemApps()
    }

    private fun loadFavorites() {
        val saved = sharedPrefs.getStringSet("pinned_packages", emptySet()) ?: emptySet()
        _favorites.value = saved
    }

    fun toggleFavorite(packageName: String) {
        val current = _favorites.value.toMutableSet()
        if (current.contains(packageName)) {
            current.remove(packageName)
        } else {
            current.add(packageName)
        }
        sharedPrefs.edit().putStringSet("pinned_packages", current).apply()
        _favorites.value = current
    }

    private fun getCategoryAestheticColor(category: String, name: String): Color {
        val colors = when (category) {
            "Games" -> listOf(Color(0xFFE74C3C), Color(0xFFC0392B), Color(0xFFD35400))
            "Productivity" -> listOf(Color(0xFF2ECC71), Color(0xFF27AE60), Color(0xFF16A085))
            "Social" -> listOf(Color(0xFF3498DB), Color(0xFF2980B9), Color(0xFF1F3A60))
            "Entertainment" -> listOf(Color(0xFF9B59B6), Color(0xFF8E44AD), Color(0xFF663399))
            "Utilities" -> listOf(Color(0xFFF1C40F), Color(0xFFF39C12), Color(0xFFD35400))
            else -> listOf(Color(0xFF7F8C8D), Color(0xFF95A5A6), Color(0xFF34495E))
        }
        val safeHash = name.hashCode().toLong() and 0xFFFFFFFFL
        val index = (safeHash % colors.size).toInt()
        return colors[index]
    }

    private fun getSavedAppsOrder(): List<String> {
        val savedString = sharedPrefs.getString("apps_order_list", "") ?: ""
        if (savedString.isEmpty()) return emptyList()
        return savedString.split(",")
    }

    private fun saveAppsOrder(order: List<String>) {
        val s = order.joinToString(",")
        sharedPrefs.edit().putString("apps_order_list", s).apply()
    }

    fun moveApp(packageName: String, direction: Int) {
        val currentList = _systemApps.value.toMutableList()
        val index = currentList.indexOfFirst { it.packageName == packageName }
        if (index == -1) return

        val targetIndex = index + direction
        if (targetIndex in 0 until currentList.size) {
            // Swap
            val temp = currentList[index]
            currentList[index] = currentList[targetIndex]
            currentList[targetIndex] = temp

            // Save new order
            val newOrder = currentList.map { it.packageName }
            saveAppsOrder(newOrder)

            _systemApps.value = currentList
        }
    }

    private fun loadSystemApps() {
        viewModelScope.launch {
            val apps = withContext(Dispatchers.IO) {
                val pm = context.packageManager
                val intent = Intent(Intent.ACTION_MAIN, null).apply {
                    addCategory(Intent.CATEGORY_LAUNCHER)
                }
                val resolveInfos = pm.queryIntentActivities(intent, 0) ?: emptyList()
                val parsed = resolveInfos.mapNotNull { info ->
                    val activityInfo = info?.activityInfo ?: return@mapNotNull null
                    val pName = activityInfo.packageName ?: return@mapNotNull null
                    if (pName == context.packageName) return@mapNotNull null
                    val name = info.loadLabel(pm)?.toString() ?: pName
                    val icon = info.loadIcon(pm)
                    val launchIntent = Intent(Intent.ACTION_MAIN).apply {
                        addCategory(Intent.CATEGORY_LAUNCHER)
                        component = android.content.ComponentName(pName, activityInfo.name)
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                    }
                    val appInfo = activityInfo.applicationInfo
                    
                    val detectedCategory = AppCategoryClassifier.getCategory(pName, name, appInfo)
                    
                    val specs = when (detectedCategory) {
                        "Games" -> SPECS_GAMES
                        "Productivity" -> SPECS_PRODUCTIVITY
                        "Social" -> SPECS_SOCIAL
                        "Entertainment" -> SPECS_ENTERTAINMENT
                        "Utilities" -> SPECS_UTILITIES
                        else -> SPECS_OTHER
                    }
                    val description = when (detectedCategory) {
                        "Games" -> "Immersive gameplay application installed on this device. Start playing with zero buffering."
                        "Productivity" -> "Productivity workspace tool for managing documents, tasks, and notes on your cinematic driver."
                        "Social" -> "Communicate, message, and stay connected with your social circles directly from your TV layout."
                        "Entertainment" -> "High quality video, music, or media stream application. Cinema mode output fully supported."
                        "Utilities" -> "System helper or core administration tool to keep your hardware running smoothly."
                        else -> "Local system device application launch helper. Tap to open package directly."
                    }
                    val rating = when (detectedCategory) {
                        "Games" -> "4.7 ⭐"
                        "Productivity" -> "4.5 ⭐"
                        "Social" -> "4.6 ⭐"
                        "Entertainment" -> "4.8 ⭐"
                        "Utilities" -> "4.4 ⭐"
                        else -> "4.2 ⭐"
                    }

                    AppItem(
                        id = pName,
                        name = name,
                        packageName = pName,
                        isSystem = true,
                        brandColor = getCategoryAestheticColor(detectedCategory, name),
                        description = description,
                        category = detectedCategory,
                        rating = rating,
                        specs = specs,
                        mockShow = "Package Reference Name: $pName",
                        launchIntent = launchIntent,
                        iconDrawable = icon
                    )
                }.distinctBy { it.packageName }

                val savedOrder = getSavedAppsOrder()
                if (savedOrder.isEmpty()) {
                    parsed.sortedBy { it.name.lowercase() }
                } else {
                    parsed.sortedWith(compareBy { app ->
                        val idx = savedOrder.indexOf(app.packageName)
                        if (idx != -1) idx else Int.MAX_VALUE
                    })
                }
            }
            _systemApps.value = apps
        }
    }

    fun selectApp(appItem: AppItem?) {
        _selectedApp.value = appItem
    }

    fun selectTab(tab: String) {
        _selectedTab.value = tab
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStreamingFeed(active: Boolean) {
        _isStreamingFeedActive.value = active
    }

}

// MAIN ACTIVITY GATEWAY
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val context = LocalContext.current
                val viewModel: FireAppsViewModel = viewModel(
                    factory = object : androidx.lifecycle.ViewModelProvider.Factory {
                        @Suppress("UNCHECKED_CAST")
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            return FireAppsViewModel(context.applicationContext) as T
                        }
                    }
                )
                FireAppsDashboard(viewModel)
            }
        }
    }
}

// CLOCK TICK GENERATOR WITH SMOOTH COROUTINE COUPLING
@Composable
fun rememberCurrentTimeAndDate(
    useSystemTime: Boolean,
    timeOffset: Long,
    is24h: Boolean
): Pair<String, String> {
    var tick by remember { mutableStateOf(0L) }

    LaunchedEffect(useSystemTime, timeOffset) {
        while (true) {
            kotlinx.coroutines.delay(1000L)
            tick++
        }
    }

    val timeFormat = remember(is24h) {
        val timePattern = if (is24h) "HH:mm" else "hh:mm a"
        java.text.SimpleDateFormat(timePattern, java.util.Locale.getDefault())
    }

    val dateFormat = remember {
        val datePattern = "EEEE, MMMM dd, yyyy"
        java.text.SimpleDateFormat(datePattern, java.util.Locale.getDefault())
    }

    return remember(tick, useSystemTime, timeOffset, is24h) {
        val currentTimeMillis = System.currentTimeMillis() + if (useSystemTime) 0L else timeOffset
        val date = java.util.Date(currentTimeMillis)

        val timeStr = timeFormat.format(date)
        val dateStr = dateFormat.format(date)

        Pair(timeStr, dateStr)
    }
}

// DESIGN COMPLIANT TV CLOCK WITH SEAMLESS INTERACTION
@Composable
fun MinimalistTopClock(
    viewModel: FireAppsViewModel,
    isFocusable: Boolean = true,
    onClockClick: () -> Unit
) {
    val useSystemTime by viewModel.useSystemTime.collectAsState()
    val timeOffset by viewModel.timeOffset.collectAsState()
    val is24h by viewModel.timeFormat24h.collectAsState()

    val (timeStr, dateStr) = rememberCurrentTimeAndDate(
        useSystemTime = useSystemTime,
        timeOffset = timeOffset,
        is24h = is24h
    )

    var isFocused by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .onFocusChanged { isFocused = it.isFocused }
            .focusable(enabled = isFocusable)
            .clickable(enabled = isFocusable) { onClockClick() }
            .border(
                width = if (isFocused) 1.5.dp else 1.dp,
                color = if (isFocused) Color.White else Color(0x1FFFFFFF),
                shape = RoundedCornerShape(12.dp)
            )
            .background(
                color = if (isFocused) Color(0x11FFFFFF) else Color(0x05FFFFFF),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 14.dp, vertical = 8.dp),
        horizontalAlignment = Alignment.End
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange,
                contentDescription = "Date and time",
                tint = if (isFocused) Color.White else FireOrangePrimary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = timeStr,
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = dateStr,
            color = if (isFocused) Color.White else FireTextSecondary,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

// TV CUSTOMIZER DIALOG OVERLAY WITH SEAMLESS D-PAD CLICKS
@Composable
fun CalendarTimeCustomizerOverlay(
    viewModel: FireAppsViewModel,
    onClose: () -> Unit
) {
    val useSystemTime by viewModel.useSystemTime.collectAsState()
    val is24h by viewModel.timeFormat24h.collectAsState()

    var fields by remember { mutableStateOf(viewModel.getCalendarFields()) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        // Automatically request focus on TV remote open to prevent background scrolling
        try {
            focusRequester.requestFocus()
        } catch (e: Exception) {}
    }

    LaunchedEffect(useSystemTime) {
        if (useSystemTime) {
            fields = viewModel.getCalendarFields()
        }
    }

    val updateTime = { h: Int, m: Int ->
        fields = fields.copy(hour = (h + 24) % 24, minute = (m + 60) % 60)
        viewModel.setCustomDateTime(fields.hour, fields.minute, fields.day, fields.month, fields.year)
    }

    val updateDate = { d: Int, mo: Int, y: Int ->
        val safeMonth = ((mo - 1 + 12) % 12) + 1
        val safeYear = y.coerceIn(1970, 2100)
        fields = fields.copy(day = d.coerceIn(1, 31), month = safeMonth, year = safeYear)
        viewModel.setCustomDateTime(fields.hour, fields.minute, fields.day, fields.month, fields.year)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(enabled = true) { /* Intercept click backdrop */ }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141A24)),
            border = BorderStroke(1.5.dp, Color(0xFF2C3E50)),
            modifier = Modifier
                .widthIn(max = 440.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Time and date settings",
                            tint = FireOrangePrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Text(
                            text = "Time & Date Settings",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    var isCloseFocused by remember { mutableStateOf(false) }
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .focusRequester(focusRequester)
                            .onFocusChanged { isCloseFocused = it.isFocused }
                            .background(
                                color = if (isCloseFocused) Color(0x33FFFFFF) else Color.Transparent,
                                shape = CircleShape
                            )
                            .border(
                                width = if (isCloseFocused) 1.5.dp else 0.dp,
                                color = Color.White,
                                shape = CircleShape
                            )
                    ) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                HorizontalDivider(color = Color(0x33FFFFFF), thickness = 1.dp)

                // 24 Hour Toggle Format Row
                var is24hRowFocused by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { is24hRowFocused = it.isFocused }
                        .focusable()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.setTimeFormat24h(!is24h) }
                        .border(
                            width = if (is24hRowFocused) 1.5.dp else 0.dp,
                            color = if (is24hRowFocused) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(if (is24hRowFocused) Color(0x1AFFFFFF) else Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("24-Hour Time Format", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Toggle between 12-hour and 24-hour style", color = FireTextSecondary, fontSize = 10.sp)
                    }
                    Switch(
                        checked = is24h,
                        onCheckedChange = { viewModel.setTimeFormat24h(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FireOrangePrimary,
                            checkedTrackColor = FireOrangePrimary.copy(alpha = 0.4f)
                        )
                    )
                }

                // Auto System Time Toggle Format Row
                var isAutoTimeRowFocused by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isAutoTimeRowFocused = it.isFocused }
                        .focusable()
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { viewModel.setUseSystemTime(!useSystemTime) }
                        .border(
                            width = if (isAutoTimeRowFocused) 1.5.dp else 0.dp,
                            color = if (isAutoTimeRowFocused) Color.White else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(if (isAutoTimeRowFocused) Color(0x1AFFFFFF) else Color.Transparent)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Network Auto Sync Time", color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Text("Sync clock with system background driver", color = FireTextSecondary, fontSize = 10.sp)
                    }
                    Switch(
                        checked = useSystemTime,
                        onCheckedChange = { viewModel.setUseSystemTime(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = FireOrangePrimary,
                            checkedTrackColor = FireOrangePrimary.copy(alpha = 0.4f)
                        )
                    )
                }

                if (!useSystemTime) {
                    HorizontalDivider(color = Color(0x1AFFFFFF), thickness = 1.dp)

                    Text(
                        text = "MANUAL CALENDAR SETTINGS",
                        color = FireOrangePrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    AdjustmentRow(
                        label = "Hours (0-23)",
                        value = fields.hour.toString().padStart(2, '0'),
                        onDecrement = { updateTime(fields.hour - 1, fields.minute) },
                        onIncrement = { updateTime(fields.hour + 1, fields.minute) }
                    )

                    AdjustmentRow(
                        label = "Minutes (0-59)",
                        value = fields.minute.toString().padStart(2, '0'),
                        onDecrement = { updateTime(fields.hour, fields.minute - 1) },
                        onIncrement = { updateTime(fields.hour, fields.minute + 1) }
                    )

                    AdjustmentRow(
                        label = "Day of Month",
                        value = fields.day.toString().padStart(2, '0'),
                        onDecrement = { updateDate(fields.day - 1, fields.month, fields.year) },
                        onIncrement = { updateDate(fields.day + 1, fields.month, fields.year) }
                    )

                    val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                    AdjustmentRow(
                        label = "Month",
                        value = monthNames.getOrNull(fields.month - 1) ?: fields.month.toString(),
                        onDecrement = { updateDate(fields.day, fields.month - 1, fields.year) },
                        onIncrement = { updateDate(fields.day, fields.month + 1, fields.year) }
                    )

                    AdjustmentRow(
                        label = "Year",
                        value = fields.year.toString(),
                        onDecrement = { updateDate(fields.day, fields.month, fields.year - 1) },
                        onIncrement = { updateDate(fields.day, fields.month, fields.year + 1) }
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                var isApplyFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isApplyFocused = it.isFocused }
                        .border(
                            width = if (isApplyFocused) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(100.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isApplyFocused) Color.White else FireOrangePrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Apply & Exit Settings", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }
    }
}

@Composable
fun AdjustmentRow(
    label: String,
    value: String,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            var isDecFocused by remember { mutableStateOf(false) }
            FilledIconButton(
                onClick = onDecrement,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isDecFocused) Color.White else Color(0xFF2C3E50),
                    contentColor = if (isDecFocused) Color.Black else Color.White
                ),
                modifier = Modifier
                    .size(32.dp)
                    .onFocusChanged { isDecFocused = it.isFocused }
                    .border(
                        width = if (isDecFocused) 1.5.dp else 0.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowLeft, contentDescription = "Decrement", modifier = Modifier.size(18.dp))
            }
            Box(
                modifier = Modifier.width(54.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                )
            }
            var isIncFocused by remember { mutableStateOf(false) }
            FilledIconButton(
                onClick = onIncrement,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = if (isIncFocused) Color.White else Color(0xFF2C3E50),
                    contentColor = if (isIncFocused) Color.Black else Color.White
                ),
                modifier = Modifier
                    .size(32.dp)
                    .onFocusChanged { isIncFocused = it.isFocused }
                    .border(
                        width = if (isIncFocused) 1.5.dp else 0.dp,
                        color = Color.White,
                        shape = CircleShape
                    )
            ) {
                Icon(imageVector = Icons.Default.KeyboardArrowRight, contentDescription = "Increment", modifier = Modifier.size(18.dp))
            }
        }
    }
}

// SLEEK, MODERN REMOTE-FRIENDLY OPTIONS OVERLAY (NO UGLY NAVIGATION BUTTONS)
@Composable
fun AppOptionsDialog(
    appItem: AppItem,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onStartRearranging: () -> Unit,
    onClose: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        try {
            focusRequester.requestFocus()
        } catch (e: Exception) {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(enabled = true) { onClose() }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF141A24)),
            border = BorderStroke(1.5.dp, FireOrangePrimary.copy(alpha = 0.6f)),
            modifier = Modifier
                .widthIn(max = 380.dp)
                .fillMaxWidth()
                .clickable(enabled = true, onClick = {}) // Intercept click inside card
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header with app name
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFF101416), CircleShape)
                            .border(1.dp, Color(0x33FFFFFF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        AppIcon(app = appItem, modifier = Modifier.fillMaxSize())
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = appItem.name,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Launcher Options",
                            color = FireTextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    IconButton(onClick = onClose) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                HorizontalDivider(color = Color(0x1BFFFFFF), thickness = 1.dp)

                // Option 1: Favorite Toggle Button
                var isFavoriteFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        onToggleFavorite()
                    },
                    modifier = Modifier
                        .focusRequester(focusRequester) // Set initial focus here
                        .fillMaxWidth()
                        .onFocusChanged { isFavoriteFocused = it.isFocused }
                        .border(
                            width = if (isFavoriteFocused) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(100.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isFavoriteFocused) Color.White else if (isFavorite) Color(0xFF2C3E50) else Color(0xFFE74C3C),
                        contentColor = if (isFavoriteFocused) Color.Black else Color.White
                    )
                ) {
                    val label = if (isFavorite) "Remove from Favorites" else "Add to Favorites"
                    val iconVector = if (isFavorite) Icons.Default.FavoriteBorder else Icons.Default.Favorite
                    Icon(
                        imageVector = iconVector,
                        contentDescription = label,
                        modifier = Modifier.padding(end = 8.dp).size(16.dp)
                    )
                    Text(text = label, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Option 2: Rearrange / Move Button
                var isRearrangeFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = {
                        onStartRearranging()
                        onClose()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isRearrangeFocused = it.isFocused }
                        .border(
                            width = if (isRearrangeFocused) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(100.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isRearrangeFocused) Color.White else Color(0xFF2E7D32),
                        contentColor = if (isRearrangeFocused) Color.Black else Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Move app",
                        modifier = Modifier.padding(end = 8.dp).size(16.dp)
                    )
                    Text(text = "Move / Rearrange App", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                HorizontalDivider(color = Color(0x11FFFFFF), thickness = 1.dp)

                // Done Button
                var isDoneFocused by remember { mutableStateOf(false) }
                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .onFocusChanged { isDoneFocused = it.isFocused }
                        .border(
                            width = if (isDoneFocused) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(100.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isDoneFocused) Color.White else FireOrangePrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Close Options", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }
    }
}

// PRIMARY JETPACK COMPOSE COMPONENT
@Composable
fun FireAppsDashboard(viewModel: FireAppsViewModel) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedTab by viewModel.selectedTab.collectAsState()
    val systemApps by viewModel.systemApps.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val selectedApp by viewModel.selectedApp.collectAsState()
    val isStreamingActive by viewModel.isStreamingFeedActive.collectAsState()

    var isDeveloperInfoOpen by remember { mutableStateOf(false) }
    var isCustomizerOpen by remember { mutableStateOf(false) }
    var appToEdit by remember { mutableStateOf<AppItem?>(null) }
    var rearrangingApp by remember { mutableStateOf<AppItem?>(null) }

    // Direct app launching logic with 1-click fallback
    val launchAppDirectly = { app: AppItem ->
        if (app.launchIntent != null) {
            try {
                context.startActivity(app.launchIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "Could not launch ${app.name}", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Launch intent not available for ${app.name}", Toast.LENGTH_SHORT).show()
        }
    }

    val filteredSystem = remember(searchQuery, systemApps) {
        if (searchQuery.isBlank()) {
            systemApps
        } else {
            systemApps.filter {
                NativeStringMatcher.containsIgnoreCase(it.name, searchQuery) ||
                        NativeStringMatcher.containsIgnoreCase(it.packageName, searchQuery)
            }
        }
    }

    val allApps = filteredSystem
    val chunkedApps = remember(allApps) { allApps.chunked(8) }

    // FAVORITE APPS ROW (ONLY SHOWS IF NOT EMPTY, COOPERATES WITH REAL-TIME FACTION LISTS)
    val favApps = remember(favorites, systemApps) {
        systemApps.filter { favorites.contains(it.packageName) }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // SCROLLABLE INTERFACE LAYOUT WITH CLIPTOPADDING = FALSE TO AVOID CUTTING OFF FOCUSED FONT DEVIATIONS
        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->
            LazyColumn(
                contentPadding = PaddingValues(
                    top = paddingValues.calculateTopPadding() + 20.dp,
                    bottom = paddingValues.calculateBottomPadding() + 180.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // TV CENTERED TITLE AND ACTION ROW (MATCHING USER REDESIGN IMAGE)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp)
                    ) {
                        // Top-Left Navigation Buttons (Glass Design)
                        Row(
                            modifier = Modifier.align(Alignment.CenterStart),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            var isBackFocused by remember { mutableStateOf(false) }
                            var isHomeFocused by remember { mutableStateOf(false) }
                            
                            // Back Button
                            IconButton(
                                onClick = { 
                                    // Simulate back button press - closes dialogs or exits app
                                    (context as? ComponentActivity)?.onBackPressedDispatcher?.onBackPressed()
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .onFocusChanged { isBackFocused = it.isFocused }
                                    .focusable(enabled = !isCustomizerOpen && !isDeveloperInfoOpen && appToEdit == null && rearrangingApp == null)
                                    .border(
                                        width = if (isBackFocused) 1.5.dp else 1.dp,
                                        color = if (isBackFocused) Color.White else Color(0x1FFFFFFF),
                                        shape = CircleShape
                                    )
                                    .background(
                                        color = if (isBackFocused) Color(0x11FFFFFF) else Color(0x05FFFFFF),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Back",
                                    tint = if (isBackFocused) Color.White else FireOrangePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            
                            // Home Button - Returns to launcher home (closes any open dialogs/overlays)
                            IconButton(
                                onClick = { 
                                    // Close all overlays and return to main launcher view
                                    isDeveloperInfoOpen = false
                                    isCustomizerOpen = false
                                    appToEdit = null
                                    rearrangingApp = null
                                },
                                modifier = Modifier
                                    .size(36.dp)
                                    .onFocusChanged { isHomeFocused = it.isFocused }
                                    .focusable(enabled = !isCustomizerOpen && !isDeveloperInfoOpen && appToEdit == null && rearrangingApp == null)
                                    .border(
                                        width = if (isHomeFocused) 1.5.dp else 1.dp,
                                        color = if (isHomeFocused) Color.White else Color(0x1FFFFFFF),
                                        shape = CircleShape
                                    )
                                    .background(
                                        color = if (isHomeFocused) Color(0x11FFFFFF) else Color(0x05FFFFFF),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Home,
                                    contentDescription = "Home",
                                    tint = if (isHomeFocused) Color.White else FireOrangePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                        
                        // Centered "Your apps" Title
                        Text(
                            text = "Your apps",
                            color = Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.align(Alignment.Center)
                        )

                        // Top-Right Discrete Remote-friendly actions
                        Row(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            var isInfoFocused by remember { mutableStateOf(false) }
                            IconButton(
                                onClick = { isDeveloperInfoOpen = true },
                                modifier = Modifier
                                    .size(36.dp)
                                    .onFocusChanged { isInfoFocused = it.isFocused }
                                    .focusable(enabled = !isCustomizerOpen && !isDeveloperInfoOpen && appToEdit == null && rearrangingApp == null)
                                    .border(
                                        width = if (isInfoFocused) 1.5.dp else 1.dp,
                                        color = if (isInfoFocused) Color.White else Color(0x1FFFFFFF),
                                        shape = CircleShape
                                    )
                                    .background(
                                        color = if (isInfoFocused) Color(0x11FFFFFF) else Color(0x05FFFFFF),
                                        shape = CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Developer Info",
                                    tint = if (isInfoFocused) Color.White else FireOrangePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            MinimalistTopClock(
                                viewModel = viewModel,
                                isFocusable = !isCustomizerOpen && !isDeveloperInfoOpen && appToEdit == null && rearrangingApp == null
                            ) {
                                isCustomizerOpen = true
                            }
                        }
                    }
                }

                if (favApps.isNotEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Favorite Apps",
                                color = FireOrangePrimary,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.SansSerif
                            )
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(favApps, key = { it.packageName }) { app ->
                                    val isPinned = favorites.contains(app.packageName)
                                    AppCircularHubCard(
                                        appItem = app,
                                        isPinned = isPinned,
                                        onClick = { launchAppDirectly(app) },
                                        onLongClick = { appToEdit = app },
                                        isFocusable = !isCustomizerOpen && !isDeveloperInfoOpen && appToEdit == null && rearrangingApp == null,
                                        rearrangingApp = rearrangingApp,
                                        onMoveApp = { offset -> viewModel.moveApp(app.packageName, offset) },
                                        onFinishRearranging = { rearrangingApp = null }
                                    )
                                }
                            }
                        }
                    }

                    item {
                        Text(
                            text = "All Apps",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.SansSerif,
                            modifier = Modifier.padding(horizontal = 24.dp, vertical = 4.dp)
                        )
                    }
                }

                // UNIFIED FLAT GRID of all INSTALLED apps (8 columns to perfectly fit TV screen width)

                if (allApps.isEmpty()) {
                    item {
                        EmptyStatePanel(searchQuery) {
                            viewModel.updateSearchQuery("")
                        }
                    }
                } else {
                    items(
                        items = chunkedApps,
                        key = { rowApps -> rowApps.joinToString(separator = "|") { app -> app.packageName } }
                    ) { rowApps ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowApps.forEach { app ->
                                val isPinned = favorites.contains(app.packageName)
                                AppCircularHubCard(
                                    appItem = app,
                                    isPinned = isPinned,
                                    onClick = { launchAppDirectly(app) },
                                    onLongClick = { appToEdit = app },
                                    isFocusable = !isCustomizerOpen && !isDeveloperInfoOpen && appToEdit == null && (rearrangingApp == null || rearrangingApp?.packageName == app.packageName),
                                    rearrangingApp = rearrangingApp,
                                    onMoveApp = { offset -> viewModel.moveApp(app.packageName, offset) },
                                    onFinishRearranging = { rearrangingApp = null }
                                )
                            }
                        }
                    }
                }
            }
        }

        // FLOATING REARRANGE INSTRUCTION BANNER (REPLACES UGLY ON-SCREEN ARROWS)
        if (rearrangingApp != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFD84315)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                    border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.6f)),
                    modifier = Modifier.align(Alignment.Center)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Move mode active",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                        Column {
                            Text(
                                text = "MOVE MODE ACTIVE: ${rearrangingApp!!.name}",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Use TV remote navigation arrows (Up, Down, Left, Right) to rearrange this app, then press OK/Select or Back to exit.",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }

        // FULL SCREEN COMPONENT: IMMERSIVE STREAM TRAILER SIMULATOR
        AnimatedVisibility(
            visible = isStreamingActive,
            enter = fadeIn() + scaleIn(initialScale = 0.9f),
            exit = fadeOut() + scaleOut(targetScale = 0.9f)
        ) {
            selectedApp?.let { app ->
                CinemaFeedPlayer(
                    app = app,
                    onClose = {
                        viewModel.setStreamingFeed(false)
                        viewModel.selectApp(null)
                    }
                )
            }
        }

        if (isCustomizerOpen) {
            CalendarTimeCustomizerOverlay(viewModel = viewModel) {
                isCustomizerOpen = false
            }
        }

        if (isDeveloperInfoOpen) {
            DeveloperInfoOverlay {
                isDeveloperInfoOpen = false
            }
        }

        if (appToEdit != null) {
            val app = appToEdit!!
            val isPinned = favorites.contains(app.packageName)
            AppOptionsDialog(
                appItem = app,
                isFavorite = isPinned,
                onToggleFavorite = { viewModel.toggleFavorite(app.packageName) },
                onStartRearranging = { rearrangingApp = app },
                onClose = { appToEdit = null }
            )
        }
    }
}

// VAIBHAV DEVELOPER INFO DIALOG OVERLAY (FULLY DPAD FRIENDLY)
@Composable
fun DeveloperInfoOverlay(
    onClose: () -> Unit
) {
    val context = LocalContext.current
    var isCloseFocused by remember { mutableStateOf(false) }
    var isTelegramFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        // Automatically request D-pad focus on TV remote open to prevent D-pad background leak
        try {
            focusRequester.requestFocus()
        } catch (e: Exception) {}
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.85f))
            .clickable(enabled = true) { /* Intercept back click backdrop */ }
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF101416)),
            border = BorderStroke(1.5.dp, FireOrangePrimary.copy(alpha = 0.6f)),
            modifier = Modifier
                .widthIn(max = 400.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0x1BFF6400), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Launcher information",
                        tint = FireOrangePrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = "Launcher Info",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "This dashboard launcher is completely optimized and styled for Android TV systems, providing direct application execution with high-contrast active icons highlighting.",
                    color = FireTextSecondary,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp
                )

                HorizontalDivider(color = Color(0x33FFFFFF), thickness = 1.dp)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "DEVELOPED BY",
                        color = FireOrangePrimary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Vaibhav",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "TELEGRAM CHANNEL / SUPPORT",
                        color = FireTextSecondary,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "t.me/BlackDex",
                        color = FireAmberAccent,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .onFocusChanged { isTelegramFocused = it.isFocused }
                            .focusable()
                            .border(
                                width = if (isTelegramFocused) 2.dp else 1.dp,
                                color = if (isTelegramFocused) Color.White else FireOrangePrimary.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = if (isTelegramFocused) Color(0x22FFFFFF) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable {
                                try {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/BlackDex"))
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "t.me/BlackDex", Toast.LENGTH_SHORT).show()
                                }
                            }
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Button(
                    onClick = onClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                        .onFocusChanged { isCloseFocused = it.isFocused }
                        .border(
                            width = if (isCloseFocused) 2.dp else 0.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(100.dp)
                        ),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCloseFocused) Color.White else FireOrangePrimary,
                        contentColor = Color.Black
                    )
                ) {
                    Text("Dismiss", fontWeight = FontWeight.Black, fontSize = 13.sp)
                }
            }
        }
    }
}

// CINEMATIC BILLBOARD BANNER HERO CARD
@Composable
fun CinemaBillboard(appItem: AppItem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
            .padding(horizontal = 20.dp)
            .shadow(16.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(appItem.brandColor.copy(alpha = 0.25f))
            .clickable { onClick() }
    ) {
        // Gradient Brush Overlays
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.linearGradient(
                    colors = listOf(
                        appItem.brandColor.copy(alpha = 0.85f),
                        Color(0xBB000000),
                        Color(0xFF0C0F13)
                    )
                )
            )
        }

        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .background(Color(0x33FFFFFF), RoundedCornerShape(4.dp))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = appItem.category.uppercase(),
                                color = Color.White,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (appItem.isSystem) "Installed Application" else "Streaming Direct Hub+",
                            color = FireAmberAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = appItem.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = appItem.description,
                        color = FireTextSecondary,
                        fontSize = 12.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        lineHeight = 16.sp
                    )
                }

                Column {
                    Text(
                        text = appItem.mockShow,
                        color = FireTextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = appItem.rating,
                            color = FireAmberAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        appItem.specs.forEach { spec ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0x22FFFFFF), RoundedCornerShape(4.dp))
                                    .border(0.5.dp, Color(0x44FFFFFF), RoundedCornerShape(4.dp))
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = spec,
                                    color = FireTextPrimary,
                                    fontSize = 8.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(16.dp))
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .shadow(8.dp, RoundedCornerShape(16.dp))
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0x22FFFFFF))
                    .border(1.5.dp, Color(0x11FFFFFF), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                AppIcon(
                    app = appItem,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

// INPUT SEARCH ROW WITH DOUBLE-CLICK TO ACTIVATE TYPE SEARCH ON ANDROID TV
@Composable
fun SearchSection(query: String, onQueryChange: (String) -> Unit, isFocusable: Boolean = true) {
    var isEditing by remember { mutableStateOf(false) }
    var lastClickTime by remember { mutableStateOf(0L) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(isEditing) {
        if (isEditing) {
            focusRequester.requestFocus()
            keyboardController?.show()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
    ) {
        if (!isEditing) {
            var isBoxFocused by remember { mutableStateOf(false) }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onFocusChanged { isBoxFocused = it.isFocused }
                    .focusable(enabled = isFocusable)
                    .clickable(enabled = isFocusable) {
                        val now = System.currentTimeMillis()
                        if (now - lastClickTime < 800) {
                            isEditing = true
                        }
                        lastClickTime = now
                    }
                    .border(
                        width = if (isBoxFocused) 2.5.dp else 1.dp,
                        color = if (isBoxFocused) FireOrangePrimary else Color(0xFF202730),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .background(
                        color = if (isBoxFocused) Color(0xFF10141A) else Color(0xFF0C0F13),
                        shape = RoundedCornerShape(28.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = FireOrangePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Text(
                        text = if (query.isNotEmpty()) query else "Search apps (Double-click to type)...",
                        color = if (query.isNotEmpty()) FireTextPrimary else FireTextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        } else {
            OutlinedTextField(
                value = query,
                onValueChange = onQueryChange,
                placeholder = {
                    Text(
                        text = "Search dynamic channels & installed applications...",
                        color = FireTextSecondary,
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = FireOrangePrimary
                    )
                },
                trailingIcon = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (query.isNotEmpty()) {
                            IconButton(
                                onClick = { onQueryChange("") },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Clear Search",
                                    tint = FireTextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        IconButton(
                            onClick = { isEditing = false },
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Done",
                                    tint = FireOrangePrimary
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = FireTextPrimary,
                    unfocusedTextColor = FireTextPrimary,
                    focusedBorderColor = FireOrangePrimary,
                    unfocusedBorderColor = Color(0xFF202730),
                    focusedContainerColor = Color(0xFF10141A),
                    unfocusedContainerColor = Color(0xFF0C0F13)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = {
                        isEditing = false
                        keyboardController?.hide()
                    }
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
                    .onFocusChanged {
                        if (!it.isFocused) {
                            isEditing = false
                        }
                    }
            )
        }
    }
}

// HORIZONTAL CATEGORIES WITH HIGH VISIBILITY TV D-PAD FOCUS
@Composable
fun CategoryRow(selectedTab: String, onTabSelected: (String) -> Unit, isFocusable: Boolean = true) {
    val tabs = listOf("All Apps", "Device Applications", "Favorites Hub")

    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(tabs) { tab ->
            val isSelected = selectedTab == tab
            var isFocused by remember { mutableStateOf(false) }
            val scale by animateFloatAsState(
                targetValue = if (isFocused) 1.08f else 1.0f,
                animationSpec = spring()
            )

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                    }
                    .onFocusChanged { isFocused = it.isFocused }
                    .focusable(enabled = isFocusable)
                    .clip(RoundedCornerShape(20.dp))
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = if (isFocused) Color.White else if (isSelected) FireOrangePrimary else Color(0x3390A4AE),
                        shape = RoundedCornerShape(20.dp)
                    )
                    .background(
                        color = if (isFocused) Color(0x2BFFFFFF) else if (isSelected) Color(0x1AFF6400) else Color.Transparent
                    )
                    .clickable(enabled = isFocusable) { onTabSelected(tab) }
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (tab) {
                        "All Apps" -> Icons.Default.List
                        "Device Applications" -> Icons.Default.Settings
                        else -> Icons.Default.Favorite
                    }
                    Icon(
                        imageVector = icon,
                        contentDescription = tab,
                        tint = if (isFocused) Color.White else if (isSelected) FireOrangePrimary else FireTextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = tab,
                        color = if (isFocused) Color.White else if (isSelected) FireTextPrimary else FireTextSecondary,
                        fontSize = 11.sp,
                        fontWeight = if (isFocused || isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

// HORIZONTAL APPS SLIDER LIST
@Composable
fun AppHorizontalGroup(
    title: String,
    apps: List<AppItem>,
    favorites: Set<String>,
    onAppClick: (AppItem) -> Unit,
    isFocusable: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = FireTextSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
            Text(
                text = "${apps.size} Apps",
                color = FireOrangePrimary.copy(alpha = 0.8f),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(apps, key = { it.id }) { app ->
                val isPinned = favorites.contains(app.packageName)

                AppCircularHubCard(
                    appItem = app,
                    isPinned = isPinned,
                    onClick = { onAppClick(app) },
                    isFocusable = isFocusable
                )
            }
        }
    }
}

// PREMIUM CIRCULAR TV APP SHORTCUT COMPONENT WITH BLURRY HALO FOCUS FEEDBACK
@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun AppCircularHubCard(
    appItem: AppItem,
    isPinned: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit = {},
    isFocusable: Boolean = true,
    rearrangingApp: AppItem? = null,
    onMoveApp: (Int) -> Unit = {},
    onFinishRearranging: () -> Unit = {}
) {
    var isFocused by remember { mutableStateOf(false) }
    val isRearranging = rearrangingApp?.packageName == appItem.packageName
    val scale by animateFloatAsState(targetValue = if (isRearranging) 1.25f else if (isFocused) 1.15f else 1.0f, animationSpec = spring())
    val haloAlpha by animateFloatAsState(targetValue = if (isRearranging) 0.8f else if (isFocused) 0.5f else 0.0f)

    var dpadPressJob by remember { mutableStateOf<Job?>(null) }
    val coroutineScope = rememberCoroutineScope()
    var isDpadLongPressTriggered by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(96.dp)
            .onFocusChanged { 
                isFocused = it.isFocused 
                if (!it.isFocused) {
                    dpadPressJob?.cancel()
                    dpadPressJob = null
                    isDpadLongPressTriggered = false
                }
            }
            .focusable(enabled = isFocusable)
            .semantics(mergeDescendants = true) {
                role = Role.Button
                contentDescription = appItem.name
                stateDescription = when {
                    isRearranging -> "Move mode active"
                    isPinned -> "Pinned app"
                    else -> "Press to open. Long press for options."
                }
            }
            .onKeyEvent { keyEvent ->
                if (isRearranging) {
                    if (keyEvent.type == KeyEventType.KeyDown) {
                        when (keyEvent.nativeKeyEvent.keyCode) {
                            KeyEvent.KEYCODE_DPAD_LEFT -> {
                                onMoveApp(-1)
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_RIGHT -> {
                                onMoveApp(1)
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_UP -> {
                                onMoveApp(-8)
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_DOWN -> {
                                onMoveApp(8)
                                true
                            }
                            KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER, KeyEvent.KEYCODE_BACK -> {
                                onFinishRearranging()
                                true
                            }
                            else -> false
                        }
                    } else {
                        val code = keyEvent.nativeKeyEvent.keyCode
                        code == KeyEvent.KEYCODE_DPAD_LEFT ||
                        code == KeyEvent.KEYCODE_DPAD_RIGHT ||
                        code == KeyEvent.KEYCODE_DPAD_UP ||
                        code == KeyEvent.KEYCODE_DPAD_DOWN ||
                        code == KeyEvent.KEYCODE_DPAD_CENTER ||
                        code == KeyEvent.KEYCODE_ENTER ||
                        code == KeyEvent.KEYCODE_BACK
                    }
                } else {
                    val code = keyEvent.nativeKeyEvent.keyCode
                    val isCenterKey = code == KeyEvent.KEYCODE_DPAD_CENTER || code == KeyEvent.KEYCODE_ENTER
                    if (isCenterKey && isFocusable) {
                        if (keyEvent.type == KeyEventType.KeyDown) {
                            if (dpadPressJob == null && !isDpadLongPressTriggered) {
                                isDpadLongPressTriggered = false
                                dpadPressJob = coroutineScope.launch {
                                    delay(650)
                                    isDpadLongPressTriggered = true
                                    onLongClick()
                                }
                            }
                            true
                        } else if (keyEvent.type == KeyEventType.KeyUp) {
                            dpadPressJob?.cancel()
                            dpadPressJob = null
                            if (!isDpadLongPressTriggered) {
                                onClick()
                            }
                            isDpadLongPressTriggered = false
                            true
                        } else {
                            false
                        }
                    } else {
                        false
                    }
                }
            }
            .combinedClickable(
                enabled = isFocusable,
                onClick = { onClick() },
                onLongClick = { onLongClick() }
            )
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.size(92.dp),
            contentAlignment = Alignment.Center
        ) {
            // Glow aura background matching the app's brandColor
            if (isRearranging || isFocused || haloAlpha > 0f) {
                val auraColor = if (isRearranging) Color(0xFFFF5722) else appItem.brandColor
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(auraColor.copy(alpha = haloAlpha), Color.Transparent),
                            ),
                            shape = CircleShape
                )
                        )
            }

            // Normal Circle Box
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .background(
                        color = if (isRearranging) Color(0xFF3E2723) else if (isFocused) Color(0xFF243042) else Color(0xFF141A24),
                        shape = CircleShape
                    )
                    .border(
                        width = if (isRearranging) 4.dp else if (isFocused) 3.dp else 1.dp,
                        color = if (isRearranging) Color(0xFFFF5722) else if (isFocused) FireOrangePrimary else Color(0x1FFFFFFF),
                        shape = CircleShape
                    )
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AppIcon(
                    app = appItem,
                    modifier = Modifier.fillMaxSize()
                )

                if (isPinned) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(2.dp)
                            .size(14.dp)
                            .background(FireOrangePrimary, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Pinned App",
                            tint = Color.Black,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }

                if (isRearranging) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(Color(0xFFFF5722), RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "MOVE",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = appItem.name,
            color = if (isRearranging) Color(0xFFFF5722) else if (isFocused) FireOrangePrimary else FireTextSecondary,
            fontSize = 11.sp,
            fontWeight = if (isRearranging || isFocused) FontWeight.Bold else FontWeight.Normal,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            lineHeight = 13.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
}

// IMMERSIVE EMBEDDED DYNAMIC REPRESTATIONS Composable
@Composable
fun AppIcon(app: AppItem, modifier: Modifier = Modifier) {
    if (app.isSystem) {
        if (app.iconDrawable != null) {
            val resources = LocalContext.current.resources
            val configuration = LocalConfiguration.current
            val drawable = remember(app.iconDrawable, configuration) {
                app.iconDrawable?.constantState?.newDrawable(resources)?.mutate() ?: app.iconDrawable
            }
            AndroidView(
                factory = { context ->
                    ImageView(context).apply {
                        scaleType = ImageView.ScaleType.FIT_CENTER
                        setImageDrawable(drawable)
                    }
                },
                update = { imageView -> imageView.setImageDrawable(drawable) },
                modifier = modifier.padding(10.dp)
            )
        } else {
            Icon(
                imageVector = Icons.Default.Settings,
                contentDescription = "App icon placeholder",
                tint = FireOrangePrimary,
                modifier = modifier.size(32.dp)
            )
        }
    } else {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCuratedLogo(app.id, size)
            }
        }
    }
}

// THE ARTWORK LOGO CANVAS WRITER
fun DrawScope.drawCuratedLogo(id: String, size: Size) {
    val width = size.width
    val height = size.height

    when (id) {
        "prime_video" -> {
            drawRect(color = Color(0xFF0F1722))
            val smilePath = Path().apply {
                moveTo(width * 0.25f, height * 0.52f)
                quadraticTo(width * 0.5f, height * 0.76f, width * 0.75f, height * 0.52f)
            }
            drawPath(
                path = smilePath,
                color = Color(0xFFfa9d18),
                style = Stroke(width = width * 0.07f, cap = StrokeCap.Round)
            )
            val arrowPath = Path().apply {
                moveTo(width * 0.69f, height * 0.55f)
                lineTo(width * 0.78f, height * 0.52f)
                lineTo(width * 0.75f, height * 0.62f)
                close()
            }
            drawPath(path = arrowPath, color = Color(0xFFfa9d18))
        }
        "netflix" -> {
            drawRect(color = Color(0xFF141414))
            val bandWidth = width * 0.16f
            val left = width * 0.24f
            val right = width * 0.60f

            drawRect(
                color = Color(0xFF5A0004),
                topLeft = Offset(left, height * 0.15f),
                size = Size(bandWidth, height * 0.7f)
            )
            drawRect(
                color = Color(0xFF5A0004),
                topLeft = Offset(right, height * 0.15f),
                size = Size(bandWidth, height * 0.7f)
            )
            val diagPath = Path().apply {
                moveTo(left, height * 0.15f)
                lineTo(left + bandWidth, height * 0.15f)
                lineTo(right + bandWidth, height * 0.85f)
                lineTo(right, height * 0.85f)
                close()
            }
            drawPath(path = diagPath, color = Color(0xFFE50914))

            drawRect(
                color = Color(0xFFB20710),
                topLeft = Offset(left, height * 0.15f),
                size = Size(bandWidth, height * 0.7f)
            )
            drawRect(
                color = Color(0xFFB20710),
                topLeft = Offset(right, height * 0.15f),
                size = Size(bandWidth, height * 0.7f)
            )
            drawPath(path = diagPath, color = Color(0xFFE50914))
        }
        "youtube" -> {
            drawRect(color = Color(0xFF1A1A1A))
            val btnWidth = width * 0.62f
            val btnHeight = height * 0.42f
            val btnTop = (height - btnHeight) / 2f
            val btnLeft = (width - btnWidth) / 2f

            drawRoundRect(
                color = Color(0xFFFF0000),
                topLeft = Offset(btnLeft, btnTop),
                size = Size(btnWidth, btnHeight),
                cornerRadius = CornerRadius(width * 0.10f)
            )
            val triPath = Path().apply {
                moveTo(width * 0.45f, height * 0.39f)
                lineTo(width * 0.59f, height * 0.50f)
                lineTo(width * 0.45f, height * 0.61f)
                close()
            }
            drawPath(path = triPath, color = Color.White)
        }
        "disneyplus" -> {
            drawRect(color = Color(0xFF010A26))
            drawArc(
                color = Color(0xFF00ACC1),
                startAngle = 180f,
                sweepAngle = 120f,
                useCenter = false,
                topLeft = Offset(width * 0.16f, height * 0.28f),
                size = Size(width * 0.68f, height * 0.68f),
                style = Stroke(width = width * 0.05f, cap = StrokeCap.Round)
            )
            drawCircle(
                color = Color.White,
                radius = width * 0.04f,
                center = Offset(width * 0.48f, height * 0.46f)
            )
            drawCircle(
                color = Color(0x55448AFF),
                radius = width * 0.09f,
                center = Offset(width * 0.48f, height * 0.46f)
            )
        }
        "twitch" -> {
            drawRect(color = Color(0xFF6441A5))
            val pWidth = width * 0.54f
            val pHeight = height * 0.48f
            val pLeft = (width - pWidth) / 2f
            val pTop = (height - pHeight) / 2.3f

            drawRoundRect(
                color = Color.White,
                topLeft = Offset(pLeft, pTop),
                size = Size(pWidth, pHeight),
                cornerRadius = CornerRadius(width * 0.08f)
            )
            drawRect(
                color = Color(0xFF6441A5),
                topLeft = Offset(width * 0.39f, height * 0.45f),
                size = Size(width * 0.07f, height * 0.16f)
            )
            drawRect(
                color = Color(0xFF6441A5),
                topLeft = Offset(width * 0.54f, height * 0.45f),
                size = Size(width * 0.07f, height * 0.16f)
            )
            val pointer = Path().apply {
                moveTo(pLeft + pWidth * 0.2f, pTop + pHeight)
                lineTo(pLeft + pWidth * 0.2f, pTop + pHeight + height * 0.09f)
                lineTo(pLeft + pWidth * 0.38f, pTop + pHeight)
                close()
            }
            drawPath(path = pointer, color = Color.White)
        }
        "max" -> {
            drawRect(color = Color(0xFF0F002E))
            drawCircle(
                color = Color(0xFF0038FF),
                radius = width * 0.30f,
                center = Offset(width * 0.5f, height * 0.5f),
                style = Stroke(width = width * 0.07f)
            )
            drawCircle(
                color = Color(0xFF9933FF),
                radius = width * 0.19f,
                center = Offset(width * 0.5f, height * 0.5f),
                style = Stroke(width = width * 0.03f)
            )
            drawCircle(
                color = Color.White,
                radius = width * 0.07f,
                center = Offset(width * 0.5f, height * 0.5f)
            )
        }
        "apple_tv" -> {
            drawRect(color = Color(0xFF1E1E24))
            drawCircle(
                color = Color(0xFF90A4AE),
                radius = width * 0.22f,
                center = Offset(width * 0.5f, height * 0.54f)
            )
            val leafPath = Path().apply {
                moveTo(width * 0.5f, height * 0.36f)
                quadraticTo(width * 0.57f, height * 0.27f, width * 0.61f, height * 0.29f)
                quadraticTo(width * 0.55f, height * 0.38f, width * 0.5f, height * 0.36f)
            }
            drawPath(path = leafPath, color = Color(0xFF90A4AE))
        }
        "spotify" -> {
            drawRect(color = Color(0xFF121212))
            drawCircle(
                color = Color(0xFF1DB954),
                radius = width * 0.35f,
                center = Offset(width * 0.5f, height * 0.5f)
            )
            drawArc(
                color = Color.Black,
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(width * 0.25f, height * 0.28f),
                size = Size(width * 0.5f, height * 0.5f),
                style = Stroke(width = width * 0.05f, cap = StrokeCap.Round)
            )
            drawArc(
                color = Color.Black,
                startAngle = 200f,
                sweepAngle = 140f,
                useCenter = false,
                topLeft = Offset(width * 0.3f, height * 0.38f),
                size = Size(width * 0.4f, height * 0.4f),
                style = Stroke(width = width * 0.05f, cap = StrokeCap.Round)
            )
        }
        "pluto_tv" -> {
            drawRect(color = Color.Black)
            val colors = listOf(Color(0xFFE50914), Color(0xFFFFA000), Color(0xFF00B4D8), Color(0xFF1DB954))
            val centers = listOf(
                Offset(width * 0.33f, height * 0.35f),
                Offset(width * 0.65f, height * 0.35f),
                Offset(width * 0.33f, height * 0.65f),
                Offset(width * 0.65f, height * 0.65f)
            )
            for (i in 0..3) {
                drawCircle(color = colors[i], radius = width * 0.14f, center = centers[i])
            }
        }
        else -> {
            drawRect(color = Color(0xFF37474F))
        }
    }
}

// EMPTY STATE PANELS
@Composable
fun EmptyStatePanel(query: String, onReset: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 40.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "No results",
                tint = FireTextSecondary,
                modifier = Modifier.size(54.dp)
            )
            Text(
                text = "No Channels Found",
                color = FireTextPrimary,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "No results matches \"$query\". Review spelling or switch filtering category tabs.",
                color = FireTextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.padding(horizontal = 40.dp),
                overflow = TextOverflow.Visible
            )
            Button(
                onClick = onReset,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0x33FF6400)),
                border = BorderStroke(1.dp, Color(0x66FF6400)),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(text = "Reset Dashboard Hub", color = FireOrangePrimary, fontSize = 12.sp)
            }
        }
    }
}

// FULL SCREEN ACTION DETAIL OVERLAY
@Composable
fun AppDetailOverlay(
    app: AppItem,
    isFavorited: Boolean,
    onToggleFavorite: () -> Unit,
    onLaunch: () -> Unit,
    onClose: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xE6050709))
            .clickable { onClose() },
        contentAlignment = Alignment.Center
    ) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1217)),
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
                .clickable(enabled = false) {}
                .padding(24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = app.category.uppercase(),
                            color = FireOrangePrimary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.8.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = app.name,
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }

                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0x33FFFFFF), CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Window",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                HorizontalDivider(color = Color(0x11FFFFFF))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .shadow(8.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(app.brandColor.copy(alpha = 0.3f))
                            .border(2.dp, app.brandColor.copy(alpha = 0.8f), RoundedCornerShape(16.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        AppIcon(app = app, modifier = Modifier.fillMaxSize())
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Aesthetic Rating: ${app.rating}",
                            color = FireAmberAccent,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = app.description,
                            color = FireTextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 16.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            app.specs.map { spec ->
                                Box(
                                    modifier = Modifier
                                        .background(Color(0x11FFFFFF), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = spec,
                                        color = FireTextPrimary,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }

                HorizontalDivider(color = Color(0x11FFFFFF))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onToggleFavorite,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0x11FFFFFF)),
                        border = BorderStroke(1.dp, Color(0x33FFFFFF)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (isFavorited) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorited) "Remove favorite" else "Add favorite",
                                tint = if (isFavorited) FireOrangePrimary else Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isFavorited) "Pins: Remove" else "Pin Shortcut",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val isClickable = !app.isSystem || app.launchIntent != null
                    Button(
                        onClick = onLaunch,
                        enabled = isClickable,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = FireOrangePrimary,
                            disabledContainerColor = Color(0xFF222831)
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (app.isSystem) Icons.Default.ExitToApp else Icons.Default.PlayArrow,
                                contentDescription = if (app.isSystem) "Open app" else "Play content",
                                tint = if (isClickable) Color.Black else FireTextMuted,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (app.isSystem) {
                                    if (isClickable) "LAUNCH APP" else "SYSTEM BLOCKED"
                                } else "STREAM FEED",
                                color = if (isClickable) Color.Black else FireTextMuted,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }
        }
    }
}

// FULLSCREEN CINEMATIC FEED SIMULATOR
@Composable
fun CinemaFeedPlayer(app: AppItem, onClose: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition(label = "audio_visualizer")
    val heights = (0..11).map { index ->
        infiniteTransition.animateFloat(
            initialValue = 0.1f,
            targetValue = 1.0f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 300 + (index * 70), easing = FastOutLinearInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "val_$index"
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                brush = Brush.radialGradient(
                    colors = listOf(
                        app.brandColor.copy(alpha = 0.4f),
                        Color.Black
                    ),
                    center = Offset(size.width / 2f, size.height / 3f),
                    radius = size.width * 0.9f
                )
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .background(FireOrangePrimary, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "LIVE CHANNEL PREVIEW FEED",
                        color = FireOrangePrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0x33FFFFFF), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Stop Feed",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f).wrapContentHeight()
            ) {
                Text(
                    text = app.name,
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = app.mockShow,
                    color = FireAmberAccent,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier
                        .height(100.dp)
                        .wrapContentWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    heights.map { animState ->
                        Box(
                            modifier = Modifier
                                .width(8.dp)
                                .fillMaxHeight(animState.value)
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            app.brandColor,
                                            FireOrangeGlow,
                                            FireAmberAccent
                                        )
                                    ),
                                    shape = RoundedCornerShape(4.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = "Streaming broadcast feeds buffer simulation active at 60 FPS...",
                    color = FireTextSecondary,
                    fontSize = 12.sp
                )
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xAA11141A)),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .border(1.dp, Color(0x33FFFFFF), RoundedCornerShape(24.dp))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Rewind Simulation",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(54.dp)
                            .background(FireOrangePrimary, CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Pause feed",
                            tint = Color.Black,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Forward Simulation",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
