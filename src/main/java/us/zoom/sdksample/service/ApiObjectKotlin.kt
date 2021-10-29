package us.zoom.sdksample.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiObjectKotlin {

    var apiObjects = Retrofit.Builder()
        .baseUrl("https://zoom.ksta.co/api/").addConverterFactory(
            GsonConverterFactory.create()).build()
}