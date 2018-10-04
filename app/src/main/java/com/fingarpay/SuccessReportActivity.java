package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SuccessReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_report);
    }

    public void backHome(View view) {
        Intent myIntent = new Intent();
        myIntent.setClass(getApplicationContext(),HomeScreenActivity.class);

        startActivity(myIntent);
    }
}
