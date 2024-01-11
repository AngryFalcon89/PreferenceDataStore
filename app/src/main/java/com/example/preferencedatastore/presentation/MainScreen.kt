package com.example.preferencedatastore.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {

    val userName = viewModel.userName.collectAsState()

    val text = remember {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {

        Text(text = "Your Saved Name", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = userName.value, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(12.dp))

        Text(text = "Enter Your name", style = MaterialTheme.typography.titleLarge)

        OutlinedTextField(value = text.value, onValueChange = { text.value = it })

        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = { viewModel.saveData(text.value) }) {
            Text(text = "Save Name")
        }

    }

}