package com.xeecos.motionmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Button settingBt = (Button)findViewById(R.id.buttonSetting);
        settingBt.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(PreviewActivity.this, SettingActivity.class);   //Intent intent=new Intent(MainActivity.this,JumpToActivity.class);
                 startActivity(intent);
             }
        });
        Button previewBt = (Button)findViewById(R.id.buttonPreview);
        previewBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
                String ip = sp.getString("ipaddress", "192.168.43.204");
                EzImageView imageView = (EzImageView) findViewById(R.id.imageView);
                imageView.setImageDrawable(null);
                imageView.setImageURL("http://"+ip+"/preview/latest.jpg");
            }
        });
        Button timelapseBt = (Button)findViewById(R.id.buttonTimelapse);
        timelapseBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PreviewActivity.this, TimelapseActivity.class);   //Intent intent=new Intent(MainActivity.this,JumpToActivity.class);
                startActivity(intent);
            }
        });
    }
}