package com.example.simplegameoflife

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var gameOfLifeView: GameOfLifeView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameOfLifeView = findViewById(R.id.game_of_life)
    }

    override fun onResume() {
        super.onResume()
        gameOfLifeView.start()
        Log.e(TAG, "resuming activity")
    }

    override fun onPause() {
        super.onPause()
        gameOfLifeView.stop()
        Log.e(TAG, "stopping activity")
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_info -> {
            // User chose the "Settings" item, show the app settings UI...
            startActivity(Intent(this, InfoActivity::class.java))
            true
        }
        R.id.action_start -> {
            if(!gameOfLifeView.isRunning)
                gameOfLifeView.start()
            true
        }
        R.id.action_stop -> {
            if(gameOfLifeView.isRunning)
                gameOfLifeView.stop()
            true
        }
        R.id.action_clear -> {
            gameOfLifeView.clear()
            true
        }
        R.id.action_camera -> {
            gameOfLifeView.clear()
            Log.e(TAG, "starting camera intent")
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent,
                REQUEST_TAKE_PHOTO
            )
            true
        }
        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            REQUEST_TAKE_PHOTO -> {
                Log.e(TAG, "received result code == REQUEST_TAKE_PHOTO")
                val bmp = data!!.extras!!.get("data") as Bitmap
                val isAliveStates = ImageProcessor.toBooleanArray(bmp, Pair(gameOfLifeView.nbColumns, gameOfLifeView.nbRows))
                gameOfLifeView.updateIsAliveStates(isAliveStates)
            }
            else ->
                Log.e(TAG, "result code does not match.")

        }
    }

    companion object {
        private const val REQUEST_TAKE_PHOTO = 0
        private const val TAG = "MainActivity"
    }
}
