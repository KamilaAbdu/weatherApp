package com.projects.weatherapplication

import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.projects.weatherapplication.adapters.WeatherModel

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<String>()
    val liveDataList = MutableLiveData<List<WeatherModel>>()

}