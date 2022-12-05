package com.rifqi.weatherapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetColor = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetColor?.isAppearanceLightStatusBars = true

        setContentView(R.layout.activity_main)
    }
}