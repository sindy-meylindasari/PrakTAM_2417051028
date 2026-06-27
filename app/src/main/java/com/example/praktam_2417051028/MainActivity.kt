package com.example.praktam_2417051028

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import androidx.compose.foundation.Canvas
import androidx.compose.ui.geometry.Offset
import com.example.praktam_2417051028.data.repository.MemoryRepository
import com.example.praktam_2417051028.lifereplay.Memory
import com.example.praktam_2417051028.ui.theme.PrakTAM_2417051028Theme
import kotlinx.coroutines.launch

private val Cream = Color(0xFFFFF8F0)
private val Rose = Color(0xFFA26769)
private val Brown = Color(0xFF4A2C2A)
private val SoftCard = Color(0xFFFFFBFA)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PrakTAM_2417051028Theme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "list") {
        composable("list") {
            MemoryListScreen(navController)
        }

        composable("add") {
            AddMemoryScreen(navController)
        }

        composable("detail/{title}/{desc}/{date}/{image}") { backStackEntry ->
            val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "")
            val desc = Uri.decode(backStackEntry.arguments?.getString("desc") ?: "")
            val date = Uri.decode(backStackEntry.arguments?.getString("date") ?: "")
            val image = Uri.decode(backStackEntry.arguments?.getString("image") ?: "")

            MemoryDetailScreen(title, desc, date, image, navController)
        }
    }
}

@Composable
fun MemoryListScreen(navController: NavController) {
    var memories by remember { mutableStateOf<List<Memory>>(emptyList()) }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val repository = remember { MemoryRepository() }

    val filteredMemories = memories.filter {
        it.title.contains(searchQuery, ignoreCase = true) || it.description.contains(
            searchQuery,
            ignoreCase = true
        ) || it.date.contains(searchQuery, ignoreCase = true)
    }

    LaunchedEffect(Unit) {
        try {
            isLoading = true
            memories = repository.getMemories()
            isError = false
        } catch (e: Exception) {
            isError = true
        } finally {
            isLoading = false
        }
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Cream),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Rose)
            }
        }

        isError -> {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Cream)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Gagal Memuat Data", color = Color.Red, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Pastikan koneksi internet Anda menyala.")
            }
        }

        else -> {
            Scaffold(
                containerColor = Cream, floatingActionButton = {
                    FloatingActionButton(
                        onClick = { navController.navigate("add") },
                        containerColor = Rose,
                        contentColor = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Icon(Icons.Filled.Add, contentDescription = "Tambah Kenangan")
                    }
                }) { padding ->
                LazyColumn(
                    modifier = Modifier
                        .padding(padding)
                        .fillMaxSize()
                        .background(Cream)
                        .statusBarsPadding(),
                    contentPadding = PaddingValues(18.dp),
                    verticalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    item {
                        HomeHeader(memories.size)

                        Spacer(modifier = Modifier.height(18.dp))

                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Cari kenangan...") },
                            singleLine = true,
                            shape = RoundedCornerShape(22.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Rose,
                                unfocusedBorderColor = Rose.copy(alpha = 0.35f),
                                focusedLabelColor = Rose,
                                cursorColor = Rose
                            )
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = "“Collect moments, not things.”",
                            color = Rose.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionTitle("Rekomendasi")

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyRow(horizontalArrangement = Arrangement.spacedBy(14.dp)) {
                            items(filteredMemories) { memory ->
                                MemoryRowItem(memory)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionTitle("Semua Kenangan")
                    }

                    if (filteredMemories.isEmpty()) {
                        item {
                            EmptyState()
                        }
                    } else {
                        items(filteredMemories) { memory ->
                            MemoryItem(memory, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HomeHeader(total: Int) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(34.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(210.dp)
                .background(
                    Brush.linearGradient(
                        listOf(
                            Color(0xFF7D4F50),
                            Color(0xFFA26769),
                            Color(0xFFE6B8A2)
                        )
                    )
                )
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.White.copy(alpha = 0.16f),
                    radius = 150f,
                    center = Offset(size.width * 0.9f, size.height * 0.05f)
                )
                drawCircle(
                    color = Color.White.copy(alpha = 0.10f),
                    radius = 95f,
                    center = Offset(size.width * 0.12f, size.height * 0.9f)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(26.dp)
            ) {
                Text(
                    text = "LifeReplay",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Every memory deserves a place.",
                    color = Color.White.copy(alpha = 0.95f),
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(22.dp))

                AssistChip(
                    onClick = {},
                    label = { Text("$total memories saved") }
                )
            }
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Bold,
        color = Brown
    )
}

@Composable
fun EmptyState() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Belum ada kenangan yang cocok", fontWeight = FontWeight.Bold, color = Brown)
            Spacer(modifier = Modifier.height(6.dp))
            Text("Coba cari dengan kata lain ya.", color = Color.DarkGray)
        }
    }
}

@Composable
fun MemoryItem(memory: Memory, navController: NavController) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(26.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCard)
    ) {
        Column {
            Box {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(memory.imageUrl)
                        .decoderFactory(SvgDecoder.Factory()).crossfade(true).build(),
                    contentDescription = memory.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp)),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                        contentDescription = null,
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(18.dp)) {
                Text(
                    text = memory.title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Brown
                )

                Spacer(modifier = Modifier.height(6.dp))

                AssistChip(onClick = {}, label = { Text(memory.date) })

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = memory.description,
                    maxLines = 2,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val title = Uri.encode(memory.title)
                        val desc = Uri.encode(memory.description)
                        val date = Uri.encode(memory.date)
                        val image = Uri.encode(memory.imageUrl)

                        navController.navigate("detail/$title/$desc/$date/$image")
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Rose)
                ) {
                    Text("Buka Kenangan")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryDetailScreen(
    title: String, desc: String, date: String, image: String, navController: NavController
) {
    Scaffold(
        containerColor = Cream, topBar = {
            TopAppBar(
                title = { Text("Detail Kenangan") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Cream, titleContentColor = Brown
            )
            )
        }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Cream),
            contentPadding = PaddingValues(18.dp)
        ) {
            item {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current).data(image)
                        .decoderFactory(SvgDecoder.Factory()).crossfade(true).build(),
                    contentDescription = title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Brown
                )

                Spacer(modifier = Modifier.height(8.dp))

                AssistChip(onClick = {}, label = { Text(date) })

                Spacer(modifier = Modifier.height(18.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftCard)
                ) {
                    Text(
                        text = desc,
                        modifier = Modifier.padding(20.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

@Composable
fun MemoryRowItem(memory: Memory) {
    Card(
        modifier = Modifier.width(165.dp),
        shape = RoundedCornerShape(22.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        colors = CardDefaults.cardColors(containerColor = SoftCard)
    ) {
        Column {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current).data(memory.imageUrl)
                    .decoderFactory(SvgDecoder.Factory()).crossfade(true).build(),
                contentDescription = memory.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp)),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(10.dp)) {
                Text(memory.title, fontWeight = FontWeight.Bold, maxLines = 2, color = Brown)
                Spacer(modifier = Modifier.height(4.dp))
                Text(memory.date, style = MaterialTheme.typography.bodySmall, color = Rose)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddMemoryScreen(navController: NavController) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }

    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val repository = remember { MemoryRepository() }

    Scaffold(
        containerColor = Cream, topBar = {
            TopAppBar(
                title = { Text("Tambah Kenangan") }, navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            }, colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Cream, titleContentColor = Brown
            )
            )
        }) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Cream),
            contentPadding = PaddingValues(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Buat kenangan baru ✨",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Brown
                )
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(26.dp),
                    colors = CardDefaults.cardColors(containerColor = SoftCard),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Judul Kenangan") },
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Deskripsi") },
                            minLines = 3,
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Tanggal") },
                            shape = RoundedCornerShape(18.dp)
                        )

                        OutlinedTextField(
                            value = imageUrl,
                            onValueChange = { imageUrl = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("URL Gambar") },
                            shape = RoundedCornerShape(18.dp)
                        )

                        Button(
                            onClick = {
                                scope.launch {
                                    isLoading = true

                                    val newMemory = Memory(
                                        title = title,
                                        description = description,
                                        date = date,
                                        imageUrl = imageUrl
                                    )

                                    repository.addMemory(newMemory)

                                    isLoading = false

                                    navController.navigate("list") {
                                        popUpTo("list") { inclusive = true }
                                    }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !isLoading && title.isNotBlank() && description.isNotBlank() && date.isNotBlank() && imageUrl.isNotBlank(),
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Rose)
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Menyimpan...")
                            } else {
                                Text("Simpan Kenangan")
                            }
                        }
                    }
                }
            }
        }
    }
}