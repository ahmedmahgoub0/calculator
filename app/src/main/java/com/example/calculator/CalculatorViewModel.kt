package com.example.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CalculatorUiState(
    val previousResult: String = "",
    val firstNumber: String = "",
    val secondNumber: String = "",
    val operator: Operator? = null
)

class CalculatorViewModel() : ViewModel() {
    private val _state = MutableStateFlow(CalculatorUiState())
    val state = _state.asStateFlow()

    fun clickNumber(number: String) {
        if (state.value.operator == null) {
            if (number == "." && _state.value.firstNumber.contains(".")) return
            _state.value = _state.value.copy(firstNumber = _state.value.firstNumber + number)
            return
        }

        if (number == "." && _state.value.secondNumber.contains(".")) return
        _state.value = _state.value.copy(secondNumber = _state.value.secondNumber + number)
    }

    fun clickOperator(operator: Operator) {
        if (_state.value.firstNumber.isEmpty()) return
        if (_state.value.secondNumber.isNotEmpty()) calculate()

        _state.value = _state.value.copy(operator = operator)
    }

    fun clearCalculations() {
        _state.value = CalculatorUiState()
    }

    fun clearLast() {
        val state = _state.value
        when {
            state.secondNumber.isNotEmpty() -> {
                _state.value = state.copy(secondNumber = state.secondNumber.dropLast(1))
            }

            state.operator != null -> {
                _state.value = state.copy(operator = null)
            }

            state.firstNumber.isNotEmpty() -> {
                _state.value = state.copy(firstNumber = state.firstNumber.dropLast(1))
            }
        }
    }

    fun calculate() {
        val first = _state.value.firstNumber.toDoubleOrNull()
        val second = _state.value.secondNumber.toDoubleOrNull()
        val operator = _state.value.operator

        if (first == null || second == null || operator == null) return

        val result = when (operator) {
            Operator.PLUS -> first + second
            Operator.MINUS -> first - second
            Operator.MULTIPLY -> first * second
            Operator.DIVIDE -> if (second != 0.0) first / second else throw ArithmeticException()
            Operator.MODULUS -> first % second
            Operator.CHANGE_SIGN -> -first
        }

        updateState(result)
    }

    private fun updateState(result: Double) {
        val formattedResult = String.format("%.3f", result).trimEnd('0').trimEnd('.')
        val state = _state.value
        _state.value = CalculatorUiState(
            firstNumber = formattedResult,
            secondNumber = "",
            operator = null,
            previousResult = state.firstNumber + state.operator?.sign + state.secondNumber
        )
    }
}