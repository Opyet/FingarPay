package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class HomeScreenActivity extends AppCompatActivity {
    private LinearLayout btnReceivePay;
    private LinearLayout btnCashTransfer;
    private Button btnReportHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        btnReceivePay = (LinearLayout)findViewById(R.id.btn_ReceivePay);
        btnCashTransfer = (LinearLayout)findViewById(R.id.btn_CashTransfer);
        btnReportHistory = (Button)findViewById(R.id.btn_viewReports);

        btnReceivePay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),ReceivePaymentActivity.class);
                startActivity(myIntent);
            }
        });

        btnCashTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),TransferActivity.class);
                startActivity(myIntent);
            }
        });

        btnReportHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),HistoryTransactionActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
