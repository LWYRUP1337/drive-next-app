package com.example.myapplication.ui.profile

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplication.R
import com.example.myapplication.domain.UserProfile
import com.example.myapplication.ui.common.openAsRoot
import com.example.myapplication.ui.common.showAvatar
import com.example.myapplication.ui.login.LoginActivity

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val viewModel: ProfileViewModel by activityViewModels()

    private val pickAvatar = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        if (uri != null) viewModel.changeAvatar(uri)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val avatar = view.findViewById<ImageView>(R.id.ivAvatar)

        viewModel.profile.observe(viewLifecycleOwner) { showProfile(view, it) }
        viewModel.avatar.observe(viewLifecycleOwner) { avatar.showAvatar(it, R.drawable.ic_person) }
        viewModel.signedOut.observe(viewLifecycleOwner) { signedOut ->
            if (signedOut) requireActivity().openAsRoot(LoginActivity::class.java)
        }

        view.findViewById<View>(R.id.avatar).setOnClickListener {
            pickAvatar.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
        view.findViewById<View>(R.id.tvSignOut).setOnClickListener { viewModel.signOut() }
    }

    private fun showProfile(view: View, profile: UserProfile) {
        val month = resources.getStringArray(R.array.months_prepositional)[profile.joinedAt.monthValue - 1]
        val joined = if (profile.gender == "female") R.string.joined_female else R.string.joined_male
        view.findViewById<TextView>(R.id.tvName).text =
            getString(R.string.full_name, profile.firstName, profile.lastName).trim()
        view.findViewById<TextView>(R.id.tvJoined).text = getString(joined, month, profile.joinedAt.year)
        view.findViewById<TextView>(R.id.tvEmail).text = profile.email
        view.findViewById<TextView>(R.id.tvGender).setText(
            when (profile.gender) {
                "male" -> R.string.male
                "female" -> R.string.female
                else -> R.string.not_specified
            }
        )
    }
}
