package com.example.phonebook.screens

import android.provider.ContactsContract.CommonDataKinds.Note
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import com.example.phonebook.domain.model.ColorModel
import com.example.phonebook.domain.model.NoteModel
import com.example.phonebook.routing.Screen
import com.example.phonebook.ui.components.AppDrawer
import com.example.phonebook.ui.components.Contacts
import com.example.phonebook.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun QuickCall(viewModel: MainViewModel) {
    val contacts by viewModel.notesNotInTrash.observeAsState(listOf())
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Quick Call",
                        color = MaterialTheme.colors.onPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        coroutineScope.launch { scaffoldState.drawerState.open() }
                    }) {
                        Icon(
                            imageVector = Icons.Filled.List,
                            contentDescription = "Drawer Button"
                        )
                    }
                }
            )
        },
        drawerContent = {
            AppDrawer(
                currentScreen = Screen.QuickCall,
                closeDrawerAction = {
                    coroutineScope.launch {
                        scaffoldState.drawerState.close()
                    }
                }
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onCreateNewNoteClick() },
                contentColor = MaterialTheme.colors.background,
                content = {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Add Person Button"
                    )
                }
            )
        }
    ) {
        if (contacts.isNotEmpty()) {
            ImportantPersonsList(
                contacts = contacts.sortedBy { it.title },
                onContactCheckedChange = {
                    viewModel.onNoteCheckedChange(it)
                },
                onContactClick = { viewModel.onNoteClick(it) }
            )
        }
    }
}

@ExperimentalMaterialApi
@Composable
private fun ImportantPersonsList(
    contacts: List<NoteModel>,
    onContactCheckedChange: (NoteModel) -> Unit,
    onContactClick: (NoteModel) -> Unit
) {
    val importantPersons = contacts.filter{ it.isCheckedOff }
    LazyColumn {
        items(count = importantPersons.size) { noteIndex ->
            val contact = importantPersons[noteIndex]
            Contacts(
                note = contact,
                onNoteClick = onContactClick,
                onNoteCheckedChange = onContactCheckedChange,
                isSelected = false
            )
        }
    }
}

@ExperimentalMaterialApi
@Preview
@Composable
private fun ImportantPersonsPreview() {
    ImportantPersonsList(
        contacts = listOf(
            NoteModel(1, "Z", "Content 1", false),
            NoteModel(2, "M", "Content 2", false),
            NoteModel(3, "L", "Content 3", true)
        ).sortedBy { it.title },
        onContactCheckedChange = {},
        onContactClick = {}
    )
}
