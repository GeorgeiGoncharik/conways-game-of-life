package com.example.simplegameoflife

import android.graphics.Bitmap
import android.graphics.Color


object ImageProcessor {
    fun toBooleanArray(bmp: Bitmap, dimensions: Pair<Int, Int>): Array<Array<Boolean>> {
        val (x,y) = dimensions
        val states: Array<Array<Boolean>> = Array(x) { Array(y) { false } }
        val bitmap = Bitmap.createScaledBitmap(bmp, x, y, false)

        for (x_i in 0 until x)
            for(y_i in 0 until y){
                val pixel = bitmap.getPixel(x_i, y_i)
                val r = Color.red(pixel)
                val g = Color.green(pixel)
                val b = Color.blue(pixel)
                val luminance =
                    0.299 * r + 0.0f + 0.587 * g + 0.114 * b
                states[x_i][y_i] = luminance >= 128.0f
            }

        return states
    }
}