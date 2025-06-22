package com.example.meditation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.meditation.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 💡 기존 감정 카드 클릭 이벤트 뒤에 추가하거나 바로 아래에 추가
        val cardJournal = findViewById<CardView>(R.id.cardJournal)
        cardJournal.setOnClickListener {
            val intent = Intent(this, HistoryActivity::class.java)
            startActivity(intent)
        }

        // ▶️ 감정 카드 클릭 이벤트
        binding.cardHappy.setOnClickListener {
            goToContentActivity("happy")
        }

        binding.cardNeutral.setOnClickListener {
            goToContentActivity("neutral")
        }

        binding.cardSad.setOnClickListener {
            goToContentActivity("sad")
        }

        // ▶️ 명상 시작 버튼 (기본 ContentActivity로 이동)
        binding.btnStartMeditation.setOnClickListener {
            startActivity(Intent(this, ContentActivity::class.java))
        }

        // 📊 통계 출력
        val prefs = getSharedPreferences("MeditationStats", MODE_PRIVATE)
        val sessionCount = prefs.getInt("session_count", 0)
        val totalTime = prefs.getInt("total_time_minutes", 0)

        binding.txtStats.text = "총 명상 시간: ${totalTime}분\n명상 횟수: ${sessionCount}회"
    }

    // ▶️ 감정 전달 후 ContentActivity 실행 함수
    private fun goToContentActivity(emotion: String) {
        val intent = Intent(this, ContentActivity::class.java)
        intent.putExtra("emotion", emotion)
        startActivity(intent)
    }
}
