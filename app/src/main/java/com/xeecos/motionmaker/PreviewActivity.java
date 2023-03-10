package com.xeecos.motionmaker;

import static java.lang.Float.parseFloat;
import static java.lang.Math.floor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

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
//                imageView.setImageURL("http://snibgo.com/imforums/parrots_mos.jpg");
                imageView.setImageURL("http://"+ip+"/preview/latest.jpg");
            }
        });
        Button shotBt = (Button)findViewById(R.id.buttonShot);
        shotBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
                String ip = sp.getString("ipaddress", "192.168.43.204");
                request("http://"+ip+"/capture/shot");
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

        EditText txt_gainr = (EditText)findViewById(R.id.editTextGainR);
        EditText txt_gaing = (EditText)findViewById(R.id.editTextGainG);
        EditText txt_gainb = (EditText)findViewById(R.id.editTextGainB);
        EditText txt_exposure = (EditText)findViewById(R.id.editTextExp);

        txt_gainr.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refreshResult();
                return false;
            }
        });
        txt_gaing.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refreshResult();
                return false;
            }
        });
        txt_gainb.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refreshResult();
                return false;
            }
        });
        txt_exposure.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refreshResult();
                return false;
            }
        });
    }

    private void refreshResult()
    {
        SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        EditText txt_gainr = (EditText)findViewById(R.id.editTextGainR);
        EditText txt_gaing = (EditText)findViewById(R.id.editTextGainG);
        EditText txt_gainb = (EditText)findViewById(R.id.editTextGainB);
        EditText txt_exposure = (EditText)findViewById(R.id.editTextExp);


        float gainr = parseFloat(txt_gainr.getText().toString());
        float gaing = parseFloat(txt_gaing.getText().toString());
        float gainb = parseFloat(txt_gainb.getText().toString());
        float exposure = parseFloat(txt_exposure.getText().toString());

        editor.putString("gainr", ""+gainr);
        editor.putString("gaing", ""+gaing);
        editor.putString("gainb", ""+gainb);
        editor.putString("exposure", ""+exposure);
        editor.apply();

        String ip = sp.getString("ipaddress", "192.168.43.204");
        request("http://"+ip+"/storage/set?mode=camera");
        request("http://"+ip+"/capture/set?coarse="+(exposure*1000)+"&fine=1&r_gain="+gainr+"&gr_gain="+gaing+"&gb_gain="+gaing+"&b_gain="+gainb);

    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
        float gainr = parseFloat(sp.getString("gainr", "4"));
        float gaing = parseFloat(sp.getString("gaing", "4"));
        float gainb = parseFloat(sp.getString("gainb", "4"));
        float exposure = parseFloat(sp.getString("exposure", "1"));

        EditText txt_exposure = (EditText) findViewById(R.id.editTextExp);
        EditText txt_gainr = (EditText) findViewById(R.id.editTextGainR);
        EditText txt_gaing = (EditText) findViewById(R.id.editTextGainG);
        EditText txt_gainb = (EditText) findViewById(R.id.editTextGainB);

        txt_exposure.setText(""+exposure);
        txt_gainr.setText(""+gainr);
        txt_gaing.setText(""+gaing);
        txt_gainb.setText(""+gainb);

        String ip = sp.getString("ipaddress", "192.168.43.204");
        request("http://"+ip+"/storage/set?mode=camera");

    }
    private void request(String url)
    {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Toast.makeText(PreviewActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PreviewActivity.this,"error", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}