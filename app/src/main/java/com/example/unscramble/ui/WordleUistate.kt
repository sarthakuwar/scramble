package com.example.unscramble.ui


enum class LetterResult{   CORRECT,INCORRECT,WRONG_POSITION}

data class Letter(val char: Char, val result: LetterResult)

data class WordGuess(val letters: List<Letter>)

data class WordleUiState(
    val targetWord: String = "",
    val currentInput: String = "",
    val guesses: List<WordGuess> = emptyList(),
    val isGameOver: Boolean = false,
    val isGameWon: Boolean = false,
    val maxAttempts: Int = 6


)
