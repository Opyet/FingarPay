package com.fingarpay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class PinActivity extends AppCompatActivity{
     EditText merchantPin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin);

        merchantPin = findViewById(R.id.edt_merchantPin);
        //merchantPin.requestFocus();


        merchantPin.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String bvncode=merchantPin.getText().toString();
                    if (bvncode.length()==4) {
                        Intent myIntent = new Intent();
                        myIntent.setClass(getApplicationContext(),HomeScreenActivity.class);
                        startActivity(myIntent);
                        finish();
                    }else{
                        //txtHideCode.setText("##101#");

                    }
                } catch (Exception e) {

                    Log.e("Error:", e.getMessage());
                }
            }
        });

    }
}
