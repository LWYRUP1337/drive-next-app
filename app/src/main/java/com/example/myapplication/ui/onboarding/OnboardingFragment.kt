package com.example.myapplication.ui.onboarding

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.myapplication.R

class OnboardingFragment : Fragment(R.layout.fragment_onboarding) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = requireArguments()
        view.findViewById<ImageView>(R.id.ivSlide).setImageResource(args.getInt(ARG_IMAGE))
        view.findViewById<TextView>(R.id.tvTitle).setText(args.getInt(ARG_TITLE))
        view.findViewById<TextView>(R.id.tvDescription).setText(args.getInt(ARG_DESCRIPTION))
    }

    companion object {
        private const val ARG_IMAGE = "image"
        private const val ARG_TITLE = "title"
        private const val ARG_DESCRIPTION = "description"

        // Данные передаются через arguments, чтобы фрагмент пересоздавался после поворота экрана
        fun newInstance(imageRes: Int, titleRes: Int, descriptionRes: Int) = OnboardingFragment().apply {
            arguments = bundleOf(ARG_IMAGE to imageRes, ARG_TITLE to titleRes, ARG_DESCRIPTION to descriptionRes)
        }
    }
}
