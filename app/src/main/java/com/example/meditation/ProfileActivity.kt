package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val txtUserEmail = findViewById<TextView>(R.id.txtUserEmail)
        val btnLogout = findViewById<Button>(R.id.btnLogout)

        // 현재 로그인된 유저의 이메일 표시
        val currentUser = FirebaseAuth.getInstance().currentUser
        txtUserEmail.text = "현재 로그인된 계정: ${currentUser?.email ?: "알 수 없음"}"

        // 로그아웃 버튼 클릭 시
        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }
}
