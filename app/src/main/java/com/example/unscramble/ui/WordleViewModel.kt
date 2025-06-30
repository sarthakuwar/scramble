package com.example.unscramble.ui


import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow



class WordleViewModel : ViewModel() {


    private val wordList = listOf("PLANET", "GARDEN", "PENCIL", "MARKET", "BUTTON", "JACKET")

    private val _uiState = MutableStateFlow(
        WordleUiState(
            targetWord = wordList.random()
        )
    )
    val uiState: StateFlow<WordleUiState> = _uiState

    fun onLetterInput(char: Char) {
        val current = _uiState.value
        if (current.currentInput.length < 6 && !current.isGameOver) {
            _uiState.value = current.copy(currentInput = current.currentInput + char.uppercaseChar())
        }
    }

    fun onDeleteLetter() {
        val current = _uiState.value
        if (current.currentInput.isNotEmpty() && !current.isGameOver) {
            _uiState.value = current.copy(currentInput = current.currentInput.dropLast(1))
        }
    }

    fun onSubmitGuess() {
        val current = _uiState.value
        val guess = current.currentInput

        if (guess.length != 6) return

        val result = checkGuess(guess, current.targetWord)
        val wordGuess = WordGuess(result.mapIndexed { i, res -> Letter(guess[i], res) })

        val newGuesses = current.guesses + wordGuess
        val won = guess == current.targetWord
        val over = won || newGuesses.size >= current.maxAttempts

        _uiState.value = current.copy(
            guesses = newGuesses,
            currentInput = "",
            isGameOver = over,
            isGameWon = won
        )
    }

    fun restartGame() {
        _uiState.value = WordleUiState(targetWord = wordList.random())
    }

    private fun checkGuess(guess: String, target: String): List<LetterResult> {
        val result = MutableList(6) { LetterResult.INCORRECT }
        val targetChars = target.toMutableList()

        for (i in guess.indices) {
            if (guess[i] == target[i]) {
                result[i] = LetterResult.CORRECT
                targetChars[i] = '*'
            }
        }

        for (i in guess.indices) {
            if (result[i] != LetterResult.CORRECT && targetChars.contains(guess[i])) {
                result[i] = LetterResult.WRONG_POSITION
                targetChars[targetChars.indexOf(guess[i])] = '*'
            }
        }

        return result
    }
}
