package com.example.meditation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logoView = findViewById<ImageView>(R.id.logoView)

        // scaleX, scaleY 애니메이터 불러오기
        val scaleX = AnimatorInflater.loadAnimator(this, R.animator.scale_up_x) as ObjectAnimator
        val scaleY = AnimatorInflater.loadAnimator(this, R.animator.scale_up_y) as ObjectAnimator

        scaleX.setTarget(logoView)
        scaleY.setTarget(logoView)

        // 동시에 실행되도록 묶기
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY)

        // 애니메이션 끝나면 HomeActivity로 이동
        animatorSet.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {}
            override fun onAnimationEnd(animation: Animator) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
            override fun onAnimationCancel(animation: Animator) {}
            override fun onAnimationRepeat(animation: Animator) {}
        })

        animatorSet.start()
    }
}
