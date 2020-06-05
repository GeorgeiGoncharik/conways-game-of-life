package com.example.simplegameoflife

import kotlin.random.Random

class Universe(private val width: Int, private val height: Int) {

    private val cells: Array<Array<Cell>> = Array(width) { x -> Array(height) { y ->
        Cell(
            x,
            y,
            Random.nextInt(10) == 0
        )
    } }

    operator fun get(i: Int, j: Int): Cell { return cells[i][j] }

    private fun nbNeighboursOf(x: Int, y: Int): Int {
        var nb = 0
        for (k in x - 1..x + 1) {
            for (l in y - 1..y + 1) {
                if ((k != x || l != y) && k >= 0 && k < width && l >= 0 && l < height
                ) {
                    val cell = cells[k][l]
                    if (cell.isAlive) {
                        nb++
                    }
                }
            }
        }
        return nb
    }

    fun nextGeneration() {
        val liveCells: MutableList<Cell> = ArrayList()
        val deadCells: MutableList<Cell> = ArrayList()
        for (i in 0 until width) {
            for (j in 0 until height) {
                val cell = cells[i][j]
                val nbNeighbours = nbNeighboursOf(cell.x, cell.y)
                if (cell.isAlive &&
                    (nbNeighbours < 2 || nbNeighbours > 3)) {
                    deadCells.add(cell)
                }
                if ((cell.isAlive && (nbNeighbours == 3 || nbNeighbours == 2))
                    ||
                    (!cell.isAlive && nbNeighbours == 3)) {
                    liveCells.add(cell)
                }
            }
            }
        liveCells.forEach { cell -> cell.reborn()}
        deadCells.forEach { cell -> cell.die()}
    }

    fun killAll() {
        cells.forEach { row -> row.forEach { cell -> cell.die() } }
    }

    fun rebornAll() {
        cells.forEach { row -> row.forEach { cell -> cell.reborn() } }
    }
}