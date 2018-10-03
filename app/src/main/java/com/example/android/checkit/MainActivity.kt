package com.example.android.checkit

import android.content.Intent
import android.content.SharedPreferences
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.Menu
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    // Keys and IDs
    val SHARED_PREFS_ISFIRSTRUN_KEY = "shared-prefs-isfirstrun-key"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set the "Is First Run" sharedprefs boolean as true
        val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(SHARED_PREFS_ISFIRSTRUN_KEY, true)
        editor.apply()

        // Initialize logger
        Timber.plant(Timber.DebugTree())

        // Set adapter to view pager
        viewpager.adapter = CategoryFragmentPagerAdapter(supportFragmentManager,
                this@MainActivity)

        // Give the TabLayout the ViewPager
        sliding_tabs.setupWithViewPager(viewpager)
    }
}
