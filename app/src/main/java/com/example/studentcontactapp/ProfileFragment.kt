package com.example.studentcontactapp

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import utils.PrefManager

class ProfileFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.profile_fragment, container, false)
        val pref = PrefManager(requireContext())

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            pref.logout()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        return view
    }
}