package us.zoom.sdksample.ui;

import Adapter.DeviceAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.sdk.DirectShareStatus;
import us.zoom.sdk.IDirectShareServiceHelper;
import us.zoom.sdk.IDirectShareServiceHelperEvent;
import us.zoom.sdk.IDirectShareViaMeetingIDOrPairingCodeHandler;
import us.zoom.sdk.InstantMeetingOptions;
import us.zoom.sdk.MeetingError;
import us.zoom.sdk.MeetingService;
import us.zoom.sdk.MeetingServiceListener;
import us.zoom.sdk.MeetingStatus;
import us.zoom.sdk.PreMeetingService;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdk.ZoomSDKAuthenticationListener;
import us.zoom.sdksample.Model.Device;
import us.zoom.sdksample.initsdk.AuthConstants;
import us.zoom.sdksample.R;
import us.zoom.sdksample.inmeetingfunction.customizedmeetingui.MyMeetingActivity;
import us.zoom.sdksample.inmeetingfunction.customizedmeetingui.RawDataMeetingActivity;
import us.zoom.sdksample.inmeetingfunction.customizedmeetingui.SimpleZoomUIDelegate;
import us.zoom.sdksample.inmeetingfunction.customizedmeetingui.view.MeetingWindowHelper;
import us.zoom.sdksample.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper;
import us.zoom.sdksample.service.Api;
import us.zoom.sdksample.startjoinmeeting.LoginUserStartMeetingHelper;
import us.zoom.sdksample.startjoinmeeting.joinmeetingonly.JoinMeetingHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LoginUserStartJoinMeetingActivity extends Activity implements AuthConstants, MeetingServiceListener, ZoomSDKAuthenticationListener {

    private final static String TAG = "ZoomSDKExample";
    private LinearLayout linearLayout, linearLayoutSetting;
    private EditText mEdtMeetingNo;
    private EditText mEdtMeetingPassword;
    private EditText mEdtVanityId;
    private Button mBtnStartInstantMeeting;
    private Button mBtnLoginOut;
    private Button mBtnSettings;
    private Button mReturnMeeting;
    private Button mBtnDirectShare;
    private final static String DISPLAY_NAME = "ZoomUS SDK";

    private boolean mbPendingStartMeeting = false;
    private boolean isResumed = false;

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://zoom.ksta.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private Api api;
    List<Device> device;
    int device_id;
    private RecyclerView recyclerView;
    private DeviceAdapter adapter;
    //    private String TAG = "DeviceActivity";
    private Button startBtn;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.login_user_start_join);

        initUI();
        progressDialog = ProgressDialog.show(LoginUserStartJoinMeetingActivity.this, "Loading","Loading...", true, false);
        getAllDevice();
        registerListener();
    }

    private void initUI() {
        linearLayoutSetting = findViewById(R.id.linearSetting);
        linearLayout = findViewById(R.id.linearBtn);
        mEdtMeetingNo = (EditText) findViewById(R.id.edtMeetingNo);
        mEdtVanityId = (EditText) findViewById(R.id.edtVanityUrl);
        mEdtMeetingPassword = (EditText) findViewById(R.id.edtMeetingPassword);
        mBtnStartInstantMeeting = (Button) findViewById(R.id.btnLoginUserStartInstant);
        mBtnLoginOut = (Button) findViewById(R.id.btnLogout);
        mBtnSettings = findViewById(R.id.btn_settings);
        mBtnDirectShare = findViewById(R.id.btnShare);
        mReturnMeeting = findViewById(R.id.btn_return);
        mEdtMeetingNo.setVisibility(View.GONE);
        mEdtMeetingPassword.setVisibility(View.GONE);
        mEdtVanityId.setVisibility(View.GONE);
        mBtnLoginOut.setVisibility(View.GONE);
        mBtnSettings.setVisibility(View.INVISIBLE);
        mReturnMeeting.setVisibility(View.GONE);
        mBtnDirectShare.setVisibility(View.GONE);
        mBtnStartInstantMeeting.setVisibility(View.GONE);
        linearLayout.setVisibility(View.GONE);
        linearLayoutSetting.setVisibility(View.GONE);

        mBtnLoginOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickBtnLogout();
            }
        });


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
                    SharedPreferences sharedPreferences1 = getSharedPreferences("FROM", 0);
                    SharedPreferences.Editor editor1 = sharedPreferences1.edit();
                    editor1.putString("from", "");
                    editor1.commit();

                    SharedPreferences sharedPreferences2 = getSharedPreferences("PATIENT", 0);
                    SharedPreferences.Editor editor2 = sharedPreferences2.edit();
                    editor2.putString("patient", "");
                    editor2.commit();

//                    onClickBtnLoginUserStartInstant();
                    create(LoginUserStartJoinMeetingActivity.this);
                }
            }
        });
    }


    public int create(Context context) {
        int ret = -1;
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        zoomSDK.getMeetingSettingsHelper().setCustomizedMeetingUIEnabled(true);
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService == null) {
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

            meetingService.addListener(new MeetingServiceListener() {
                @Override
                public void onMeetingStatusChanged(MeetingStatus meetingStatus, int i, int i1) {
                    if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING){
                        System.out.println("from loginuser" +meetingService.getCurrentRtcMeetingNumber());

                    }
                }
            });
        return meetingService.startInstantMeeting(context, opts);
    }

    private void createList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DeviceAdapter(this, device);
        recyclerView.setAdapter(adapter);

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
                    progressDialog.dismiss();
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<Device>> call, Throwable t) {

            }
        });
    }

    private void registerListener() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        zoomSDK.addAuthenticationListener(this);
        MeetingService meetingService = zoomSDK.getMeetingService();
        if (meetingService != null) {
            meetingService.addListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResumed = true;
        refreshUI();
//        ZoomSDK.getInstance().getZoomUIService().setZoomUIDelegate(new SimpleZoomUIDelegate() {
//            @Override
//            public void afterMeetingMinimized(Activity activity) {
//                Intent intent = new Intent(activity, LoginUserStartJoinMeetingActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                activity.startActivity(intent);
//            }
//        });

        PreMeetingService preMeetingService = ZoomSDK.getInstance().getPreMeetingService();
        if (null == preMeetingService || !preMeetingService.getDirectShareService().canStartDirectShare()) {
            mBtnDirectShare.setVisibility(View.GONE);
        } else {
            mBtnDirectShare.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        isResumed = false;
    }

    @Override
    protected void onDestroy() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        zoomSDK.removeAuthenticationListener(this);//unregister ZoomSDKAuthenticationListener
        if (zoomSDK.isInitialized()) {
            MeetingService meetingService = zoomSDK.getMeetingService();
            meetingService.removeListener(this);//unregister meetingServiceListener
        }
        MeetingWindowHelper.getInstance().removeOverlayListener();

        super.onDestroy();
    }

    public void onClickBtnJoinMeeting(View view) {
        String meetingNo = mEdtMeetingNo.getText().toString().trim();
        String meetingPassword = mEdtMeetingPassword.getText().toString().trim();

        String vanityId = mEdtVanityId.getText().toString().trim();

        if (meetingNo.length() == 0 && vanityId.length() == 0) {
            Toast.makeText(this, "You need to enter a meeting number/ vanity id which you want to join.", Toast.LENGTH_LONG).show();
            return;
        }

        if (meetingNo.length() != 0 && vanityId.length() != 0) {
            Toast.makeText(this, "Both meeting number and vanity id have value,  just set one of them", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        MeetingService meetingService = zoomSDK.getMeetingService();


        int ret = -1;
        if (vanityId.length() != 0) {
            ret = JoinMeetingHelper.getInstance().joinMeetingWithVanityId(this, vanityId, meetingPassword);
        } else {
            ret = JoinMeetingHelper.getInstance().joinMeetingWithNumber(this, meetingNo, meetingPassword);
        }

        Log.i(TAG, "onClickBtnJoinMeeting, ret=" + ret);
    }

    public void onClickBtnStartMeeting() {
        String meetingNo = mEdtMeetingNo.getText().toString().trim();
        String vanityId = mEdtVanityId.getText().toString().trim();

        if (meetingNo.length() == 0 && vanityId.length() == 0) {
            Toast.makeText(this, "You need to enter a meeting number/ vanity  which you want to join.", Toast.LENGTH_LONG).show();
            return;
        }

        if (meetingNo.length() != 0 && vanityId.length() != 0) {
            Toast.makeText(this, "Both meeting number and vanity  have value,  just set one of them", Toast.LENGTH_LONG).show();
            return;
        }

        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        final MeetingService meetingService = zoomSDK.getMeetingService();

        if (meetingService.getMeetingStatus() != MeetingStatus.MEETING_STATUS_IDLE) {
            long lMeetingNo = 0;
            try {
                lMeetingNo = Long.parseLong(meetingNo);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid meeting number: " + meetingNo, Toast.LENGTH_LONG).show();
                return;
            }

            if (meetingService.getCurrentRtcMeetingNumber() == lMeetingNo) {
                meetingService.returnToMeeting(this);
                return;
            }

            new AlertDialog.Builder(this)
                    .setMessage("Do you want to leave current meeting and start another?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mbPendingStartMeeting = true;
                            meetingService.leaveCurrentMeeting(false);
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
            return;
        }

        int ret = -1;
        if (vanityId.length() != 0) {
            ret = LoginUserStartMeetingHelper.getInstance().startMeetingWithVanityId(this, vanityId);
        } else {
            ret = LoginUserStartMeetingHelper.getInstance().startMeetingWithNumber(this, meetingNo);
        }
        Log.i(TAG, "onClickBtnStartMeeting, ret=" + ret);
    }

    public void onClickBtnLoginUserStartInstant() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();

        if (!zoomSDK.isInitialized()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
            return;
        }

        int ret = LoginUserStartMeetingHelper.getInstance().startInstanceMeeting(this);

        Log.i(TAG, "onClickBtnLoginUserStartInstant, ret=" + ret);
    }

    public void onClickReturnMeeting(View view) {
        UIUtil.returnToMeeting(this);
    }

    public void onClickDirectShare(View view) {
        final IDirectShareServiceHelper shareServiceHelper = ZoomSDK.getInstance().getPreMeetingService().getDirectShareService();
        if (shareServiceHelper.isDirectShareInProgress()) {
            Toast.makeText(getBaseContext(), "isDirectShareInProgress", Toast.LENGTH_SHORT).show();
            return;
        }

        String meetingNumber = mEdtMeetingNo.getText().toString();

        final String pairCode = mEdtVanityId.getText().toString();
        if (TextUtils.isEmpty(meetingNumber) && TextUtils.isEmpty(pairCode)) {
            Toast.makeText(getBaseContext(), "meetingNumber or pairCode is null", Toast.LENGTH_SHORT).show();
            return;
        }

        long number = 0;
        if (TextUtils.isEmpty(pairCode)) {
            try {
                number = Long.parseLong(meetingNumber);
            } catch (Exception e) {
                return;
            }
        }
        final long tempNumber = number;
        shareServiceHelper.setEvent(new IDirectShareServiceHelperEvent() {
            @Override
            public void onDirectShareStatusUpdate(DirectShareStatus status, final IDirectShareViaMeetingIDOrPairingCodeHandler handler) {
                if (null != handler) {
                    if (status == DirectShareStatus.DirectShare_Need_MeetingID_Or_PairingCode) {
                        if (tempNumber != 0) {
                            handler.tryWithMeetingNumber(tempNumber);
                        } else {
                            String code = pairCode.toUpperCase();
                            Toast.makeText(getBaseContext(), "Code:" + code, Toast.LENGTH_SHORT).show();
                            handler.tryWithPairingCode(code);
                        }

                    } else if (status == DirectShareStatus.DirectShare_WrongMeetingID_Or_SharingKey) {
                        Toast.makeText(getBaseContext(), "Wrong Id or Key:", Toast.LENGTH_SHORT).show();
                        handler.cancel();
                    } else if (status == DirectShareStatus.DirectShare_Other_Error || status == DirectShareStatus.DirectShare_NetWork_Error) {
                        Toast.makeText(getBaseContext(), "Error:" + status, Toast.LENGTH_SHORT).show();
                        handler.cancel();
                    }
                }
                Log.d(TAG, "status:" + status + " :" + shareServiceHelper.isDirectShareInProgress());
            }
        });
        shareServiceHelper.startDirectShare();
    }


    public void onClickBtnLogout() {
        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (!zoomSDK.logoutZoom()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
        }
    }

    public void onClickSettings(View view) {
        Intent intent = new Intent(this, MeetingSettingActivity.class);
        startActivity(intent);
    }

    @Override
    public void onMeetingStatusChanged(MeetingStatus meetingStatus, int errorCode,
                                       int internalErrorCode) {
        Log.i(TAG, "onMeetingStatusChanged, meetingStatus=" + meetingStatus + ", errorCode=" + errorCode
                + ", internalErrorCode=" + internalErrorCode);

        if (meetingStatus == MeetingStatus.MEETING_STATUS_FAILED && errorCode == MeetingError.MEETING_ERROR_CLIENT_INCOMPATIBLE) {
            Toast.makeText(this, "Version of ZoomSDK is too low!", Toast.LENGTH_LONG).show();
        }

        if (mbPendingStartMeeting && meetingStatus == MeetingStatus.MEETING_STATUS_IDLE) {
            mbPendingStartMeeting = false;
            onClickBtnStartMeeting();
        }
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING) {
            if (ZoomMeetingUISettingHelper.useExternalVideoSource) {
                ZoomMeetingUISettingHelper.changeVideoSource(true);
            }
            showMeetingUi();
        }
        refreshUI();
    }

    private void refreshUI() {
        if (null == ZoomSDK.getInstance().getMeetingService()) {
            return;
        }
        MeetingStatus meetingStatus = ZoomSDK.getInstance().getMeetingService().getMeetingStatus();
        if (meetingStatus == MeetingStatus.MEETING_STATUS_CONNECTING || meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING
                || meetingStatus == MeetingStatus.MEETING_STATUS_RECONNECTING) {
            mBtnSettings.setVisibility(View.GONE);
            mReturnMeeting.setVisibility(View.VISIBLE);
        } else {
            mBtnSettings.setVisibility(View.VISIBLE);
            mReturnMeeting.setVisibility(View.GONE);
        }
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            if (meetingStatus == MeetingStatus.MEETING_STATUS_INMEETING && isResumed) {
                MeetingWindowHelper.getInstance().showMeetingWindow(this);
            } else {
                MeetingWindowHelper.getInstance().hiddenMeetingWindow(true);
            }
        }
    }

    private void showMeetingUi() {
        if (ZoomSDK.getInstance().getMeetingSettingsHelper().isCustomizedMeetingUIEnabled()) {
            SharedPreferences sharedPreferences = getSharedPreferences("UI_Setting", Context.MODE_PRIVATE);
            boolean enable = sharedPreferences.getBoolean("enable_rawdata", false);
            Intent intent = null;
            if (!enable) {
                intent = new Intent(this, MyMeetingActivity.class);
            } else {
                intent = new Intent(this, RawDataMeetingActivity.class);
            }
            intent.putExtra("from", MyMeetingActivity.JOIN_FROM_LOGIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            this.startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
//        LoginUserStartJoinMeetingActivity loginUserStartJoinMeetingActivity = new LoginUserStartJoinMeetingActivity();
//        loginUserStartJoinMeetingActivity.onClickBtnLogout();
    }

    @Override
    public void onZoomSDKLoginResult(long l) {

    }

    @Override
    public void onZoomSDKLogoutResult(long result) {
        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
            showLoginView();
        } else {
            Toast.makeText(this, "Logout failed result code = " + result, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onZoomIdentityExpired() {
        ZoomSDK.getInstance().logoutZoom();
    }

    @Override
    public void onZoomAuthIdentityExpired() {
    }

    private void showLoginView() {
        mEdtMeetingPassword.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ZoomSDK.getInstance().isInitialized()) {
                    Intent intent = new Intent(LoginUserStartJoinMeetingActivity.this, EmailUserLoginActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        }, 500);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        MeetingWindowHelper.getInstance().onActivityResult(requestCode, this);
    }
}
