package us.zoom.sdksample.otherfeatures;

import Adapter.FunctionAll;
import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.zoom.androidlib.utils.ZmMimeTypeUtils;
import us.zoom.sdksample.Model.UrlZoom;
import us.zoom.sdksample.Model.User;
import us.zoom.sdksample.Model.zoom_list;
import us.zoom.sdksample.R;
import us.zoom.sdksample.service.Api;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.C;
import com.google.zxing.WriterException;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyInviteActivity extends Activity {
    private Api api;
    private ImageView qrcodeImage;
    private EditText urlEdiText;
    private Button copyImageBtn, copyTextBtn ,doneBtn;
    Bitmap bitmap;
    private List<User> users;
    QRGEncoder qrgEncoder;
    private Uri uri;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://zoom.ksta.co/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.invite_activity);
        initUI();

        TextView txtUrl = (TextView) findViewById(R.id.txtUrl);
        TextView txtSubject = (TextView) findViewById(R.id.txtSubject);
        TextView txtMeetingId = (TextView) findViewById(R.id.txtMeetingId);
        TextView txtPassword = (TextView) findViewById(R.id.txtPassword);
        TextView txtRawPassword = (TextView) findViewById(R.id.txtRawPassword);
        EditText edtText = (EditText) findViewById(R.id.edtText);
        txtUrl.setVisibility(TextView.GONE);
        txtSubject.setVisibility(TextView.GONE);
        txtMeetingId.setVisibility(TextView.GONE);
        txtPassword.setVisibility(TextView.GONE);
        txtRawPassword.setVisibility(TextView.GONE);
        edtText.setVisibility(EditText.GONE);

        Intent intent = getIntent();
        Uri uri = intent.getData();

        if (uri != null)
            txtUrl.setText("URL:" + uri.toString());

        String subject = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_SUBJECT);
        if (subject != null)
            txtSubject.setText("Subject: " + subject);

        long meetingId = intent.getLongExtra(ZmMimeTypeUtils.EXTRA_MEETING_ID, 0);
        if (meetingId > 0)
            txtMeetingId.setText("Meeting ID: " + meetingId);

        String meetingPassword = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_MEETING_PSW);
        if (meetingPassword != null)
            txtPassword.setText("Password: " + meetingPassword);

        String meetingRawPassword = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_MEETING_RAW_PSW);
        if (meetingRawPassword != null)
            txtRawPassword.setText("Raw Password: " + meetingRawPassword);

        String text = intent.getStringExtra(ZmMimeTypeUtils.EXTRA_TEXT);
        if (text != null)
            edtText.setText(text);
        System.out.println(uri.toString());
        if (uri.toString() != null) {
                generateQR(uri.toString());
                urlEdiText.setText(uri.toString());
                loadSharedPreference(uri.toString());
                postMeeting(subject, uri.toString(), String.valueOf(meetingId), meetingRawPassword);

        }

    }

    private void initUI() {
        qrcodeImage = findViewById(R.id.qrcodeImageview);
        urlEdiText = findViewById(R.id.urlEditText);
        copyImageBtn = findViewById(R.id.copyImage);
        copyTextBtn = findViewById(R.id.copyText);
        doneBtn = findViewById(R.id.doneBtn);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        copyTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copy(MyInviteActivity.this, urlEdiText.getText().toString());
            }
        });
        copyImageBtn.setVisibility(View.GONE);
        copyImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyImage(MyInviteActivity.this, uri);
            }
        });

    }

    public void loadSharedPreference(String url) {
        SharedPreferences sharedPreferences = getSharedPreferences("LINE", 0);
        String line = sharedPreferences.getString("line", "");
        String[] lineToken = line.split(",");
        List<String> item = new ArrayList<String>();
        for (int i = 0; i < lineToken.length; i++) {
            item.add(lineToken[i]);
        }
        for (int i = 0; i < item.size(); i++) {
//			Log.i("invite", String.valueOf(users.get(Integer.parseInt(item.get(i)))));
            sendUrl(item.get(i), url);
            System.out.println(item.get(i));
        }

    }

    public String loadSharedPreferenceDevice() {
        SharedPreferences sharedPreferences = getSharedPreferences("DEVICE_ID", 0);
        String device_id = sharedPreferences.getString("device_id", "");
        return device_id;
    }


    public void copyImage(Context context, Uri uri) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newUri(getContentResolver(), "qrcode", uri);
        clipboardManager.setPrimaryClip(clipData);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        System.out.println("11");
        inImage.compress(Bitmap.CompressFormat.PNG, 70, bytes);
        System.out.println("22");
        System.out.println(inImage.toString());
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", "qrcode");
        System.out.println("33");
        System.out.println(path);
        return Uri.parse(path);
    }

    public void copy(Context context, String url) {
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newHtmlText("Copied ", url, url);
        clipboardManager.setPrimaryClip(clipData);
    }

    public void generateQR(String url) {
        // below line is for getting
        // the windowmanager service.
        WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);

        // initializing a variable for default display.
        Display display = manager.getDefaultDisplay();

        // creating a variable for point which
        // is to be displayed in QR Code.
        Point point = new Point();
        display.getSize(point);

        int width = point.x;
        int height = point.y;

        // generating dimension from width and height.
        int dimen = width < height ? width : height;
        dimen = dimen * 3 / 4;

        // setting this dimensions inside our qr code
        // encoder to generate our qr code.
        qrgEncoder = new QRGEncoder(url, null, QRGContents.Type.TEXT, dimen);
        try {
            // getting our qrcode in the form of bitmap.
            bitmap = qrgEncoder.encodeAsBitmap();
            // the bitmap is set inside our image
            // view using .setimagebitmap method.
            System.out.println("1");
//            uri = getImageUri(MyInviteActivity.this, bitmap);
            System.out.println("2");
            qrcodeImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            // this method is called for
            // exception handling.
            FunctionAll functionAll = new FunctionAll();
            functionAll.showToast(MyInviteActivity.this, "error: " + e);
            Log.e("Tag", e.toString());
        }
    }

    public String loadSharedPreferenceIdhost() {
        SharedPreferences sharedPreferences = getSharedPreferences("IDHOST", 0);
        String idhost = sharedPreferences.getString("idhost", "");
        return idhost;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void postMeeting(String topic, String url, String meeting_id, String password) {

        api = retrofit.create(Api.class);
        String device_id = loadSharedPreferenceDevice();
        zoom_list zoomlist = new zoom_list(device_id,Integer.parseInt(loadSharedPreferenceIdhost()), topic, url, meeting_id, password);
        Call<zoom_list> call = api.postMeeting(zoomlist);
        call.enqueue(new Callback<zoom_list>() {
            @Override
            public void onResponse(Call<zoom_list> call, Response<zoom_list> response) {
                if (response.isSuccessful()) {
                    Log.i("response", "success");       
                } else {
                    Log.i("response", " not success");
                }
            }

            @Override
            public void onFailure(Call<zoom_list> call, Throwable t) {
                Log.i("response", "failed:" +t.getMessage());
            }
        });
    }

    public void sendUrl(String uid, String url) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://bot.ksta.co/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        api = retrofit.create(Api.class);
        UrlZoom urlZoom = new UrlZoom(uid, url);
        System.out.println("uid:" + uid);
        System.out.println("url:" + url);
        System.out.println(urlZoom.getUid());
        Call<String> call = api.sendUrl(uid, url);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    System.out.println("success");
                } else {
                    System.out.println("can't send rightnow");
                    System.out.println(response.body());
                    System.out.println(response.code());
                    System.out.println(response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("send failed:" + t.getLocalizedMessage());
                System.out.println(t.getCause());
                System.out.println(t.getMessage());
                System.out.println(t.toString());
            }
        });
    }
}
