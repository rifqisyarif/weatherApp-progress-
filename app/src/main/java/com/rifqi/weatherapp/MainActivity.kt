package com.rifqi.weatherapp

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rifqi.weatherapp.databinding.ActivityMainBinding
import com.rifqi.weatherapp.model.MainViewModel
import com.rifqi.weatherapp.utils.degreeToCelsius


class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private  val binding get() = _binding as ActivityMainBinding

    private var _viewModel: MainViewModel? = null
    private val viewModel get() = _viewModel as MainViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAsFullscreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = MainViewModel()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            1000
        )
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            viewModel.currentWeatherByCoordinate(it.latitude, it.longitude)
        }
        viewModel.getCurrentWeatherByCoordinate.observe(this) {
            binding.apply {
                tvCity.text = it.name
                Log.i("MainActivity", "${it.name}")
                tvDegree.text = degreeToCelsius(it.main?.temp)
                Log.i("MainActivity", "${degreeToCelsius(it.main?.temp)}")
            }
        }
    }

    private fun setAsFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetColor = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetColor?.isAppearanceLightStatusBars = true
    }
}