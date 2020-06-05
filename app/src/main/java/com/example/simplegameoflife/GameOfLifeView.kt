package com.example.simplegameoflife

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.WindowManager


class GameOfLifeView : SurfaceView, Runnable {
    // Thread which will be responsible to manage the evolution of the World
    private var thread: Thread? = null

    // Boolean indicating if the World is evolving or not
    var isRunning = false
    var nbColumns = 1
        private set
    var nbRows = 1
        private set
    private var columnWidth = 1
    private var rowHeight = 1
    private lateinit var universe: Universe

    // Utilitaries objects : a Rectangle instance and a Paint instance used to draw the elements
    private val r: Rect = Rect()
    private val p: Paint = Paint()
    private val targetFPS = 60 // frames per second, the rate at which you would like to refresh the Canvas

    constructor(context: Context) : super(context) {
        initUniverse()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initUniverse()
    }

    override fun run() {
        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        val targetTime = (1000 / targetFPS).toLong()

        // while the world is evolving
        while (isRunning) {
            startTime = System.nanoTime()

            if (!holder.surface.isValid) continue
            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            if (waitTime < 100) {
                waitTime = 100
            }
            try {
                Thread.sleep(waitTime)
            } catch (e: InterruptedException) {
            }
            universe.nextGeneration()
            drawCells()
        }
    }

    fun start() {
        // World is evolving
        isRunning = true

        thread = Thread(this)
        // we start the Thread for the World's evolution
        thread!!.start()
    }

    fun stop() {
        isRunning = false
        while (true) {
            try {
                thread!!.join()
            } catch (e: InterruptedException) {
            }
            break
        }
    }

    fun clear() {
        stop()
        universe.killAll()
        drawCells()
    }

    private fun initUniverse() {
        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val point = Point()
        display.getSize(point)
        // we calculate the number of columns and rows for our World
        nbColumns = point.x / DEFAULT_SIZE
        nbRows = point.y / DEFAULT_SIZE
        // we calculate the column width and row height
        columnWidth = point.x / nbColumns
        rowHeight = point.y / nbRows
        universe = Universe(nbColumns, nbRows)
    }

    // Method to draw each cell of the world on the canvas
    private fun drawCells() {
        if (holder.surface.isValid){
            val canvas: Canvas = holder.lockCanvas()
            for (i in 0 until nbColumns) {
                for (j in 0 until nbRows) {
                    val cell: Cell = universe[i, j]
                    r.set(
                        cell.x * columnWidth - 1, cell.y * rowHeight - 1,
                        cell.x * columnWidth + columnWidth - 1,
                        cell.y * rowHeight + rowHeight - 1
                    )
                    // we change the color according the alive status of the cell
                    p.color = if (cell.isAlive) DEFAULT_ALIVE_COLOR else DEFAULT_DEAD_COLOR
                    canvas.drawRect(r, p)
                }
            }
            holder.unlockCanvasAndPost(canvas)
        }

    }

    fun updateIsAliveStates(states: Array<Array<Boolean>>){
        for(i in 0 until nbColumns)
            for(j in 0 until nbRows){
                val cell: Cell = universe[i, j]
                if(states[i][j])
                    cell.reborn()
                else
                    cell.die()
            }
        drawCells()
    }

    // We let the user to interact with the Cells of the World
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            // we get the coordinates of the touch and we convert it in coordinates for the board
            val i = (event.x / columnWidth).toInt()
            val j = (event.y / rowHeight).toInt()
            // we get the cell associated to these positions
            val cell: Cell = universe[i, j]
            // we call the invert method of the cell got to change its state
            cell.invert()
            drawCells()
            invalidate()
        }
        return super.onTouchEvent(event)
    }



    companion object {
        // Default size of a cell
        const val DEFAULT_SIZE = 5
        // Default color of an alive color (white in our case)
        const val DEFAULT_ALIVE_COLOR: Int = android.graphics.Color.WHITE
        // Default color of a dead color (black in our case)
        const val DEFAULT_DEAD_COLOR: Int = android.graphics.Color.BLACK
    }
}