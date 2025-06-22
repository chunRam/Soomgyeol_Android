package com.example.meditation

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class StatsActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val sessionsText = findViewById<TextView>(R.id.txtTotalSessions)
        val timeText = findViewById<TextView>(R.id.txtTotalTime)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val uid = auth.currentUser?.uid
        if (uid == null) {
            sessionsText.text = "로그인이 필요합니다."
            timeText.text = ""
            return
        }

        db.collection("users").document(uid).collection("journals")
            .get()
            .addOnSuccessListener { documents ->
                val totalSessions = documents.size()
                var totalMinutes = 0L

                for (doc in documents) {
                    val minutes = doc.getLong("duration_minutes") ?: 5L
                    totalMinutes += minutes
                }

                sessionsText.text = "총 명상 횟수: ${totalSessions}회"
                timeText.text = "총 명상 시간: ${totalMinutes}분"
            }
            .addOnFailureListener {
                sessionsText.text = "통계 불러오기 실패"
                timeText.text = ""
            }
    }
}
