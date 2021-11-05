package us.zoom.sdksample.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import Adapter.FunctionAll;
import Adapter.ParticipantsAdapter;
import Adapter.RecentAdapter;
import Adapter.TeamAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.sdk.ZoomAuthenticationError;
import us.zoom.sdk.ZoomSDK;
import us.zoom.sdksample.Model.Team;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.Model.ZoomHost;
import us.zoom.sdksample.Model.zoom_list;
import us.zoom.sdksample.R;
import us.zoom.sdksample.service.Api;

public class ParticipantsActivity extends AppCompatActivity {

    List<User> users;
    List<Team> teams;
    List<ZoomHost> zoomHosts;
    List<String> uidLine = new ArrayList<>();
    ;
    private RecyclerView recyclerView;
    private ParticipantsAdapter adapter;
    private RecentAdapter recentAdapter;
    ArrayList<Team> arrayListTeam;
    LinearLayoutManager layoutManagerTeam;
    TeamAdapter teamAdapter;
    private Api api;
    String username, password, tokenLine, tokenHost;
    Button startBtn, teamBtn, allBtn, recentBtn;
    FunctionAll functionAll = new FunctionAll();
    private ZoomSDK mZoomSDK;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://zoom.ksta.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participants);
        getTeam();
        getRecently();
        initUI();
        progressDialog = ProgressDialog.show(ParticipantsActivity.this,"Loading","Loading...",true,false);

        getUser();


    }


    private void initUI() {
        recyclerView = findViewById(R.id.participantsRecyclerView);
        startBtn = findViewById(R.id.startInstantBtn);
        teamBtn = findViewById(R.id.teamBtn);
        allBtn = findViewById(R.id.allBtn);
        recentBtn = findViewById(R.id.recentBtn);
        username = getIntent().getStringExtra("userName");
        password = getIntent().getStringExtra("password");
        tokenHost = getIntent().getStringExtra("tokenHost");

        arrayListTeam = new ArrayList<>();
//        for (int i=1; i<=10;i++){
//            arrayListTeam.add("Team"+i);
//        }

        layoutManagerTeam = new LinearLayoutManager(ParticipantsActivity.this);

        recentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createListRecent();
                recentBtn.setBackgroundResource(R.drawable.rec_btn);
                recentBtn.setTextColor(getApplication().getResources().getColor(R.color.white));
                allBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                allBtn.setBackgroundResource(R.drawable.rec_blue_btn);
                teamBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                teamBtn.setBackgroundResource(R.drawable.rec_blue_btn);
            }
        });
        teamBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View v) {

                teamBtn.setBackgroundResource(R.drawable.rec_btn);
                teamBtn.setTextColor(getApplication().getResources().getColor(R.color.white));
                allBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                allBtn.setBackgroundResource(R.drawable.rec_blue_btn);
                recentBtn.setTextColor(getApplication().getResources().getColor(R.color.blueBtn));
                recentBtn.setBackgroundResource(R.drawable.rec_blue_btn);
                createListTeam();
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
                try {
                    if (recentAdapter.getSelectItem() != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (ZoomHost zoomHost : recentAdapter.getSelectItem()) {
//                            System.out.println(zoomHost.getUser().getName());
                            uidLine.add(zoomHost.getUser().getToken_line());
                        }
                        for (String s : uidLine) {
                            stringBuilder.append(s);
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(tokenHost);
                        SharedPreferences sharedPreferences = getSharedPreferences("LINE", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("line", stringBuilder.toString());
                        editor.commit();
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }


                try {
                    if (adapter.getSelectItem() != null) {
                        StringBuilder stringBuilder = new StringBuilder();
                        System.out.println("adapter");
                        for (int i = 0; i < adapter.getSelectItem().size(); i++) {
                            username = adapter.getSelectItem().get(i).getName();
                            password = adapter.getSelectItem().get(i).getPassword();
                            tokenLine = adapter.getSelectItem().get(i).getToken_line();
                            uidLine.add(tokenLine);
//                        System.out.println(username+ " " + password+" "+tokenLine);
//                        System.out.println(uidLine.size());
                        }

                        for (String s : uidLine) {
                            stringBuilder.append(s);
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(tokenHost);
                        SharedPreferences sharedPreferences = getSharedPreferences("LINE", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("line", stringBuilder.toString());
                        editor.commit();

//                    functionAll.showToast(ParticipantsActivity.this,""+userName+password);
//                    LoginUserStartMeetingHelper loginUserStartMeetingHelper = new LoginUserStartMeetingHelper();
//                    loginUserStartMeetingHelper.startInstanceMeeting(ParticipantsActivity.this);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
//                    functionAll.showToast(ParticipantsActivity.this, "No selection");
                }
                try {
                    if (teamAdapter.getSelectItem() != null) {
                        System.out.println("team adapter");
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Team team : teamAdapter.getSelectItem()) {
                            System.out.println(team.getName());
                            for (int i = 0; i < team.getUser().size(); i++) {
                                System.out.println(team.getUser().get(i).getName());
                                uidLine.add(team.getUser().get(i).getToken_line());
                            }
                        }
                        for (String s : uidLine) {
                            stringBuilder.append(s);
                            stringBuilder.append(",");
                        }
                        stringBuilder.append(tokenHost);
                        SharedPreferences sharedPreferences = getSharedPreferences("LINE", 0);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("line", stringBuilder.toString());
                        editor.commit();
//                    Intent intent = new Intent(ParticipantsActivity.this,LoginUserStartJoinMeetingActivity.class);
//                    startActivity(intent);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }

                Intent intent = new Intent(ParticipantsActivity.this, LoginUserStartJoinMeetingActivity.class);
                startActivity(intent);
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

    }

    private void createListTeam() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        teamAdapter = new TeamAdapter(ParticipantsActivity.this, teams);
        recyclerView.setAdapter(teamAdapter);
    }

    private void createListRecent() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recentAdapter = new RecentAdapter(ParticipantsActivity.this, zoomHosts);
        recyclerView.setAdapter(recentAdapter);
    }

    private void getTeam() {
        api = retrofit.create(Api.class);
        Call<List<Team>> call = api.getTeam();
        call.enqueue(new Callback<List<Team>>() {
            @Override
            public void onResponse(Call<List<Team>> call, Response<List<Team>> response) {
                if (response.isSuccessful()) {
//                    System.out.println(response.body());
                    teams = response.body();
                    progressDialog.dismiss();
//                    for (Team team:teams){
//                        usersTeam = team.getUser();
//                        System.out.println(team.getName());
//                        for(User user:team.getUser()){
//                            System.out.println(user.getName());
//                        }
//                    }
//                    createListTeam();
                    System.out.println("get team");
                } else {
                    System.out.println("can't get team");
                }
            }

            @Override
            public void onFailure(Call<List<Team>> call, Throwable t) {
                System.out.println("fail:" + t.getLocalizedMessage());
            }
        });
    }

    private void getRecently() {
        api = retrofit.create(Api.class);
        Call<List<ZoomHost>> call = api.getRecent();
        call.enqueue(new Callback<List<ZoomHost>>() {
            @Override
            public void onResponse(Call<List<ZoomHost>> call, Response<List<ZoomHost>> response) {
                if (response.isSuccessful()) {
                    zoomHosts = response.body();
                    System.out.println("get zoom list");
                    for (int i = 0; i < zoomHosts.size(); i++) {
                        System.out.println("Date " + i + ":" + zoomHosts.get(i).getZoom_list().getDate());
                    }
                } else {
                    System.out.println("can't response get Recently");
                }
            }

            @Override
            public void onFailure(Call<List<ZoomHost>> call, Throwable t) {

            }
        });
    }

    private void getUser() {

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
                    progressDialog.dismiss();
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