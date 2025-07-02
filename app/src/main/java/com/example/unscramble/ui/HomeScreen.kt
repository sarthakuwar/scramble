package com.example.unscramble.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.ActivityNavigator
import com.example.unscramble.ui.navigation.Destination


@Composable
fun HomeScreen(onNavigate: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Welcome to the Game Hub!\nChoose a game below:")
        Button(
            onClick = { onNavigate(Destination.Wordle.route) },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Play Wordle")
        }
        Button(
            onClick = { onNavigate(Destination.Unscramble.route) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Play Unscramble")
        }
    }
    /* TODO: implement your UI. Call onNavigate(Destination.Wordle.route) to navigate. */
}