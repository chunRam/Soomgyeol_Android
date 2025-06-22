package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MeditationEndActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_meditation_end)

        val emotion = intent.getStringExtra("emotion") ?: "unknown"
        val txtMessage = findViewById<TextView>(R.id.txtEndMessage)
        val btnHome = findViewById<Button>(R.id.btnReturnHome)

        txtMessage.text = when (emotion) {
            "happy" -> "😊 기쁜 감정과 함께\n잘 마무리했어요!"
            "neutral" -> "😌 차분한 하루를\n의미 있게 보냈네요."
            "sad" -> "😢 오늘도 수고했어요.\n당신은 참 잘했어요."
            else -> "🧘 명상을 마쳤어요.\n수고하셨어요!"
        }

        btnHome.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }
}
