import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.lab_week_09.R // Ganti dengan package Anda jika berbeda
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

//Sekarang kita extend ComponentActivity
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Kita gunakan setContent bukan setContentView
        setContent {
            //Kita bungkus konten dengan tema
            LAB_WEEK_09Theme {
                // A surface container using the 'background' color from the
                Surface(
                    //Gunakan Modifier.fillMaxSize() agar surface memenuhi layar
                    modifier = Modifier.fillMaxSize(),
                    //Gunakan MaterialTheme.colorScheme.background untuk warna background
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Memanggil Home composable
                    Home()
                }
            }
        }
    }
}

//Fungsi preview terpisah untuk Home
@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(listOf("Tanu", "Tina", "Tono"))
}
@Composable
fun Home(items: List<String>) { // Tambahkan parameter items
    //LazyColumn lebih efisien, seperti RecyclerView
    LazyColumn {
        //item untuk menampilkan satu item di LazyColumn
        item {
            Column(
                //Modifier.padding(16.dp) untuk menambah padding
                modifier = Modifier.padding(16.dp).fillMaxSize(),
                //Alignment.CenterHorizontally untuk rata tengah horizontal
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(
                    id = R.string.enter_item)
                )
                //TextField untuk input teks
                TextField(
                    value = "",
                    // Keyboard type
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number // Anda bisa ganti ke KeyboardType.Text
                    ),
                    // Event saat nilai berubah
                    onValueChange = {
                    }
                )
                //Button untuk tombol
                Button(onClick = { }) {
                    //Teks tombol
                    Text(text = stringResource(
                        id = R.string.button_click)
                    )
                }
            }
        }
        //items untuk menampilkan list item (pengganti Adapter)
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