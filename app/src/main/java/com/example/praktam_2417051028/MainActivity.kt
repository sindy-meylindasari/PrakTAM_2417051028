package com.example.praktam_2417051028

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.praktam_2417051028.lifereplay.Memory
import com.example.praktam_2417051028.lifereplay.MemorySource
import com.example.praktam_2417051028.ui.theme.PrakTAM_2417051028Theme

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

        composable("detail/{title}/{desc}/{date}/{image}") { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val desc = backStackEntry.arguments?.getString("desc") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val image = backStackEntry.arguments?.getString("image")?.toInt() ?: 0

            MemoryDetailScreen(title, desc, date, image, navController)
        }
    }
}

@Composable
fun MemoryListScreen(navController: NavController) {

    val memories = MemorySource.dummyMemory

    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(1500)

        // simulasi sukses / gagal
        isError = (0..1).random() == 0
        isLoading = false
    }

    when {
        isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        isError -> {
            //  TAMPILAN ERROR (INI YANG DIMINTA MODUL)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "Gagal Memuat Data",
                    color = Color.Red,
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text("Pastikan koneksi internet Anda menyala")

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = {
                    // retry
                    isLoading = true
                    scope.launch {
                        delay(1500)
                        isError = false
                        isLoading = false
                    }
                }) {
                    Text("Coba Lagi")
                }
            }
        }

        else -> {
            //  TAMPILAN NORMAL (SUKSES)
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .statusBarsPadding(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                item {
                    Text("Rekomendasi", style = MaterialTheme.typography.titleLarge)

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(memories) {
                            MemoryRowItem(it)
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Semua Kenangan", style = MaterialTheme.typography.titleLarge)
                }

                items(memories) {
                    MemoryItem(it, navController)
                }
            }
        }
    }
}

@Composable
fun MemoryItem(memory: Memory, navController: NavController) {

    var isFavorite by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Box {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column {

                Box {
                    Image(
                        painter = painterResource(memory.imageRes),
                        contentDescription = memory.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )

                    IconButton(
                        onClick = { isFavorite = !isFavorite },
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        Icon(
                            imageVector = if (isFavorite)
                                Icons.Filled.Favorite
                            else
                                Icons.Outlined.FavoriteBorder,
                            contentDescription = null,
                            tint = if (isFavorite) Color.Red else Color.White
                        )
                    }
                }

                Column(modifier = Modifier.padding(12.dp)) {
                    Text(memory.title, fontWeight = FontWeight.Bold)
                    Text(memory.date)
                    Text(memory.description)

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = {
                            scope.launch {
                                isLoading = true
                                delay(1000)

                                val isError = (0..1).random() == 0

                                if (isError) {
                                    snackbarHostState.showSnackbar("Gagal membuka data ❌")
                                } else {
                                    snackbarHostState.showSnackbar("Berhasil membuka ${memory.title} ✅")

                                    navController.navigate(
                                        "detail/${memory.title}/${memory.description}/${memory.date}/${memory.imageRes}"
                                    )
                                }

                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Loading...")
                        } else {
                            Text("Lihat Detail")
                        }
                    }
                }
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MemoryDetailScreen(
    title: String,
    desc: String,
    date: String,
    image: Int,
    navController: NavController
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detail Kenangan") },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
        ) {

            Image(
                painter = painterResource(image),
                contentDescription = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(title, fontWeight = FontWeight.Bold)
            Text(date, color = MaterialTheme.colorScheme.primary)

            Spacer(modifier = Modifier.height(8.dp))

            Text(desc)
        }
    }
}

@Composable
fun MemoryRowItem(memory: Memory) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            Image(
                painter = painterResource(memory.imageRes),
                contentDescription = memory.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(8.dp)) {
                Text(memory.title, fontWeight = FontWeight.Bold)
                Text(memory.date)
            }
        }
    }
}