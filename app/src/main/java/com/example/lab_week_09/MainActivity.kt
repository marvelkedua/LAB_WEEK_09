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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LAB_WEEK_09Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Panggil App composable dan buat NavController [cite: 605-617]
                    val navController = rememberNavController()
                    App(navController = navController)
                }
            }
        }
    }
}

// App Composable - Root Navigasi (Langkah 4, Part 4) [cite: 557-604]
@Composable
fun App(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        // Rute "home" [cite: 569-579]
        composable("home") {
            Home { listDataString ->
                // Navigasi ke "resultContent" dengan data
                navController.navigate(
                    "resultContent/?listData=$listDataString")
            }
        }
        // Rute "resultContent" dengan argumen [cite: 580-602]
        composable(
            "resultContent/?listData={listData}",
            arguments = listOf(navArgument("listData") {
                type = NavType.StringType
            })
        ) {
            ResultContent(
                it.arguments?.getString("listData").orEmpty()
            )
        }
    }
}

data class Student(
    var name: String
)

// Home Composable - diperbarui (Langkah 6, Part 4) [cite: 620-625]
@Composable
fun Home(
    navigateFromHomeToResult: (String) -> Unit // Parameter lambda baru
) {
    val listData = remember {
        mutableStateListOf(
            Student("Tanu"),
            Student("Tina"),
            Student("Tono")
        )
    }
    var inputField by remember { mutableStateOf(Student("")) }

    // Memanggil HomeContent - diperbarui (Langkah 8, Part 4) [cite: 635-645]
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
        // Teruskan lambda navigasi [cite: 645]
        navigateFromHomeToResult = { navigateFromHomeToResult(listData.toList().toString()) }
    )
}

// HomeContent Composable - diperbarui (Langkah 7 & 9, Part 4)
@Composable
fun HomeContent(
    listData: SnapshotStateList<Student>,
    inputField: Student,
    onInputValueChange: (String) -> Unit,
    onButtonClick: () -> Unit,
    navigateFromHomeToResult: () -> Unit // Parameter lambda baru [cite: 627-634]
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
                    onValueChange = {
                        onInputValueChange(it)
                    }
                )

                // Row ditambahkan (Langkah 9, Part 4) [cite: 646-678]
                Row {
                    PrimaryTextButton(text = stringResource(id =
                        R.string.button_click)) {
                        onButtonClick() // [cite: 671]
                    }
                    PrimaryTextButton(text = stringResource(id =
                        R.string.button_navigate)) {
                        navigateFromHomeToResult() // [cite: 675]
                    }
                }
            }
        }
        items(listData) { item -> // [cite: 680]
            Column(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OnBackgroundItemText(text = item.name) // [cite: 688]
            }
        }
    }
}

// ResultContent Composable - (Langkah 10, Part 4) [cite: 698-713]
@Composable
fun ResultContent(listData: String) {
    Column (
        modifier = Modifier
            .padding(vertical = 4.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnBackgroundItemText(text = listData)
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHome() {
    // Diperbarui agar preview berfungsi dengan parameter lambda baru
    Home(navigateFromHomeToResult = {})
}