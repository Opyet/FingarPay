package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button registerBtn;
    private Button pinBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerBtn = (Button)findViewById(R.id.btn_GoToRegisterPage);
        pinBtn = (Button)findViewById(R.id.btn_GoToPinPage);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),RegisterActivity.class);
                startActivity(myIntent);
            }
        });

        pinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),PinActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
