package com.example.praktam_2417051028

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.praktam_2417051028.ui.theme.PrakTAM_2417051028Theme
import com.example.praktam_2417051028.lifereplay.Memory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.praktam_2417051028.lifereplay.MemorySource as MemorySource1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PrakTAM_2417051028Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MemoryScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun MemoryScreen(modifier: Modifier = Modifier) {

    val memories = MemorySource1.dummyMemory

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            memories.forEach { memory ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {

                        Text(text = memory.title)
                        Text(text = memory.description)
                        Text(text = memory.date)

                        Image(
                            painter = painterResource(id = memory.imageRes),
                            contentDescription = memory.title
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PrakTAM_2417051028Theme {
        MemoryScreen(modifier = Modifier)
    }
}