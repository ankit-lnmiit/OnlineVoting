package com.dollar.ankit.onlinevoting;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        SharedPreferences sharedpreferences = getSharedPreferences(login.MyPREFERENCES, Context.MODE_PRIVATE);
        String name = sharedpreferences.getString("nameKey","");
        Intent intent = getIntent();
        TextView testDisplay = findViewById(R.id.adminName);
        testDisplay.setText("Welcome " + name);
    }
}
