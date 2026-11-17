package com.example.myapplication.ui.main

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.myapplication.R
import com.example.myapplication.ui.favorites.FavoritesFragment
import com.example.myapplication.ui.home.HomeFragment
import com.example.myapplication.ui.settings.SettingsFragment

class MainActivity : AppCompatActivity() {

    enum class Tab(val buttonId: Int, val icon: Int, val activeIcon: Int) {
        HOME(R.id.tabHome, R.drawable.ic_nav_home, R.drawable.ic_nav_home_active),
        FAVORITES(R.id.tabFavorites, R.drawable.ic_nav_bookmark, R.drawable.ic_nav_bookmark_active),
        SETTINGS(R.id.tabSettings, R.drawable.ic_nav_settings, R.drawable.ic_nav_settings_active)
    }

    private var currentTab = Tab.HOME

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Tab.entries.forEach { tab ->
            findViewById<ImageView>(tab.buttonId).setOnClickListener { selectTab(tab) }
        }
        if (savedInstanceState == null) {
            selectTab(Tab.HOME)
        } else {
            currentTab = Tab.valueOf(savedInstanceState.getString(KEY_TAB, Tab.HOME.name))
            updateTabs()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_TAB, currentTab.name)
    }

    fun openScreen(fragment: Fragment) {
        supportFragmentManager.commit {
            replace(R.id.container, fragment)
            addToBackStack(null)
        }
    }

    private fun selectTab(tab: Tab) {
        currentTab = tab
        supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        val fragment = when (tab) {
            Tab.HOME -> HomeFragment()
            Tab.FAVORITES -> FavoritesFragment()
            Tab.SETTINGS -> SettingsFragment()
        }
        supportFragmentManager.commit { replace(R.id.container, fragment) }
        updateTabs()
    }

    private fun updateTabs() {
        Tab.entries.forEach { tab ->
            findViewById<ImageView>(tab.buttonId)
                .setImageResource(if (tab == currentTab) tab.activeIcon else tab.icon)
        }
    }

    private companion object {
        const val KEY_TAB = "tab"
    }
}
