package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ReceivePaymentActivity extends AppCompatActivity {
    private Button btnReceivePay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_payment);

        btnReceivePay = (Button)findViewById(R.id.btn_continuePayment);
        btnReceivePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getApplicationContext(),SetUpFingerScan.class);

                startActivity(intent);
            }
        });
    }
}
