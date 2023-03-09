package com.xeecos.motionmaker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Button previewBt = (Button)findViewById(R.id.buttonPreview);
        previewBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EzImageView imageView = (EzImageView) findViewById(R.id.imageView);
                imageView.setImageDrawable(null);
                imageView.setImageURL("http://192.168.43.204/preview/latest.jpg");
            }
        });
    }
}