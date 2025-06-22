package com.example.meditation

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.meditation.adapter.JournalAdapter
import com.example.meditation.model.JournalEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class HistoryActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: JournalAdapter
    private val entries = mutableListOf<JournalEntry>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        val txtStats = findViewById<TextView>(R.id.txtStats)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = JournalAdapter(entries)
        recyclerView.adapter = adapter

        loadJournalsFromFirestore(txtStats)
    }

    private fun loadJournalsFromFirestore(txtStats: TextView) {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(uid)
            .collection("journals")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { result ->
                entries.clear()
                var totalTime = 0
                for (doc in result) {
                    val item = doc.toObject(JournalEntry::class.java)
                    totalTime += item.duration_minutes
                    entries.add(item)
                }
                adapter.notifyDataSetChanged()

                txtStats.text = "총 명상 시간: ${totalTime}분 / 총 횟수: ${entries.size}회"
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "불러오기 실패: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }
}
