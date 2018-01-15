package com.fbb.jjzprocess;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.fbb.jjzprocess.util.DeviceUtil;
import com.fbb.jjzprocess.util.HttpUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android
                    .Manifest.permission.READ_PHONE_STATE}, 1);
        } else {
            MyApplication.getInstance().imei = DeviceUtil.getDeviceUUID(this);
            String imsi = DeviceUtil.getIMSI(this);
            if(imsi != null && !imsi.equalsIgnoreCase("")){
                MyApplication.getInstance().imsi = imsi;
            }
            Log.d("fbb","imei:"+ MyApplication.getInstance().imei);
            Log.d("fbb","imsi:"+MyApplication.getInstance().imsi);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,int[] grantResults) {
        Log.d("fbb","onRequestPermissionsResult:"+grantResults);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivityForResult(intent, 1000);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000 && resultCode == Activity.RESULT_OK){
//            requestCarList();
        }
    }

    String carListUrl = "http://192.168.0.113:8080/JJZAutoHandle/apply";
    public void requestCarList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String params = "userid="+MyApplication.getInstance().getUserId();
                String response = HttpUtil.sendPost(carListUrl, params);
                Log.d("fbb","carlist:"+response);
            }
        }).run();
    }
}
