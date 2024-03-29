package com.projects.weatherapplication.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.projects.weatherapplication.adapters.WeatherAdapter
import com.projects.weatherapplication.adapters.WeatherModel
import com.projects.weatherapplication.databinding.FragmentMainBinding
import org.json.JSONObject

const val API_KEY = "53847e2b546e42878f6153507240503"

class MainFragment : Fragment() {
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        initRcView()
        requestWeatherData("London")
    }


    private fun permissionListener(){
        pLauncher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "Permission is $it", Toast.LENGTH_LONG).show()
        }
    }

    private fun checkPermission(){
        if(!isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)){
            permissionListener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun initRcView() = with(binding){
        rcViewWidget.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rcViewWidget.adapter = adapter
        val list = listOf(
            WeatherModel("Tehran","17:45 PM","partly cloudy",
                "","12","˚С"),
            WeatherModel("Dubai","21:40 PM","Mostly sunny",
                "","31","˚С"),
            WeatherModel("London","12:00 AM","Mostly rainy",
                "","08","˚С"),
            WeatherModel("New York","08:27 AM","partly cloudy",
                "","04")
        )
        adapter.submitList(list)

    }

    private fun requestWeatherData(city: String){
       val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
               API_KEY +
               "&q=" +
               city +
               "&days=" +
               "3" +
               "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context)
        val request = StringRequest(
            Request.Method.GET,
            url,
            {
                result-> parseWeatherData(result)
            },
            {
                error -> Log.d("MyLog", "Error: $error")
            }
        )
        queue.add(request)
    }

    private fun parseWeatherData(result: String){
        val mainObject = JSONObject(result)
        val list = parseDays(mainObject)
        parseCurrentData(mainObject, list[0])

    }

    private fun parseDays(mainObject: JSONObject):List<WeatherModel>{
        val list = ArrayList<WeatherModel>()
        val daysArray = mainObject.getJSONObject("forecast")
            .getJSONArray("forecastday")
        val name = mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArray.length()) {
            val day = daysArray[i] as JSONObject
            val item = WeatherModel(
                name,
                day.getString("date"),
                day.getJSONObject("day")
                    .getJSONObject("condition")
                    .getString("text"),
                day.getJSONObject("day")
                    .getJSONObject("condition")
                    .getString("icon"),
                day.getString("uv"),
                day.getString("avghumidity"),
                day.getString("maxwind_kph"),
            "",
            "",
            "")

                day.getJSONArray("hour").toString()

            list.add(item)

        }
        return (list)
    }

    private fun parseCurrentData(mainObject: JSONObject, weatherItem: WeatherModel){

        val item = WeatherModel(
            mainObject.getJSONObject("location").getString("name"),
            mainObject.getJSONObject("location").getString("localtime"),
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("text"),
            mainObject.getJSONObject("current")
                .getJSONObject("condition").getString("icon"),
            mainObject.getJSONObject("current").getString("temp_c"),
            weatherItem.localtime,
            "",
            "",
            "",
            "",
            ""
        )
        Log.d("MyLog", "City: ${item.city}")
        Log.d("MyLog", "Time: ${item.localtime}")
        Log.d("MyLog", "Condition: ${item.condition}")
        Log.d("MyLog", "Temp: ${item.temp}")
        Log.d("MyLog", "URL: ${item.imageUrl}")
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()
    }
}