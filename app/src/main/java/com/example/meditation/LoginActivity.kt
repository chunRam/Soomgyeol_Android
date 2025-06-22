package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

// 🔐 로그인 화면을 구성하는 Activity
class LoginActivity : AppCompatActivity() {

    // Firebase 인증 객체 (클래스 전체에서 사용 가능하도록 선언)
    private lateinit var auth: FirebaseAuth

    // 🔁 앱 실행 시 이미 로그인된 사용자가 있다면 자동으로 홈 화면으로 이동
    override fun onStart() {
        super.onStart()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            // 이미 로그인되어 있는 사용자 → 바로 홈 화면으로
            startActivity(Intent(this, HomeActivity::class.java))
            finish() // LoginActivity는 백스택에서 제거
        }
    }

    // 🧱 Activity가 생성될 때 실행되는 부분
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // XML 레이아웃 연결

        // Firebase 인증 객체 초기화
        auth = FirebaseAuth.getInstance()

        // UI 요소 연결 (EditText, Button)
        val emailEditText = findViewById<EditText>(R.id.edtEmail)
        val passwordEditText = findViewById<EditText>(R.id.edtPassword)
        val loginButton = findViewById<Button>(R.id.btnLogin)

        // ✅ 회원가입 텍스트뷰 연결
        val signupText = findViewById<TextView>(R.id.txtSignup)

        // ✅ 회원가입 텍스트 클릭 시 → SignupActivity 로 전환
        signupText.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }



        // 로그인 버튼 클릭 시 실행할 동작 정의
        loginButton.setOnClickListener {
            // 사용자 입력값 읽어오기 (앞뒤 공백 제거)
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            // 입력값이 비어 있는지 확인
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener // 클릭 이벤트 종료
            }

            // ✅ Firebase 로그인 시도
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // 로그인 성공 시 → 홈 화면으로 이동
                        Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this, MainActivity::class.java))
                        finish() // 로그인 화면 종료
                    } else {
                        // 로그인 실패 시 → 에러 메시지 표시
                        Toast.makeText(
                            this,
                            "로그인 실패: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
}
