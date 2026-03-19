package com.example.praktam_2417051028

import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.praktam_2417051028.lifereplay.Memory
import com.example.praktam_2417051028.lifereplay.MemorySource
import com.example.praktam_2417051028.ui.theme.PrakTAM_2417051028Theme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PrakTAM_2417051028Theme {
                MemoryListScreen()
            }
        }
    }
}

@Composable
fun MemoryListScreen() {

    val memories = MemorySource.dummyMemory

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {

        Text(
            text = "LifeReplay Memories",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(20.dp))

        memories.forEach { memory ->

            MemoryItem(memory)

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun MemoryItem(memory: Memory) {

    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            // 🔥 BAGIAN GAMBAR + LOVE
            Box {

                Image(
                    painter = painterResource(id = memory.imageRes),
                    contentDescription = memory.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
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
                        contentDescription = "Favorite",
                        tint = if (isFavorite)
                            MaterialTheme.colorScheme.error
                        else
                            MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = memory.title,
                style = MaterialTheme.typography.titleMedium
            )

            Text(text = memory.description)
            Text(text = memory.date)

            Spacer(modifier = Modifier.height(10.dp))

            Button(onClick = { }) {
                Text("Lihat Kenangan")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MemoryPreview() {
    PrakTAM_2417051028Theme {
        MemoryListScreen()
    }
}