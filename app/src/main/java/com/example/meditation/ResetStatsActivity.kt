package com.example.meditation

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ResetStatsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_stats)

        val resetButton = findViewById<Button>(R.id.btnResetStats)

        resetButton.setOnClickListener {
            val prefs = getSharedPreferences("MeditationStats", Context.MODE_PRIVATE)
            prefs.edit().clear().apply()  // 🔄 모든 통계 초기화

            Toast.makeText(this, "명상 기록이 초기화되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }
}
