package com.example.lab_week_09 // Ganti jika package Anda berbeda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                // Surface container using the 'background' color from the theme
                Surface(
                    // Kode ini diambil dari Langkah 13
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val list = listOf("Tanu", "Tina", "Tono")
                    // Here, we call the Home composable
                    Home(list)
                }
            }
        }
    }
}

// Kode ini diambil dari Langkah 12
// @Preview dihapus dari sini karena composable memiliki parameter
@Composable
fun Home(items: List<String>) { // Parameter ditambahkan [cite: 130]
    // LazyColumn lebih efisien, seperti RecyclerView [cite: 131-133]
    LazyColumn {
        // item untuk menampilkan satu item di LazyColumn
        item {
            Column(
                // Modifier.padding(16.dp) untuk menambah padding [cite: 139]
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                // Alignment.CenterHorizontally untuk rata tengah horizontal [cite: 147]
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(
                    id = R.string.enter_item) // Mengambil string [cite: 156-157]
                )
                // TextField untuk input teks [cite: 159]
                TextField(
                    value = "",
                    // Keyboard type [cite: 161-163]
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    ),
                    // Event saat nilai berubah [cite: 164, 167]
                    onValueChange = {
                    }
                )
                // Button untuk tombol [cite: 170]
                Button(onClick = { }) {
                    // Teks tombol [cite: 174-176]
                    Text(text = stringResource(
                        id = R.string.button_click)
                    )
                }
            }
        }
        // items untuk menampilkan list item (pengganti Adapter) [cite: 181-184]
        items(items) { item ->
            Column(
                modifier = Modifier.padding(vertical = 4.dp).fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = item)
            }
        }
    }
}

// Fungsi preview terpisah untuk Home [cite: 195-202]
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(listOf("Tanu", "Tina", "Tono"))
}