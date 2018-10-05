package com.fingarpay;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class PinActivity extends AppCompatActivity{
    private EditText merchantPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        merchantPin = (EditText)findViewById(R.id.edt_merchantPin);
        merchantPin.requestFocus();
    }
}
