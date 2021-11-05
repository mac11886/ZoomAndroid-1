package us.zoom.sdksample.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.List;

import Adapter.DeviceAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.sdk.InstantMeetingOptions;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdksample.Model.Device;
import us.zoom.sdksample.Model.ZoomHost;
import us.zoom.sdksample.R;
import us.zoom.sdksample.service.Api;
import us.zoom.sdksample.startjoinmeeting.LoginUserStartMeetingHelper;

public class DeviceActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://zoom.ksta.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private Api api;
    List<Device> device;
    int device_id;
    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    private String TAG = "DeviceActivity";
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device);
        initUI();
        getAllDevice();


//        System.out.println(device);
    }

    void initUI() {
        recyclerView = findViewById(R.id.deviceRecyclerView);
        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (adapter.getSelected() != null) {
                    device_id = adapter.getSelected().getDevice_id();
                    SharedPreferences sharedPreferences = getSharedPreferences("DEVICE_ID", 0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("device_id", String.valueOf(device_id));
                    editor.commit();

                    create(DeviceActivity.this);
//                    LoginUserStartMeetingHelper loginUserStartMeetingHelper = new LoginUserStartMeetingHelper();
//                    loginUserStartMeetingHelper.startInstanceMeeting(DeviceActivity.this);
                }
            }
        });
    }

    public int create(Context context) {
        int ret = -1;
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        MeetingService meetingService = zoomSDK.getMeetingService();
        if(meetingService == null) {
            return ret;
        }

        InstantMeetingOptions opts = new InstantMeetingOptions();
//		opts.no_driving_mode = true;
//		opts.no_invite = true;
//		opts.no_meeting_end_message = true;
//		opts.no_titlebar = true;
//		opts.no_bottom_toolbar = true;
//		opts.no_dial_in_via_phone = true;
//		opts.no_dial_out_to_phone = true;
//		opts.no_disconnect_audio = true;
//		opts.no_share = true;

        return meetingService.startInstantMeeting(context, opts);
    }

    private void createList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(this, device);
        recyclerView.setAdapter(adapter);

    }

    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {
        Log.i(TAG, "onMeetingStatusChanged, meetingStatus=" + meetingStatus + ", errorCode=" + errorCode
                + ", internalErrorCode=" + internalErrorCode);

        if(meetingStatus == MeetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }
    }
    public void getAllDevice() {
        api = retrofit.create(Api.class);
        Call<List<Device>> call = api.getAllDevice();
        call.enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(Call<List<Device>> call, Response<List<Device>> response) {
                if (response.isSuccessful()) {
                    device = response.body();
                    createList();
//                    for (int i =0 ;i<device.size();i++){
//                        System.out.println(device.get(i).getName());
//                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {

            }
        });
    }
}