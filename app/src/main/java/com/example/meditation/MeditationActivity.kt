package com.example.meditation

import android.animation.*
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.meditation.databinding.ActivityMeditationBinding
import com.example.meditation.viewmodel.MeditationViewModel

class MeditationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMeditationBinding
    private val viewModel: MeditationViewModel by viewModels()

    private var mediaPlayer: MediaPlayer? = null
    private var selectedMusic: String? = null
    private var durationInMinutes: Int = 5 // 기본값

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMeditationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // ✅ 전달받은 인텐트 정보
        val emotion = intent.getStringExtra("emotion") ?: "unknown"
        val durationMillis = intent.getLongExtra("durationMillis", 5 * 60 * 1000L)
        selectedMusic = intent.getStringExtra("selected_music") ?: "meditation1"
        durationInMinutes = (durationMillis / 60000).toInt()

        // ✅ 타이머 초기화
        viewModel.setInitialTime(durationMillis / 1000)

        // ✅ 감정 메시지 표시
        binding.txtEmotionMessage.text = when (emotion) {
            "happy" -> "😊 기쁜 감정과 함께 명상을 시작해볼까요?"
            "neutral" -> "😌 차분한 하루, 내면을 돌아볼 시간이에요."
            "sad" -> "😢 오늘도 수고했어요. 당신은 소중한 존재입니다."
            else -> "🧘 감정을 인식하며 명상을 시작해요."
        }

        // ✅ 명상 시작 버튼
        binding.btnStart.setOnClickListener {
            startMusic(selectedMusic ?: "meditation1")
            viewModel.startMeditation()
        }

        // ✅ 일시정지 버튼
        binding.btnPause.setOnClickListener {
            pauseMusic()
            viewModel.pauseMeditation()
        }

        // ✅ LiveData 관찰
        viewModel.remainingTime.observe(this, Observer {
            binding.txtTime.text = formatTime(it)
            updateProgress(it)
        })

        viewModel.isRunning.observe(this, Observer {
            binding.btnStart.isEnabled = !it
            binding.btnPause.isEnabled = it
        })

        viewModel.isFinished.observe(this, Observer { finished ->
            if (finished) {
                stopMusic()
                navigateToJournal()
            }
        })

        // ✅ 숨쉬기 애니메이션
        startBreathingAnimation()
    }

    // ✅ 음악 재생
    private fun startMusic(musicName: String) {
        stopMusic() // 중복 재생 방지

        val resId = resources.getIdentifier(musicName, "raw", packageName)
        if (resId != 0) {
            mediaPlayer = MediaPlayer.create(this, resId)
            mediaPlayer?.isLooping = true
            mediaPlayer?.start()
        } else {
            Toast.makeText(this, "음악 파일을 찾을 수 없습니다: $musicName", Toast.LENGTH_SHORT).show()
        }
    }

    private fun pauseMusic() {
        mediaPlayer?.pause()
    }

    private fun stopMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    private fun navigateToJournal() {
        val intent = Intent(this, JournalActivity::class.java)
        intent.putExtra("duration_minutes", durationInMinutes)
        startActivity(intent)
        finish()
    }

    private fun formatTime(seconds: Long): String {
        val m = seconds / 60
        val s = seconds % 60
        return "%02d:%02d".format(m, s)
    }

    private fun updateProgress(secondsLeft: Long) {
        val total = viewModel.initialTime.value ?: 1
        val progress = ((total - secondsLeft).toFloat() / total * 100).toInt()
        binding.progressMeditation.progress = progress
    }

    private fun startBreathingAnimation() {
        val breathingView = binding.imgBreathing
        val scaleUpX = ObjectAnimator.ofFloat(breathingView, View.SCALE_X, 1f, 1.3f)
        val scaleUpY = ObjectAnimator.ofFloat(breathingView, View.SCALE_Y, 1f, 1.3f)
        val fadeIn = ObjectAnimator.ofFloat(breathingView, View.ALPHA, 0.8f, 1f)

        val scaleDownX = ObjectAnimator.ofFloat(breathingView, View.SCALE_X, 1.3f, 1f)
        val scaleDownY = ObjectAnimator.ofFloat(breathingView, View.SCALE_Y, 1.3f, 1f)
        val fadeOut = ObjectAnimator.ofFloat(breathingView, View.ALPHA, 1f, 0.8f)

        val expandSet = AnimatorSet().apply {
            playTogether(scaleUpX, scaleUpY, fadeIn)
            duration = 2000
        }

        val contractSet = AnimatorSet().apply {
            playTogether(scaleDownX, scaleDownY, fadeOut)
            duration = 2000
        }

        AnimatorSet().apply {
            playSequentially(expandSet, contractSet)
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    startBreathingAnimation()
                }
            })
            start()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopMusic()
    }
}
