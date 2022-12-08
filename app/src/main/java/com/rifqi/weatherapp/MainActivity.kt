package com.rifqi.weatherapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.rifqi.weatherapp.databinding.ActivityMainBinding
import com.rifqi.weatherapp.model.CurrentWeatherResponse
import com.rifqi.weatherapp.model.ForecastWeatherResponse
import com.rifqi.weatherapp.utils.ICON_BASE_URL
import com.rifqi.weatherapp.utils.ICON_SIZE_4X
import com.rifqi.weatherapp.utils.degreeToCelsius
import java.util.*


class MainActivity : AppCompatActivity(), LocationListener {

    private var _binding: ActivityMainBinding? = null
    private  val binding get() = _binding as ActivityMainBinding

    private var _viewModel: MainViewModel? = null
    private val viewModel get() = _viewModel as MainViewModel

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var _latitude: Double? = null
    private val latitude get() = _latitude as Double
    private var _longtitude: Double? = null
    private val longtitude get() = _longtitude as Double

    private var isResultFromSearch: Boolean = false
    private var isLoading: Boolean? = null

    lateinit var locManager: LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setAsFullscreen()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        _viewModel = MainViewModel()

        searchCity()

        getWeatherByCoord()
        getWeatherByCity()
    }

    private fun getWeatherByCity() {
        viewModel.getCurrentWeatherByCity.observe(this) {
            setupView(it, null)
        }
        viewModel.getForecastWeatherByCity.observe(this) {
            setupView(null, it)
        }
    }

    private fun searchCity() {
        binding.searchCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    isLoading = true
                    loadingStateView()
                    try {
                        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                        inputMethodManager.hideSoftInputFromWindow(binding.root.windowToken, 0)
                    } catch (t: Throwable) {
                        Log.e("MainActivty", "onQueryTextSubmit: ${t.message}", )
                    }
                    viewModel.currentWeatherByCity(it)
                    viewModel.forecastWeatherByCity(it)
                }
                loadingStateView()
                isResultFromSearch = true
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    viewModel.currentWeatherByCity(it)
                    viewModel.forecastWeatherByCity(it)
                }
                isLoading = false
                loadingStateView()
                isResultFromSearch = true
                return true
            }

        })
    }

    private fun getWeatherByCoord() {
        isLoading = true
        loadingStateView()
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
        val location = fusedLocationClient.lastLocation
        location.addOnSuccessListener {
            _latitude = it.latitude
            _longtitude = it.longitude
            viewModel.currentWeatherByCoordinate(-6.525365, 107.03811)
            viewModel.forecastWeatherByCoordinate(-6.525365, 107.03811)
        }
        if (location != null) {
            location.addOnSuccessListener {
                viewModel.currentWeatherByCoordinate(it.latitude, it.longitude)
            }
        } else {
            locManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val bestProvider = locManager.getBestProvider(Criteria(), true).toString()
            locManager.requestLocationUpdates(bestProvider, 1000L, 0f, this)
        }

        viewModel.getCurrentWeatherByCoordinate.observe(this) { data ->
            setupView(data, null)
        }

        viewModel.getForecastWeatherByCoordinate.observe(this) {
            setupView(null, it)
            isLoading = false
            loadingStateView()
        }
    }

    private fun loadingStateView() {
        binding.apply {
            when (isLoading) {
                true -> {
                    layoutWeather.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                } false -> {
                    layoutWeather.visibility = View.VISIBLE
                    progressBar.visibility = View.INVISIBLE
                } else -> {
                    layoutWeather.visibility = View.INVISIBLE
                    progressBar.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun setupView(weather: CurrentWeatherResponse?, forecast: ForecastWeatherResponse?) {
        weather?.let {
            binding.apply {

                when (isResultFromSearch) {
                    true -> tvCity.text = it.name
                    else -> {
                        val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                        val address = geocoder.getFromLocation(-6.525365, 107.03811, 1)
                        val addressResult = address?.get(0)?.getAddressLine(0)
                        Log.i("MainActivty", "getCurrentWeatherByCoord: $addressResult")

                        val city = address?.get(0)?.locality

                        tvCity.text = city
                        Log.i("MainActivity", "${it.name} & city from Geocoder $city")
                    }
                }


                val temp = "${it.main?.temp}°C"
                tvDegree.text = temp
                Log.i("MainActivity", "${degreeToCelsius(it.main?.temp)}")

                val imageUrl = ICON_BASE_URL + it.weather?.get(0)?.icon + ICON_SIZE_4X
                Glide.with(this@MainActivity).load(imageUrl).into(imgIcWeather)

                setupBackgroundWeather(it.weather?.get(0)?.id, it.weather?.get(0)?.icon)

            }
        }

        forecast?.let {
            val mAdapter = WeatherAdapter()
            mAdapter.setData(it.list)
            Log.i("MainActivty", "getForecastWeatherByCoord: ${it.list} ")
            binding.rvWeather.apply {
                layoutManager = LinearLayoutManager(
                    this@MainActivity,
                    LinearLayoutManager.HORIZONTAL, false
                )
                adapter = mAdapter
            }
        }

    }

    private fun setupBackgroundWeather(id: Int?, icon: String?) {
        id?.let {
            when(id) {
                in resources.getIntArray(R.array.thunderstorm_id_list) -> setImageBackground(R.drawable.thunderstorm)
                in resources.getIntArray(R.array.drizzle_id_list) -> setImageBackground(R.drawable.drizzle)
                in resources.getIntArray(R.array.rain_id_list) -> setImageBackground(R.drawable.rain)
                in resources.getIntArray(R.array.freezing_rain_id_list) -> setImageBackground(R.drawable.freezing_rain)
                in resources.getIntArray(R.array.snow_id_list) -> setImageBackground(R.drawable.snow)
                in resources.getIntArray(R.array.sleet_id_list) -> setImageBackground(R.drawable.sleet)
                in resources.getIntArray(R.array.clear_id_list) -> {
                    when (icon) {
                        "01d" -> setImageBackground(R.drawable.clear)
                        "01d" -> setImageBackground(R.drawable.clear_night)
                    }
                }
                in resources.getIntArray(R.array.clouds_id_list) -> setImageBackground(R.drawable.lightcloud)
                in resources.getIntArray(R.array.heavy_clouds_id_list) -> setImageBackground(R.drawable.heavycloud)
                in resources.getIntArray(R.array.fog_id_list) -> setImageBackground(R.drawable.fog)
                in resources.getIntArray(R.array.sand_id_list) -> setImageBackground(R.drawable.sand)
                in resources.getIntArray(R.array.dust_id_list) -> setImageBackground(R.drawable.dust)
                in resources.getIntArray(R.array.volcanic_ash_id_list) -> setImageBackground(R.drawable.volcanic)
                in resources.getIntArray(R.array.squalls_id_list) -> setImageBackground(R.drawable.squalls)
                in resources.getIntArray(R.array.tornado_id_list) -> setImageBackground(R.drawable.tornado)

            }
        }
    }

    private fun setImageBackground(image: Int) {
        Glide.with(this).load(image).into(binding.imgBgWeather)
    }

    private fun setAsFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetColor = ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetColor?.isAppearanceLightStatusBars = true
    }

    override fun onLocationChanged(p0: Location) {
        locManager.removeUpdates(this)
    }
}