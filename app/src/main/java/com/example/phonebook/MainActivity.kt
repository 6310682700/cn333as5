package com.example.phonebook

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.phonebook.routing.MyNotesRouter
import com.example.phonebook.routing.Screen
import com.example.phonebook.screens.ContactsScreen
import com.example.phonebook.screens.SaveContactScreen
import com.example.phonebook.screens.QuickCall
import com.example.phonebook.screens.TrashScreen
import com.example.phonebook.ui.theme.MyNotesTheme
import com.example.phonebook.ui.theme.MyNotesThemeSettings
import com.example.phonebook.viewmodel.MainViewModel
import com.example.phonebook.viewmodel.MainViewModelFactory

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyNotesTheme(darkTheme = MyNotesThemeSettings.isDarkThemeEnabled) {
                val viewModel: MainViewModel = viewModel(
                    factory = MainViewModelFactory(LocalContext.current.applicationContext as Application)
                )
                MainActivityScreen(viewModel)
            }
        }
    }
}

@ExperimentalMaterialApi
@Composable
fun MainActivityScreen(viewModel: MainViewModel) {
    Surface {
        when (MyNotesRouter.currentScreen) {
            is Screen.Notes -> ContactsScreen(viewModel)
            is Screen.SaveNote -> SaveContactScreen(viewModel)
            is Screen.QuickCall -> QuickCall(viewModel)
            is Screen.Trash -> TrashScreen(viewModel)
        }
    }
}
