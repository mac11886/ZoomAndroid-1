package Adapter;

import android.content.Context;
import android.widget.Toast;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FunctionAll {


    public void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    public Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://bot.ksta.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
