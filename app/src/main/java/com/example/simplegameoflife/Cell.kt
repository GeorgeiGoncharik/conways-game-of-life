package com.example.simplegameoflife

class Cell(val x: Int, val y: Int, var isAlive : Boolean = false) {
    fun die() {
        isAlive = false
    }

    fun reborn() {
        isAlive = true
    }

    fun invert() {
        isAlive = !isAlive
    }
}