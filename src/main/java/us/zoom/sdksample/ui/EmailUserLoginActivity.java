package us.zoom.sdksample.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Telephony;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import Adapter.SingleAdapter;
import Adapter.UserAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.sdk.JoinMeetingOptions;
import us.zoom.sdk.JoinMeetingParams;
import us.zoom.sdk.ZoomApiError;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdksample.Model.Posts;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;
import us.zoom.sdksample.inmeetingfunction.zoommeetingui.ZoomMeetingUISettingHelper;
import us.zoom.sdksample.otherfeatures.MyInviteActivity;
import us.zoom.sdksample.service.Api;

import us.zoom.sdksample.service.ApiObject;
import us.zoom.sdksample.startjoinmeeting.UserLoginCallback;
import us.zoom.sdksample.startjoinmeeting.emailloginuser.EmailUserLoginHelper;

public class EmailUserLoginActivity extends Activity implements UserLoginCallback.ZoomDemoAuthenticationListener, View.OnClickListener {

	private final static String TAG = "EmailActivity";
	
	private EditText mEdtUserName;
	private EditText mEdtPassord;
	private Button mBtnLogin;
	private View mProgressPanel;
	private RecyclerView recyclerView;
	private Button selectBtn;
	private ArrayList<User> userArrayList = new ArrayList<>();
	private UserAdapter adapter;
	private Api api;
	SharedPreferences sharedPreferences;
	ProgressDialog progressDialog;
	List<User> users;
	private EditText meetingNumEdt,nameEdt;
	private Button joinBtn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.email_login_activity);
		System.out.println("email_login");
//		ApiObject apiObject = new ApiObject();
//		apiObject.getRetrofit().create(Api.class);
		if (loadSharedPreference() == "leave"){
			AlertDialog.Builder builder;
			builder = new AlertDialog.Builder(this);
			builder.setMessage("Message") .setTitle("title");
			//Setting message manually and performing action on button click
			builder.setMessage("")
					.setCancelable(false)
					.setPositiveButton("Copy", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
						copy(EmailUserLoginActivity.this,"https://zoom.ksta.co/api/download/"+loadSharedPreferenceMeetingId());
						}
					})

					.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							//  Action for 'NO' Button
							dialog.cancel();
//							Toast.makeText(getApplicationContext(),"you choose no action for alertbox",
//									Toast.LENGTH_SHORT).show();
						}
					});
			//Creating dialog box
			AlertDialog alert = builder.create();
			//Setting the title manually
			alert.setTitle("Link for download");
			alert.setMessage("https://zoom.ksta.co/api/download/"+loadSharedPreferenceMeetingId());
			alert.show();

		}
		progressDialog = ProgressDialog.show(EmailUserLoginActivity.this,"Loading","Loading...",true,false);

		getUser();
		initUI();
		mEdtUserName = (EditText)findViewById(R.id.userName);
		mEdtPassord = (EditText)findViewById(R.id.password);

		mBtnLogin = (Button)findViewById(R.id.btnLogin);
		mBtnLogin.setOnClickListener(this);

		mEdtUserName.setVisibility(View.GONE);
		mEdtPassord.setVisibility(View.GONE);
		mBtnLogin.setVisibility(View.GONE);

		mProgressPanel = (View)findViewById(R.id.progressPanel);
	}
	public String loadSharedPreference() {
		SharedPreferences sharedPreferences = getSharedPreferences("FROM", 0);
		String from = sharedPreferences.getString("from", "");
		return from;
	}
	public String loadSharedPreferenceMeetingId() {
		SharedPreferences sharedPreferences = getSharedPreferences("MEETINGID", 0);
		String id = sharedPreferences.getString("meeting_id", "");
		return id;
	}
	public void copy(Context context, String url) {
		ClipboardManager clipboardManager = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
		ClipData clipData = ClipData.newHtmlText("Copied ", url, url);
		clipboardManager.setPrimaryClip(clipData);
	}
	public void joinMeeting(String number , String name){
		JoinMeetingParams params = new JoinMeetingParams();
		params.meetingNo = number;
		params.displayName = name;
		JoinMeetingOptions options=new JoinMeetingOptions();
		ZoomSDK.getInstance().getMeetingService().joinMeetingWithParams(this, params, ZoomMeetingUISettingHelper.getJoinMeetingOptions());
	}
	private void sharedPreferencesPatient(){
		SharedPreferences sharedPreferences = getSharedPreferences("PATIENT", 0);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString("patient", "true");
		editor.commit();
	}
	void initUI(){
		recyclerView = findViewById(R.id.userRecyclerView);
		meetingNumEdt = findViewById(R.id.meetingNumLogin);
		nameEdt = findViewById(R.id.passwordlogin);
		joinBtn = findViewById(R.id.joinBtn);

		selectBtn = findViewById(R.id.selectUser);

		joinBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				joinMeeting(meetingNumEdt.getText().toString(),nameEdt.getText().toString());
				sharedPreferencesPatient();
			}
		});
		selectBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (adapter.getSelected() != null){
//					showToast(adapter.getSelected().getName()+adapter.getSelected().getEmail());
					String  userName = adapter.getSelected().getEmail();
					String password = adapter.getSelected().getPassword();
					String uidLine = adapter.getSelected().getToken_line();
					String idHost = String.valueOf(adapter.getSelected().getId());
					int ret=EmailUserLoginHelper.getInstance().login(userName, password);
					if(!(ret== ZoomApiError.ZOOM_API_ERROR_SUCCESS)) {
						if (ret == ZoomApiError.ZOOM_API_ERROR_EMAIL_LOGIN_IS_DISABLED) {
//							Toast.makeText(this, "Account had disable email login ", Toast.LENGTH_LONG).show();
							showToast("Account had disable email login ");
						} else {
							showToast("ZoomSDK has not been initialized successfully or sdk is logging in.");
//							Toast.makeText(this, "ZoomSDK has not been initialized successfully or sdk is logging in.", Toast.LENGTH_LONG).show();
						}
					} else {
//						mBtnLogin.setVisibility(View.GONE);
//						mProgressPanel.setVisibility(View.VISIBLE);
						Intent intent = new Intent(EmailUserLoginActivity.this,ParticipantsActivity.class);
//					intent.putExtras(bundle);
//					intent.putExtra("list",(Serializable) users);
						intent.putExtra("tokenHost",uidLine);
						intent.putExtra("userName",userName);
						intent.putExtra("password",password);
						SharedPreferences sharedPreferences = getSharedPreferences("IDHOST",0);
						SharedPreferences.Editor editor = sharedPreferences.edit();
						editor.putString("idhost",idHost);
						editor.commit();
						try{
							startActivity(intent);
						}catch (Exception e){
							showToast("error");
						}

					}


				}else {
					showToast("no Selection");
				}
			}
		});
	}
	public void showToast(String msg){
		Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
	}
	private void createList() {
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		adapter = new UserAdapter(this, users);
		recyclerView.setAdapter(adapter);

		for (int i =0 ; i<20;i++){
//			User user = new User();
//			user.setName("User:"+(i+1));
//			userArrayList.add();
		}
	}

	private void getUser(){
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://zoom.ksta.co/api/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
		api = retrofit.create(Api.class);
		try{
			Call<List<User>> call = api.getUser();
			call.enqueue(new Callback<List<User>>() {
				@Override
				public void onResponse(Call<List<User>> call, Response<List<User>> response) {
					if (!response.isSuccessful()) {
						System.out.println("response not success");
						return;
					}
					users = response.body();

					createList();
					progressDialog.dismiss();
				}

				@Override
				public void onFailure(Call<List<User>> call, Throwable t) {
					System.out.println("response failed"+t);
				}
			});
		}catch (Exception e){
			System.out.println("response Catch"+e);
		}
	}
//	@Override
//	protected void onResume() {
//		super.onResume();
//
//		UserLoginCallback.getInstance().addListener(this);
//	}

	@Override
	protected void onPause() {
		super.onPause();

		UserLoginCallback.getInstance().removeListener(this);
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.btnLogin) {
			onClickBtnLogin();
		}
	}
	
	private void onClickBtnLogin() {
		String userName = mEdtUserName.getText().toString().trim();
		String password = mEdtPassord.getText().toString().trim();
		if(userName.length() == 0 || password.length() == 0) {
			Toast.makeText(this, "You need to enter user name and password.", Toast.LENGTH_LONG).show();
			return;
		}

		int ret=EmailUserLoginHelper.getInstance().login(userName, password);
		if(!(ret== ZoomApiError.ZOOM_API_ERROR_SUCCESS)) {
			if (ret == ZoomApiError.ZOOM_API_ERROR_EMAIL_LOGIN_IS_DISABLED) {
				Toast.makeText(this, "Account had disable email login ", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(this, "ZoomSDK has not been initialized successfully or sdk is logging in.", Toast.LENGTH_LONG).show();
			}
		} else {
			mBtnLogin.setVisibility(View.GONE);
			mProgressPanel.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onZoomSDKLoginResult(long result) {
		if(result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
			Toast.makeText(this, "Login successfully", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent(this, LoginUserStartJoinMeetingActivity.class);
			startActivity(intent);
			UserLoginCallback.getInstance().removeListener(this);
			finish();
		} else {
			Toast.makeText(this, "Login failed result code = " + result, Toast.LENGTH_SHORT).show();
		}
		mBtnLogin.setVisibility(View.VISIBLE);
		mProgressPanel.setVisibility(View.GONE);
	}

	@Override
	public void onZoomSDKLogoutResult(long result) {
		if(result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
			Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Logout failed result code = " + result, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onZoomIdentityExpired() {
		//Zoom identity expired, please re-login;
	}

	@Override
	public void onZoomAuthIdentityExpired() {

	}
}
