package com.example.meditation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val txtUserEmail = view.findViewById<TextView>(R.id.txtUserEmail)
        val btnLogout = view.findViewById<Button>(R.id.btnLogout)

        val currentUser = FirebaseAuth.getInstance().currentUser
        txtUserEmail.text = "이메일: ${currentUser?.email ?: "알 수 없음"}"

        btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()

            // 로그아웃 후 LoginActivity로 이동
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}
