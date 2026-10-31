package com.example.myapplication.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.myapplication.R

class OnboardingPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val slides = listOf(
        Triple(R.drawable.onboarding_1, R.string.onboarding_title_1, R.string.onboarding_desc_1),
        Triple(R.drawable.onboarding_2, R.string.onboarding_title_2, R.string.onboarding_desc_2),
        Triple(R.drawable.onboarding_3, R.string.onboarding_title_3, R.string.onboarding_desc_3)
    )

    override fun getItemCount(): Int = slides.size

    override fun createFragment(position: Int): Fragment {
        val (image, title, description) = slides[position]
        return OnboardingFragment.newInstance(image, title, description)
    }
}
