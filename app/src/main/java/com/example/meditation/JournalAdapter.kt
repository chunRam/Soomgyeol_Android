package com.example.meditation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.meditation.R
import com.example.meditation.model.JournalEntry
import java.text.SimpleDateFormat
import java.util.*

class JournalAdapter(private val entries: List<JournalEntry>) :
    RecyclerView.Adapter<JournalAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtContent: TextView = itemView.findViewById(R.id.txtJournalContent)
        val txtDate: TextView = itemView.findViewById(R.id.txtJournalDate)
        val txtDuration: TextView = itemView.findViewById(R.id.txtJournalDuration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_journal, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = entries.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val entry = entries[position]
        holder.txtContent.text = entry.content
        holder.txtDuration.text = "${entry.duration_minutes}분"

        entry.timestamp?.let {
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
            val dateStr = sdf.format(it.toDate())
            holder.txtDate.text = dateStr
        } ?: run {
            holder.txtDate.text = "날짜 없음"
        }
    }
}
