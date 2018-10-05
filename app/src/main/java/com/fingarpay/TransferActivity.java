package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TransferActivity extends AppCompatActivity {
    private Button btnTransfer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);

        btnTransfer = (Button)findViewById(R.id.btn_transfer);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),SetUpFingerScan.class);

                startActivity(myIntent);
            }
        });
    }
}
