package com.example.myapplication.ui.settings

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.ui.common.showAvatar
import com.example.myapplication.ui.main.MainActivity
import com.example.myapplication.ui.profile.ProfileFragment
import com.example.myapplication.ui.profile.ProfileViewModel

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val viewModel: ProfileViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val name = view.findViewById<TextView>(R.id.tvName)
        val email = view.findViewById<TextView>(R.id.tvEmail)
        val avatar = view.findViewById<ImageView>(R.id.ivAvatar)

        viewModel.profile.observe(viewLifecycleOwner) { profile ->
            name.text = getString(R.string.full_name, profile.firstName, profile.lastName).trim()
            email.text = profile.email
        }
        viewModel.avatar.observe(viewLifecycleOwner) { avatar.showAvatar(it, R.drawable.ic_person) }

        view.findViewById<View>(R.id.profileRow).setOnClickListener {
            (requireActivity() as MainActivity).openScreen(ProfileFragment())
        }
    }
}
