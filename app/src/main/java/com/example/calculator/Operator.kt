package com.example.calculator

enum class Operator(val sign: Char) {
    PLUS('+'),
    MINUS('-'),
    MULTIPLY('*'),
    DIVIDE('/'),
    MODULUS('%'),
    CHANGE_SIGN('±')
}