package us.zoom.sdksample.service;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import us.zoom.sdksample.Model.Device;
import us.zoom.sdksample.Model.Posts;
import us.zoom.sdksample.Model.UrlZoom;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.Model.zoom_list;

public interface Api {

    @POST("createInstant")
    Call<zoom_list> postMeeting(@Body zoom_list zoomlist) ;

    @GET("userHost")
    Call<List<User>> getUser() ;

    @GET("posts")
    Call<List<Posts>> getPost() ;

    @GET("getDevice")
    Call<List<Device>> getAllDevice();

    @POST("endMeetingAndroid")
    Call<String> endMeeting();

    @FormUrlEncoded
    @POST("line/sendUrlZoom")
    Call<String> sendUrl(@Field("uid")String uid
                         ,@Field("url")String url) ;
}
