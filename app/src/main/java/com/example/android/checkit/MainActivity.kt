package com.example.android.checkit

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize logger
        Timber.plant(Timber.DebugTree())

        // Set adapter to view pager
        viewpager.adapter = CategoryFragmentPagerAdapter(supportFragmentManager,
                this@MainActivity)

        // Give the TabLayout the ViewPager
        sliding_tabs.setupWithViewPager(viewpager)
    }
}
