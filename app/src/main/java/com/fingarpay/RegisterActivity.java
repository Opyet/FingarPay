package com.fingarpay;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fingarpay.Adapter.BanksAdapter;
import com.fingarpay.Data.DataAccessMethods;
import com.fingarpay.Data.DataBaseCreateHelper;
import com.fingarpay.helper.AlertMessageBox;
import com.fingarpay.helper.BVNSearchInfo;
import com.fingarpay.helper.BanksInfo;
import com.fingarpay.helper.UtilityHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegisterActivity extends AppCompatActivity {
    private Button btnRegisterMerchant;
    EditText edt_BVN_or_BVNPhoneNo;
    EditText edt_merchantFullname;
    EditText edt_accountNumber;
    Spinner sp_BankSelect;
    DataBaseCreateHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        btnRegisterMerchant =findViewById(R.id.btn_RegisterMerchant);
        edt_BVN_or_BVNPhoneNo=findViewById(R.id.edt_BVN_or_BVNPhoneNo);
        edt_merchantFullname=findViewById(R.id.edt_merchantFullname);
        edt_accountNumber=findViewById(R.id.edt_accountNumber);


        edt_BVN_or_BVNPhoneNo.addTextChangedListener(new TextWatcher() {

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
                    String bvncode=edt_BVN_or_BVNPhoneNo.getText().toString();
                    if (bvncode.length()==11) {
                        BVNSearch(bvncode);


                    }else{
                        //txtHideCode.setText("##101#");

                    }
                } catch (Exception e) {

                    Log.e("Error:", e.getMessage());
                }
            }
        });


        edt_accountNumber.addTextChangedListener(new TextWatcher() {

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
                    String bvncode=edt_accountNumber.getText().toString();
                    if (bvncode.length()==10) {
                        BanksInfo b = (BanksInfo) sp_BankSelect.getItemAtPosition(sp_BankSelect.getSelectedItemPosition());
                      String  bankCode=b.getBankCode();// String.valueOf(cmbBanks.getSelectedItemPosition());
                       String bankName=b.getBankName();
                        VerifyAccountNumber(bvncode,bankCode);
                    }else{
                        //txtHideCode.setText("##101#");

                    }
                } catch (Exception e) {

                    Log.e("Error:", e.getMessage());
                }
            }
        });


        sp_BankSelect=findViewById(R.id.sp_BankSelect);

        btnRegisterMerchant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent();
                myIntent.setClass(getApplicationContext(),MapsAddressActivity.class);

                startActivity(myIntent);
            }
        });
        db=new DataBaseCreateHelper(this);
        try {

            List<BanksInfo > result=db.BanksList();

            if (result !=null && result.size()>5) {
                BanksAdapter adapter = new BanksAdapter(getApplicationContext(),result);
                // apply the Adapter:
//		  adapter.("New Value",0);
//		  adapter.notifyDataSetChanged();
                sp_BankSelect.setAdapter(adapter);
            }else{

                LoadBanksOnline();
            }


        } catch (Exception e) {

        }
    }


    @SuppressLint("StaticFieldLeak")
    private  void BVNSearch(final String BVN){

        try {

            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            if(BVN ==null  || BVN.equals("")){

                builder.setTitle("BVN Locator!")
                        .setMessage("Please enter a valid BVN to search.")
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();
                return;
            }
            if (UtilityHelper.hasNetwork(this)) {

                final ProgressDialog dialog = new ProgressDialog(this);

                new BVNSearchTask() {
                    @Override
                    protected void onPreExecute() {
                        //dialog = new ProgressDialog(DataPreparationActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setMessage("Finding Captured Details BVN: " + BVN);
                        dialog.show();
                    }
                    @Override
                    protected void onPostExecute(BVNSearchInfo result) {

                        dialog.dismiss();
                        if (result !=null ) {


                            edt_merchantFullname.setText(result.getNameOnCard());


                        }else {
                            edt_merchantFullname.setText(null);
                            builder.setTitle("BVN Locator!")
                                    .setMessage("BVN was not Found. Check it and try again.")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // User cancelled the dialog
                                        }
                                    });
                            // Create the AlertDialog object and return it
                            builder.create();
                            builder.show();
                        }
                        dialog.hide();
                        dialog.dismiss();
                    }
                }.execute(BVN);
            }else{
                builder.setTitle("Mobile Locator!")
                        .setMessage("Please ensure you have a valid internet connection.")
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.create();
                builder.show();

                 /* AlertMessageBox.Show(this ,
                        "No Network Connection",
                        "Please ensure you have a valid internet connection.",
                        AlertMessageBox.AlertMessageBoxIcon.Error);
*/
            }
        } catch (Exception e) {
            Log.e("", e.getMessage() + e.getStackTrace());
        }
    }

    private static class BVNSearchTask extends AsyncTask<String, Integer, BVNSearchInfo> {
        @Override
        protected BVNSearchInfo doInBackground(String... args){

            DataAccessMethods rfidacc=new DataAccessMethods();
            return rfidacc.BVNSearchQuick(args);

        }
    }



       private void LoadBanksOnline(){

        try {
            if (UtilityHelper.hasNetwork(this)) {
                final ProgressDialog dialog = new ProgressDialog(this);
                new LoadBanksOnlineTask() {
                    @Override
                    protected void onPreExecute() {
                        //dialog = new ProgressDialog(DataPreparationActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setMessage("Getting all Registered Banks ...");
                        dialog.show();
                    }
                    @Override
                    protected void onPostExecute( ArrayList<BanksInfo> result) {

                        dialog.dismiss();
                        if (result != null) {
                            BanksAdapter adapter = new BanksAdapter(getApplicationContext(),result);
                            // apply the Adapter:

                            sp_BankSelect.setAdapter(adapter);
                            for (BanksInfo banksInfo : result) {

                                BanksInfo bOld=db.BanksByCodeGet(banksInfo.getBankCode());
                                if (bOld ==null || bOld.getId()==0) {

                                    int saved=db.BanksAdd(banksInfo);

                                }
                            }




                        }else {

                            Toast.makeText(getApplicationContext(),"Banks Not Setup", Toast.LENGTH_LONG).show();
                        }
                        dialog.hide();
                        dialog.dismiss();
                    }
                }.execute();
            }else{
                AlertMessageBox.Show(this,
                        "No Network Connection",
                        "Please ensure you have a valid internet connection.",
                        AlertMessageBox.AlertMessageBoxIcon.Error);

            }
        } catch (Exception e) {
            Log.e("", e.getMessage() + e.getStackTrace());
        }
    }

    private class LoadBanksOnlineTask extends AsyncTask<Void, Integer, ArrayList<BanksInfo>> {
        @Override
        protected ArrayList<BanksInfo> doInBackground(Void... args){

            DataAccessMethods rfidacc=new DataAccessMethods();
            return rfidacc.LoadBanks();

        }


    }


    private void VerifyAccountNumber(String acctNumber,String bankCode){

        try {
            if (UtilityHelper.hasNetwork(this) ){
                final ProgressDialog dialog = new ProgressDialog(this);
                new VerifyAccountNumberTask() {
                    @Override
                    protected void onPreExecute() {
                        //dialog = new ProgressDialog(DataPreparationActivity.this);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.setMessage("Verifying Account Please Wait ...");
                        dialog.show();
                    }
                    @Override
                    protected void onPostExecute( String result) {

                        dialog.dismiss();
                        if (result != null) {
                            AlertMessageBox.Show(RegisterActivity.this,
                                    "Name of Account",
                                    result,
                                    AlertMessageBox.AlertMessageBoxIcon.Info);
                            // txtReceiverName.setText(result);

                        }else {

                            Toast.makeText(getApplicationContext(),"Could Not Retrive Account Name. Ensure you entered a valid Account No. Also Confirm you have a valid Internet Connection.", Toast.LENGTH_LONG).show();
                        }
                        dialog.hide();
                        dialog.dismiss();
                    }
                }.execute(acctNumber,bankCode);
            }else{
                AlertMessageBox.Show(this,
                        "No Network Connection",
                        "Please ensure you have a valid internet connection.",
                        AlertMessageBox.AlertMessageBoxIcon.Error);

            }
        } catch (Exception e) {
            Log.e("", e.getMessage() + e.getStackTrace());
        }
    }

    private class VerifyAccountNumberTask extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... args){

            DataAccessMethods rfidacc=new DataAccessMethods();
            String account_number=args[0].toString();
            String bank_code=args[1].toString();
            return rfidacc.VerifyAccountNumber(account_number, bank_code);

        }


    }


}
