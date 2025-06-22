package com.example.meditation

import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.meditation.MeditationActivity

class ContentActivity : AppCompatActivity() {

    private val timeMap = mapOf(
        "1분" to 1 * 60 * 1000L,
        "3분" to 3 * 60 * 1000L,
        "5분" to 5 * 60 * 1000L,
        "10분" to 10 * 60 * 1000L
    )

    private val musicMap = mapOf(
        "명상 음악 1" to R.raw.meditation1,
        "명상 음악 2" to R.raw.meditation2,
        "명상 음악 3" to R.raw.meditation3
    )

    private val meditationTips = listOf(
        "편안한 자세로 등을 곧게 펴고 앉아보세요.",
        "눈을 감고 호흡에 집중해보세요.",
        "떠오르는 생각을 판단하지 말고 그냥 흘려보내세요.",
        "명상 중에는 아무것도 하지 않아도 괜찮아요.",
        "조용한 공간에서 3분만 나에게 집중해보세요."
    )

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)

        val emotion = intent.getStringExtra("emotion") ?: "unknown"

        val layout = findViewById<ConstraintLayout>(R.id.contentLayout)
        val txtMessage = findViewById<TextView>(R.id.txtEmotionMessage)
        val btnStart = findViewById<Button>(R.id.btnStartMeditation)
        val btnBack = findViewById<Button>(R.id.btnBackToEmotion)
        val spinnerTime = findViewById<MaterialAutoCompleteTextView>(R.id.spinnerTime)
        val spinnerMusic = findViewById<MaterialAutoCompleteTextView>(R.id.spinnerMusic)

        // 시간 스피너 설정
        val timeOptions = timeMap.keys.toList()
        val timeAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, timeOptions)
        spinnerTime.setAdapter(timeAdapter)

        spinnerTime.setOnClickListener { spinnerTime.showDropDown() }
        spinnerTime.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) spinnerTime.showDropDown() }

        // 음악 스피너 설정
        val musicOptions = musicMap.keys.toList()
        val musicAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, musicOptions)
        spinnerMusic.setAdapter(musicAdapter)

        spinnerMusic.setOnClickListener { spinnerMusic.showDropDown() }
        spinnerMusic.setOnFocusChangeListener { _, hasFocus -> if (hasFocus) spinnerMusic.showDropDown() }

        // 음악 미리듣기
        spinnerMusic.setOnItemClickListener { _, _, position, _ ->
            mediaPlayer?.stop()
            mediaPlayer?.release()
            val selectedKey = musicOptions[position]
            val musicResId = musicMap[selectedKey] ?: return@setOnItemClickListener
            mediaPlayer = MediaPlayer.create(this, musicResId)
            mediaPlayer?.start()
        }

        applyEmotionUI(emotion, layout, txtMessage)

        val defaultTime = when (emotion) {
            "happy" -> "3분"
            "neutral" -> "5분"
            "sad" -> "10분"
            else -> null
        }
        defaultTime?.let { spinnerTime.setText(it, false) }

        btnStart.setOnClickListener {
            val selectedTime = spinnerTime.text.toString()
            val selectedMusic = spinnerMusic.text.toString()
            val duration = timeMap[selectedTime] ?: 1 * 60 * 1000L
            val musicResId = musicMap[selectedMusic]

            if (selectedTime.isBlank() || musicResId == null || emotion == "unknown") {
                AlertDialog.Builder(this)
                    .setTitle("선택이 필요해요")
                    .setMessage("감정, 시간, 음악을 모두 선택해주세요.")
                    .setPositiveButton("확인", null)
                    .show()
                return@setOnClickListener
            }

            val prefs = getSharedPreferences("MeditationPrefs", MODE_PRIVATE)
            val hasSeenTip = prefs.getBoolean("has_seen_tip", false)

            if (hasSeenTip) {
                startMeditation(emotion, duration, musicResId)
            } else {
                val tip = meditationTips.random()
                AlertDialog.Builder(this)
                    .setTitle("명상 팁")
                    .setMessage("🧘 $tip\n\n준비되셨다면 '시작하기'를 눌러주세요.")
                    .setPositiveButton("시작하기") { _, _ ->
                        prefs.edit().putBoolean("has_seen_tip", true).apply()
                        startMeditation(emotion, duration, musicResId)
                    }
                    .setNegativeButton("취소", null)
                    .show()
            }
        }

        btnBack.setOnClickListener {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }
    }

    private fun startMeditation(emotion: String, duration: Long, musicResId: Int) {
        val intent = Intent(this, MeditationActivity::class.java)
        intent.putExtra("emotion", emotion)
        intent.putExtra("durationMillis", duration)
        intent.putExtra("musicResId", musicResId)
        startActivity(intent)
    }

    private fun applyEmotionUI(emotion: String, layout: ConstraintLayout, txtMessage: TextView) {
        when (emotion) {
            "happy" -> {
                layout.setBackgroundColor(Color.parseColor("#FFF9C4"))
                txtMessage.text = "기쁜 하루네요 😊\n당신의 웃음이 모두에게 전해지길!"
            }
            "neutral" -> {
                layout.setBackgroundColor(Color.parseColor("#E0F2F1"))
                txtMessage.text = "차분한 하루군요 😌\n숨을 고르고 나를 바라보는 시간이에요."
            }
            "sad" -> {
                layout.setBackgroundColor(Color.parseColor("#F8BBD0"))
                txtMessage.text = "마음이 무거운 날이네요 😢\n잠시 나를 위한 시간을 가져봐요."
            }
            else -> {
                layout.setBackgroundColor(Color.LTGRAY)
                txtMessage.text = "감정을 아직 선택하지 않으셨어요.\n이전 화면으로 돌아가 감정을 골라주세요."
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
    }
}
