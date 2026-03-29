package com.example.praktam_2417051028

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        //  HEADER + LAZY ROW
        item {
            Text(
                text = "Rekomendasi",
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(memories) { memory ->
                    MemoryRowItem(memory)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Semua Kenangan",
                fontWeight = FontWeight.Bold
            )
        }

        //  LIST UTAMA
        items(memories) { memory ->
            MemoryItem(memory)
        }
    }
}

@Composable
fun MemoryItem(memory: Memory) {
    var isFavorite by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column {

            Box {
                Image(
                    painter = painterResource(id = memory.imageRes),
                    contentDescription = memory.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )

                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite)
                            Icons.Filled.Favorite
                        else
                            Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (isFavorite) Color.Red else Color.White
                    )
                }
            }

            Column(modifier = Modifier.padding(12.dp)) {
                Text(memory.title, fontWeight = FontWeight.Bold)
                Text(memory.date) // 🔥 tambahan dari data kamu
                Text(memory.description)

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {},
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Lihat Detail")
                }
            }
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
                painter = painterResource(id = memory.imageRes),
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

@Preview(showBackground = true)
@Composable
fun MemoryPreview() {
    PrakTAM_2417051028Theme {
        MemoryListScreen()
    }
}