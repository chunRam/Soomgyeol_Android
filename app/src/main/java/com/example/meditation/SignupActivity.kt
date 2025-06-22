package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.edtEmail)
        val passwordEditText = findViewById<EditText>(R.id.edtPassword)
        val signupButton = findViewById<Button>(R.id.btnSignup)

        signupButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (!isValidInput(email, password)) return@setOnClickListener

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val uid = auth.currentUser?.uid
                        val userInfo = hashMapOf(
                            "email" to email,
                            "created_at" to FieldValue.serverTimestamp()
                        )

                        uid?.let {
                            db.collection("users").document(it).set(userInfo)
                                .addOnSuccessListener {
                                    showToast("회원가입 성공!")
                                    moveToHome()
                                }
                                .addOnFailureListener { e ->
                                    showToast("사용자 정보 저장 실패: ${e.localizedMessage}")
                                }
                        }
                    } else {
                        handleAuthError(task.exception)
                    }
                }
        }
    }

    private fun isValidInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            showToast("이메일과 비밀번호를 입력해주세요.")
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showToast("이메일 형식이 올바르지 않습니다.")
            return false
        }
        if (password.length < 6) {
            showToast("비밀번호는 6자 이상이어야 합니다.")
            return false
        }
        return true
    }

    private fun handleAuthError(e: Exception?) {
        when (e) {
            is FirebaseAuthUserCollisionException -> showToast("이미 등록된 이메일입니다.")
            is FirebaseAuthWeakPasswordException -> showToast("비밀번호가 너무 약합니다.")
            else -> showToast("회원가입 실패: ${e?.localizedMessage}")
        }
    }

    private fun moveToHome() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
