package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SetUpFingerScan extends AppCompatActivity {
    private Button btnFingerprintApprove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_finger_scan);
        btnFingerprintApprove = (Button)findViewById(R.id.btn_FingerprintApprove);
        btnFingerprintApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),HomeScreenActivity.class);

                startActivity(myIntent);
            }
        });
    }


}
