package com.xeecos.motionmaker;

import static java.lang.Float.parseFloat;
import static java.lang.Math.floor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

public class TimelapseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timelapse);


        EditText txt_fps = (EditText)findViewById(R.id.editTextFPS);
        EditText txt_during = (EditText)findViewById(R.id.editTextDuring);
        EditText txt_videotime = (EditText)findViewById(R.id.editTextVideoTime);
        EditText txt_exposure = (EditText)findViewById(R.id.editTextExposure);

        EditText txt_totaltime = (EditText)findViewById(R.id.editTextTotalTime);
        EditText txt_frames = (EditText)findViewById(R.id.editTextFrames);

        txt_fps.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refreshResult();
                return false;
            }
        });
        txt_during.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                refreshResult();
                return false;
            }
        });
        txt_videotime.setOnEditorActionListener(new TextView.OnEditorActionListener() {
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

        Button startBt = (Button)findViewById(R.id.buttonStart);
        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
                String ip = sp.getString("ipaddress", "192.168.43.204");

                float gainr = parseFloat(sp.getString("gainr", "4"));
                float gaing = parseFloat(sp.getString("gaing", "4"));
                float gainb = parseFloat(sp.getString("gainb", "4"));
                float fps = parseFloat(txt_fps.getText().toString());
                float during = parseFloat(txt_during.getText().toString());
                float videotime = parseFloat(txt_videotime.getText().toString());
                float exposure = parseFloat(txt_exposure.getText().toString());
                long time = System.currentTimeMillis()/1000+8*3600;
                request("http://"+ip+"/time/set?time="+time);
                final Handler handler = new Handler(Looper.getMainLooper());
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        request("http://"+ip+"/task/add?delay=1&interval="+(during*1000)+"&r_gain="+gainr+"&gr_gain="+gaing+"&gb_gain="+gaing+"&b_gain="+gainb+"&frames="+(int)(videotime*fps)+"&resolution=0&mode=3&fine=1&&coarse="+(int)(exposure*1000));
                    }
                }, 500);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        request("http://"+ip+"/task/start");
                    }
                }, 1000);
            }
        });

    }
    private void refreshResult()
    {
        SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        EditText txt_fps = (EditText)findViewById(R.id.editTextFPS);
        EditText txt_during = (EditText)findViewById(R.id.editTextDuring);
        EditText txt_videotime = (EditText)findViewById(R.id.editTextVideoTime);
        EditText txt_exposure = (EditText)findViewById(R.id.editTextExposure);

        EditText txt_totaltime = (EditText)findViewById(R.id.editTextTotalTime);
        EditText txt_frames = (EditText)findViewById(R.id.editTextFrames);

        float fps = parseFloat(txt_fps.getText().toString());
        float during = parseFloat(txt_during.getText().toString());
        float videotime = parseFloat(txt_videotime.getText().toString());
        float exposure = parseFloat(txt_exposure.getText().toString());
        txt_frames.setText(""+(int)(videotime*fps));
        float totaltime = ((during+exposure+2)*videotime*fps);
        int hours = (int)(floor(totaltime/3600.0));
        int minutes = (int)(totaltime/60-hours*60);
        txt_totaltime.setText(""+(hours)+"小时"+(minutes)+"分");
        editor.putString("fps", ""+fps);
        editor.putString("during", ""+during);
        editor.putString("videotime", ""+videotime);
        editor.putString("exposure", ""+exposure);
        editor.apply();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
        float fps = parseFloat(sp.getString("fps", "25"));
        float during = parseFloat(sp.getString("during", "1"));
        float videotime = parseFloat(sp.getString("videotime", "30"));
        float exposure = parseFloat(sp.getString("exposure", "1"));

        EditText txt_fps = (EditText)findViewById(R.id.editTextFPS);
        EditText txt_during = (EditText)findViewById(R.id.editTextDuring);
        EditText txt_videotime = (EditText)findViewById(R.id.editTextVideoTime);
        EditText txt_exposure = (EditText)findViewById(R.id.editTextExposure);

        EditText txt_totaltime = (EditText)findViewById(R.id.editTextTotalTime);
        EditText txt_frames = (EditText)findViewById(R.id.editTextFrames);

        txt_fps.setText(""+fps);
        txt_during.setText(""+during);
        txt_videotime.setText(""+videotime);
        txt_exposure.setText(""+exposure);
        txt_frames.setText(""+(int)(videotime*fps));
        float totaltime = ((during+exposure+2)*videotime*fps);
        int hours = (int)(floor(totaltime/3600.0));
        int minutes = (int)(totaltime/60-hours*60);
        txt_totaltime.setText(""+(hours)+"小时"+(minutes)+"分");
        String ip = sp.getString("ipaddress", "192.168.43.204");
        request("http://"+ip+"/storage/set?mode=camera");
//        Toast.makeText(this,separated[0].toLowerCase(), Toast.LENGTH_SHORT).show();
//        request("http://www.baidu.com");
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
                        Toast.makeText(TimelapseActivity.this, response, Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TimelapseActivity.this,"error", Toast.LENGTH_SHORT).show();
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}