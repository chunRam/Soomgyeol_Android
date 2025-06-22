package com.example.meditation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

import com.example.meditation.model.JournalEntry

class JournalViewModel : ViewModel() {

    private val _journalList = MutableLiveData<List<JournalEntry>>()
    val journalList: LiveData<List<JournalEntry>> = _journalList

    fun loadJournals() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("users")
            .document(userId)
            .collection("journals")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) return@addSnapshotListener
                val entries = snapshot.documents.mapNotNull {
                    it.toObject(JournalEntry::class.java)
                }
                _journalList.value = entries
            }
    }
}
