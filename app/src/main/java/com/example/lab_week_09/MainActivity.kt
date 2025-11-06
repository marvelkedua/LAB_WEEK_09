package com.example.lab_week_09 // Ganti jika package Anda berbeda

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshots.SnapshotStateList
// Import untuk Navigasi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// Import Composable kustom
import com.example.lab_week_09.ui.theme.OnBackgroundItemText
import com.example.lab_week_09.ui.theme.OnBackgroundTitleText
import com.example.lab_week_09.ui.theme.PrimaryTextButton
import com.example.lab_week_09.ui.theme.LAB_WEEK_09Theme

// --- IMPORT BARU UNTUK MOSHI ---
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.lang.reflect.Type

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

// App Composable - Root Navigasi (Tidak ada perubahan di sini)
@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home { listDataString ->
                navController.navigate(
                    "resultContent/?listData=$listDataString")
            }
        }
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            // Ambil argumen JSON (masih sebagai String)
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

data class Student(
    var name: String
)

// Home Composable - DIPERBARUI
@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit
) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    var inputField by remember { mutableStateOf(Student("")) }

    // --- PERUBAHAN TUGAS 2: SIAPKAN MOSHI ---
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val listType: Type = Types.newParameterizedType(List::class.java, Student::class.java)
    val jsonAdapter: JsonAdapter<List<Student>> = moshi.adapter(listType)
    // --- AKHIR PERUBAHAN ---

    HomeContent(
        listData = listData,
        inputField = inputField,
        onInputValueChange = { newName -> inputField = inputField.copy(name = newName) },
        onButtonClick = {
            if (inputField.name.isNotBlank()) {
                listData.add(inputField.copy())
            }
            inputField = Student("")
        },

        // --- PERUBAHAN TUGAS 2: KONVERSI KE JSON ---
        navigateFromHomeToResult = {
            // Ubah listData menjadi List<Student>
            val listAsList = listData.toList()
            // Konversi list menjadi string JSON
            val listAsJson = jsonAdapter.toJson(listAsList)
            // Kirim string JSON
            navigateFromHomeToResult(listAsJson)
        }
        // --- AKHIR PERUBAHAN ---
    )
}

// HomeContent Composable (Tidak ada perubahan di sini)
// HomeContent Composable (DENGAN PERBAIKAN)
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit
) {
    LazyColumn {
        item {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundTitleText(text = stringResource(
                    id = R.string.enter_item)
                )
                TextField(
                    value = inputField.name,
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text
                    ),
                    // --- INI PERBAIKANNYA ---
                    // Mengganti 'onValueVChange' menjadi 'onValueChange'
                    onValueChange = {
                        onInputValueChange(it)
                    }
                    // --- AKHIR PERBAIKAN ---
                )

                Row {
                    PrimaryTextButton(text = stringResource(id =
                        R.string.button_click)) {
                        onButtonClick()
                    }
                    PrimaryTextButton(text = stringResource(id =
                        R.string.button_navigate)) {
                        navigateFromHomeToResult()
                    }
                }
            }
        }
        items(listData) { item ->
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name)
            }
        }
    }
}

// ResultContent Composable - DIPERBARUI
@Composable
fun ResultContent(listData: String) { // listData sekarang adalah JSON String

    // --- PERUBAHAN TUGAS 2: SIAPKAN MOSHI & PARSING JSON ---
    val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val listType: Type = Types.newParameterizedType(List::class.java, Student::class.java)
    val jsonAdapter: JsonAdapter<List<Student>> = moshi.adapter(listType)

    // Parsing JSON String kembali ke List<Student>
    // Gunakan try-catch untuk menghindari crash jika JSON tidak valid
    val studentList: List<Student>? = try {
        jsonAdapter.fromJson(listData)
    } catch (e: Exception) {
        null // Gagal parsing
    }
    // --- AKHIR PERUBAHAN ---

    // --- PERUBAHAN TUGAS 2: TAMPILKAN DENGAN LAZYCOLUMN ---
    if (studentList != null) {
        LazyColumn (
            modifier = Modifier
                .padding(16.dp) // Beri padding
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Judul
            item {
                OnBackgroundTitleText(text = "Final Student List")
            }

            // Tampilkan daftar siswa
            items(studentList) { student ->
                OnBackgroundItemText(text = student.name)
            }
        }
    } else {
        // Tampilkan pesan error jika parsing gagal
        Column(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OnBackgroundItemText(text = "Error: Failed to load list.")
        }
    }
    // --- AKHIR PERUBAHAN ---
}


@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    Home(navigateFromHomeToResult = {})
}