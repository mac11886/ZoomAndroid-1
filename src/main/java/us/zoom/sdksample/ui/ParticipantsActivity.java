package us.zoom.sdksample.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.FunctionAll;
import Adapter.MemberAdapter;
import Adapter.ParticipantsAdapter;
import Adapter.TeamAdapter;
import Adapter.UserAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.R;
import us.zoom.sdksample.service.Api;
import us.zoom.sdksample.startjoinmeeting.LoginUserStartMeetingHelper;

public class ParticipantsActivity extends AppCompatActivity {

    List<User> users;
    List<String> uidLine  =new ArrayList<>();;
    private RecyclerView recyclerView;
    private ParticipantsAdapter adapter;
    private MemberAdapter memberAdapter;
    ArrayList<String> arrayListTeam;
    LinearLayoutManager layoutManagerTeam;
    TeamAdapter teamAdapter;
    private Api api;
    String username, password,tokenLine,tokenHost;
    Button startBtn,teamBtn,allBtn,recentBtn;
    FunctionAll functionAll = new FunctionAll();
    private ZoomSDK mZoomSDK;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);

        initUI();
        getUser();


    }


    private void initUI() {
        recyclerView = findViewById(R.id.participantsRecyclerView);
        startBtn = findViewById(R.id.startInstantBtn);
        teamBtn = findViewById(R.id.teamBtn);
        allBtn = findViewById(R.id.allBtn);
        recentBtn =findViewById(R.id.recentBtn);
        username = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        tokenHost = getIntent().getStringExtra("tokenHost");

        arrayListTeam = new ArrayList<>();
        for (int i=1; i<=10;i++){
            arrayListTeam.add("Team"+i);
        }
        teamAdapter = new TeamAdapter(ParticipantsActivity.this,arrayListTeam);
        layoutManagerTeam = new LinearLayoutManager(ParticipantsActivity.this);


        teamBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                recyclerView.setAdapter(teamAdapter);
                teamBtn.setBackgroundResource(R.drawable.rec_btn);
                teamBtn.setTextColor(getApplication().getResources().getColor(R.color.white));
                allBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                allBtn.setBackgroundResource(R.drawable.rec_blue_btn);
                recentBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                recentBtn.setBackgroundResource(R.drawable.rec_blue_btn);
            }
        });
        allBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {
                allBtn.setBackgroundResource(R.drawable.rec_btn);
                allBtn.setTextColor(getApplication().getResources().getColor(R.color.white));
                teamBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                teamBtn.setBackgroundResource(R.drawable.rec_blue_btn);
                recentBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                recentBtn.setBackgroundResource(R.drawable.rec_blue_btn);
                createList();
            }
        });
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adapter.getSelectItem() != null) {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < adapter.getSelectItem().size(); i++) {
                        username = adapter.getSelectItem().get(i).getName();
                        password = adapter.getSelectItem().get(i).getPassword();
                        tokenLine = adapter.getSelectItem().get(i).getToken_line();
                        uidLine.add(tokenLine);
//                        System.out.println(username+ " " + password+" "+tokenLine);
//                        System.out.println(uidLine.size());
                    }

                    for (String s : uidLine){
                        stringBuilder.append(s);
                        stringBuilder.append(",");
                    }
                    stringBuilder.append(tokenHost);
                    SharedPreferences sharedPreferences = getSharedPreferences("LINE",0);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("line",stringBuilder.toString());
                    editor.commit();
                    if (teamAdapter.getSelectItem() != null) {
                        for (int i = 0; i < teamAdapter.getSelectItem().size(); i++) {
                            System.out.println( "chosen :"+teamAdapter.getSelectItem().get(i));
                        }
                    }
                    else {
                        System.out.println("null team adapter");
                    }
//                    functionAll.showToast(ParticipantsActivity.this,""+userName+password);
//                    LoginUserStartMeetingHelper loginUserStartMeetingHelper = new LoginUserStartMeetingHelper();
//                    loginUserStartMeetingHelper.startInstanceMeeting(ParticipantsActivity.this);

                    Intent intent = new Intent(ParticipantsActivity.this,LoginUserStartJoinMeetingActivity.class);
                    startActivity(intent);
                }

                else {
                    functionAll.showToast(ParticipantsActivity.this, "No selection");
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        ZoomSDK zoomSDK = ZoomSDK.getInstance();
        if (!zoomSDK.logoutZoom()) {
            Toast.makeText(this, "ZoomSDK has not been initialized successfully", Toast.LENGTH_LONG).show();
        }
        LoginUserStartJoinMeetingActivity loginUserStartJoinMeetingActivity = new LoginUserStartJoinMeetingActivity();
        loginUserStartJoinMeetingActivity.onClickBtnLogout();
    }


    public void onZoomSDKLogoutResult(long result) {
        if (result == ZoomAuthenticationError.ZOOM_AUTH_ERROR_SUCCESS) {
            Toast.makeText(this, "Logout successfully", Toast.LENGTH_SHORT).show();
            System.out.println("logout");
        } else {
            Toast.makeText(this, "Logout failed result code = " + result, Toast.LENGTH_SHORT).show();
        }
    }
    private void createList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ParticipantsAdapter(this, users);
        recyclerView.setAdapter(adapter);

        for (int i = 0; i < 20; i++) {
//			User user = new User();
//			user.setName("User:"+(i+1));
//			userArrayList.add();
        }
    }

    private void getUser() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://zoom.ksta.co/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        try {
            Call<List<User>> call = api.getUser();
            call.enqueue(new Callback<List<User>>() {
                @Override
                public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                    if (!response.isSuccessful()) {
                        System.out.println("response not success");
                        return;
                    }
                    users = response.body();

                    for (int i = 0; i < users.size(); i++) {
                        if (username.equals(users.get(i).getEmail())) {
                            users.remove(i);
                        }
                    }
                    createList();
//                    Toast.makeText(ParticipantsActivity.this, "ok", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(Call<List<User>> call, Throwable t) {
                    System.out.println("response failed" + t);
                }
            });
        } catch (Exception e) {
            System.out.println("response Catch" + e);
        }
    }
}