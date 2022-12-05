package com.rifqi.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rifqi.weatherapp.databinding.RowItemWeatherBinding
import com.rifqi.weatherapp.model.ListItem
import com.rifqi.weatherapp.utils.degreeToCelsius
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.MyViewHolder>() {

    private val listWeather = ArrayList<ListItem>()

    class MyViewHolder(val binding: RowItemWeatherBinding) : RecyclerView.ViewHolder(binding.root)

    // Mengaitkan / menghubungkan layout row_item_weather dengan RecyclerView.Adapter
    // Jadi class WeatherAdapter dapat mengakses seluruh View/ViewGroup yang ada di layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = MyViewHolder(
        RowItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    // Mengikat data/konten dari list_data ke setiap view (TextView/Image/dsb)
    // yang ada di layout row_item_weather
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = listWeather[position]
        holder.binding.apply {
            val date = data.dtTxt?.take(10) // yyyy-MM-dd
            val time = data.dtTxt?.takeLast(8) // hh:mm:ss

            val dateArray = date?.split('-')?.toTypedArray() // [yyyy-MM-dd]
            val timeArray = time?.split(':')?.toTypedArray() // [hh:mm:ss]

            // dateFormat will looks like Tue, Aug 30
            val dateFormat = SimpleDateFormat("EEE, MMM d", Locale.getDefault())
            // timeFormat will looks like 6:00 AM
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())

            val calendar = Calendar.getInstance()
            // Date
            calendar.set(Calendar.YEAR, Integer.parseInt(dateArray?.get(0) as String)) // dengan cara casting (as)
            calendar.set(Calendar.MONTH, dateArray[1].toInt()) // dengan cara convert (toInt())
            calendar.set(Calendar.DAY_OF_MONTH, dateArray[2].toInt())

            // Time
            calendar.set(Calendar.HOUR_OF_DAY, (timeArray?.get(0) as String).toInt())
            calendar.set(Calendar.MINUTE, timeArray[1].toInt())

            // dari tgl skrng di convert -> Thu, Dec 01
            val dateResult = dateFormat.format(calendar.time).toString()
            // dari waktu skrng di convert-> 6:00 AM
            val timeResult = timeFormat.format(calendar.time).toString()

            tvItemDate.text = dateResult
            tvItemTime.text = timeResult

            val maxTemp = "Max " + degreeToCelsius(data.main?.tempMax)
            val minTemp = "Min " + degreeToCelsius(data.main?.tempMin)
            tvMaxDegree.text = maxTemp
            tvMinDegree.text = minTemp
        }
    }

    // Menghitung/menentukan jumlah data yang ada di list_data untuk memberitahukan agar
    // adapter menyediakan layout row_item sejumlah data tersebut
    override fun getItemCount() = listWeather.size

}
