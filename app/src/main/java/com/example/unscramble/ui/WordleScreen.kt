package com.example.unscramble.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions

import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


private fun LetterResult.toTileColor(): Color = when (this) {
    LetterResult.CORRECT        -> Color(0xFF6AAA64)
    LetterResult.WRONG_POSITION -> Color(0xFFC9B458)
    LetterResult.INCORRECT      -> Color(0xFF787C7E)
}
private val keyToChar: Map<Key, Char> = mapOf(
    Key.A to 'A', Key.B to 'B', Key.C to 'C', Key.D to 'D', Key.E to 'E',
    Key.F to 'F', Key.G to 'G', Key.H to 'H', Key.I to 'I', Key.J to 'J',
    Key.K to 'K', Key.L to 'L', Key.M to 'M', Key.N to 'N', Key.O to 'O',
    Key.P to 'P', Key.Q to 'Q', Key.R to 'R', Key.S to 'S', Key.T to 'T',
    Key.U to 'U', Key.V to 'V', Key.W to 'W', Key.X to 'X', Key.Y to 'Y',
    Key.Z to 'Z'
)

private val alphaKeys = keyToChar.keys


@Composable
fun WordleScreen(viewModel: WordleViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val keyboard = LocalSoftwareKeyboardController.current



    BasicTextField(
        value = "",
        onValueChange = { text ->
            if (text.isNotEmpty()) {
                val ch = text.last().uppercaseChar()
                when {
                    ch == '\n'           -> viewModel.onSubmitGuess()
                    ch == '\b'           -> viewModel.onDeleteLetter()
                    ch.isLetter()        -> viewModel.onLetterInput(ch)
                }
            }
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .size(1.dp)
            .alpha(0f)
            .focusable()
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyUp) return@onPreviewKeyEvent false
                when (event.key) {
                    Key.Enter     -> { viewModel.onSubmitGuess(); true }
                    Key.Backspace -> { viewModel.onDeleteLetter(); true}
                    in alphaKeys -> {      ;
                        val ch = keyToChar[event.key]!!
                        viewModel.onLetterInput(ch)
                        true}
                    else -> false
                }
            }
    )


    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            GuessGrid(
                guesses = uiState.guesses,
                currentInput = uiState.currentInput,
                maxAttempts = uiState.maxAttempts
            )
        }
    }


    if (uiState.isGameOver) {
        AlertDialog(
            onDismissRequest = { /* block */ },
            title  = { Text(if (uiState.isGameWon) "Congratulations!" else "Game Over") },
            text   = { Text(if (uiState.isGameWon) "You guessed the word!" else "The word was ${uiState.targetWord}") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.restartGame()
                    keyboard?.show()
                }) { Text("Play again") }
            }
        )
    }
}



@Composable
fun GuessGrid(
    guesses: List<WordGuess>,
    currentInput: String,
    maxAttempts: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        guesses.forEach { GuessRow(it) }
        if (guesses.size < maxAttempts) TypingRow(currentInput)
        repeat(maxAttempts - guesses.size - 1) { EmptyRow() }
    }
}

@Composable private fun GuessRow(wordGuess: WordGuess) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        wordGuess.letters.forEach { LetterTile(it.char, it.result) }
    }
}

@Composable private fun TypingRow(currentInput: String) {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        val letters = currentInput.padEnd(6, ' ').toCharArray()
        letters.forEachIndexed { i, ch ->
            LetterTile(ch, if (i < currentInput.length) LetterResult.INCORRECT else null)
        }
    }
}

@Composable private fun EmptyRow() {
    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) { repeat(6) { LetterTile(' ', null) } }
}

@Composable private fun LetterTile(ch: Char, result: LetterResult?) {
    val color = result?.toTileColor() ?: Color.Transparent
    val border = if (result == null) Color(0xFF3A3A3C) else Color.Transparent
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(48.dp)
            .border(2.dp, border, RoundedCornerShape(4.dp))
            .background(color, RoundedCornerShape(4.dp))
    ) {
        Text(
            text = if (ch == ' ') "" else ch.toString(),
            fontWeight = FontWeight.Bold,
            color = if (result == null) MaterialTheme.colorScheme.onBackground else Color.White
        )
    }
}


