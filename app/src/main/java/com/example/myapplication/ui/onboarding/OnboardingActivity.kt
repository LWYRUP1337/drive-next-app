package com.example.myapplication.ui.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.ui.welcome.WelcomeActivity

class OnboardingActivity : AppCompatActivity() {

    private val viewModel: OnboardingViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val btnNext = findViewById<Button>(R.id.btnNext)
        val indicators = listOf<View>(findViewById(R.id.ind1), findViewById(R.id.ind2), findViewById(R.id.ind3))
        val adapter = OnboardingPagerAdapter(this)
        viewPager.adapter = adapter

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                indicators.forEachIndexed { index, view -> updateIndicator(view, index == position) }
                val isLast = position == adapter.itemCount - 1
                btnNext.setText(if (isLast) R.string.lets_go else R.string.next)
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < adapter.itemCount - 1) {
                viewPager.setCurrentItem(viewPager.currentItem + 1, true)
            } else {
                finishOnboarding()
            }
        }
        findViewById<TextView>(R.id.tvSkip).setOnClickListener { finishOnboarding() }
    }

    private fun updateIndicator(view: View, isActive: Boolean) {
        val widthRes = if (isActive) R.dimen.indicator_active_width else R.dimen.indicator_width
        view.layoutParams = view.layoutParams.apply { width = resources.getDimensionPixelSize(widthRes) }
        view.setBackgroundResource(if (isActive) R.drawable.indicator_active else R.drawable.indicator_inactive)
    }

    private fun finishOnboarding() {
        viewModel.finishOnboarding()
        startActivity(Intent(this, WelcomeActivity::class.java))
        finish()
    }
}
