package us.zoom.sdksample.service

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import us.zoom.sdksample.Model.UserZoomItem
import us.zoom.sdksample.Model.zoom_list

interface ApiService {

    @POST("userHost")
    fun logIn(
        @Body body: HashMap<String, String>
    ): Call<UserZoomItem>

    @POST("postMeeting")
    fun postMeeting(
        @Body body: HashMap<String, String>
    ): Call<UserZoomItem>

    @POST("createInstant")
    fun postMeeting(@Body zoomlist: zoom_list?): Call<zoom_list?>?

}