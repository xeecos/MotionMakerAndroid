package com.xeecos.motionmaker;

import static android.widget.Toast.*;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        EditText txt_ip1 = (EditText)findViewById(R.id.editTextIP1);
        EditText txt_ip2 = (EditText)findViewById(R.id.editTextIP2);
        EditText txt_ip3 = (EditText)findViewById(R.id.editTextIP3);
        EditText txt_ip4 = (EditText)findViewById(R.id.editTextIP4);
        txt_ip1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String ipstr = txt_ip1.getText()+"."+txt_ip2.getText()+"."+txt_ip3.getText()+"."+txt_ip4.getText();
                editor.putString("ipaddress", ipstr);
                editor.apply();
                return false;
            }
        });
        txt_ip2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String ipstr = txt_ip1.getText()+"."+txt_ip2.getText()+"."+txt_ip3.getText()+"."+txt_ip4.getText();
                editor.putString("ipaddress", ipstr);
                editor.apply();
                return false;
            }
        });
        txt_ip3.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String ipstr = txt_ip1.getText()+"."+txt_ip2.getText()+"."+txt_ip3.getText()+"."+txt_ip4.getText();
                editor.putString("ipaddress", ipstr);
                editor.apply();
                return false;
            }
        });
        txt_ip4.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                String ipstr = txt_ip1.getText()+"."+txt_ip2.getText()+"."+txt_ip3.getText()+"."+txt_ip4.getText();
                editor.putString("ipaddress", ipstr);
                editor.apply();
                return false;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sp = getSharedPreferences("ezsetting", Context.MODE_PRIVATE);
        String ip = sp.getString("ipaddress", "192.168.43.204");

        EditText txt_ip1 = (EditText)findViewById(R.id.editTextIP1);
        EditText txt_ip2 = (EditText)findViewById(R.id.editTextIP2);
        EditText txt_ip3 = (EditText)findViewById(R.id.editTextIP3);
        EditText txt_ip4 = (EditText)findViewById(R.id.editTextIP4);
        String[] separated = ip.split("\\.");
        txt_ip1.setText(separated[0]);
        txt_ip2.setText(separated[1]);
        txt_ip3.setText(separated[2]);
        txt_ip4.setText(separated[3]);
//        Toast.makeText(this,separated[0].toLowerCase(), Toast.LENGTH_SHORT).show();
    }
}