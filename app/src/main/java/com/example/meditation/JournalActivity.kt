package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

class JournalActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_journal)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val journalEditText = findViewById<EditText>(R.id.edtJournal)
        val saveButton = findViewById<Button>(R.id.btnSaveJournal)
        val durationText = findViewById<TextView>(R.id.txtDurationInfo)

        val duration = intent.getIntExtra("duration_minutes", 10)
        durationText.text = "오늘의 명상 시간: ${duration}분"

        saveButton.setOnClickListener {
            val journalText = journalEditText.text.toString().trim()

            if (journalText.isEmpty()) {
                showDialog("입력 필요", "내용을 입력해주세요.")
                return@setOnClickListener
            }

            val uid = auth.currentUser?.uid
            if (uid == null) {
                showDialog("오류", "로그인이 필요합니다.")
                return@setOnClickListener
            }

            val journalData = hashMapOf(
                "content" to journalText,
                "timestamp" to FieldValue.serverTimestamp(),
                "duration_minutes" to duration
            )

            db.collection("users").document(uid).collection("journals")
                .add(journalData)
                .addOnSuccessListener {
                    AlertDialog.Builder(this)
                        .setTitle("기록 완료 🎉")
                        .setMessage("오늘의 마음이 잘 저장되었어요.\n홈으로 돌아갑니다.")
                        .setCancelable(false)
                        .setPositiveButton("확인") { _, _ ->
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        }
                        .show()
                }
                .addOnFailureListener { e ->
                    showDialog("저장 실패", "오류가 발생했어요: ${e.localizedMessage}")
                }
        }
    }

    private fun showDialog(title: String, message: String) {
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인", null)
            .show()
    }
}
