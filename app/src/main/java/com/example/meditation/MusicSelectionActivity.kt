package com.example.meditation

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MusicSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_selection)

        findViewById<Button>(R.id.btnMusic1).setOnClickListener {
            returnMusic("meditation1", "Calm Breeze")
        }
        findViewById<Button>(R.id.btnMusic2).setOnClickListener {
            returnMusic("meditation2", "Forest Stream")
        }
        findViewById<Button>(R.id.btnMusic3).setOnClickListener {
            returnMusic("meditation3", "Silent Mind")
        }
    }

    private fun returnMusic(fileName: String, displayName: String) {
        val resultIntent = Intent().apply {
            putExtra("selected_music", fileName)
            putExtra("display_name", displayName)
        }
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }
}
