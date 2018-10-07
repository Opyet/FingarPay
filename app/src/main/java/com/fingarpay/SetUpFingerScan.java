package com.fingarpay;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fingarpay.Data.DataBaseCreateHelper;
import com.fingarpay.helper.AlertMessageBox;
import com.fingarpay.helper.StaffDetailsInfo;
import com.fingarpay.helper.StaffFingerInfo;
import com.fingarpay.helper.UtilityHelper;

import java.util.List;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGFingerPresentEvent;

public class SetUpFingerScan extends AppCompatActivity implements SGFingerPresentEvent,java.lang.Runnable{

TextView lblPlaceFinger;

    private DataBaseCreateHelper db;  //Êý¾Ý¿â²Ù×÷
    //private FingerPrintCommandManager fp ;
    public static String deviceId="fingerDevice";
    List<StaffFingerInfo> listFinger ;
    private PendingIntent mPermissionIntent;
    private byte[] mRegisterImage;
    private byte[] mRegisterTemplate1;
    private byte[] mRegisterTemplate2;
    private boolean isStep1=true;
    private int[] mMaxTemplateSize;
    private static int mImageWidth;
    private static int mImageHeight;
    private int[] grayBuffer;
    private byte[] fpUsed;
    private Bitmap grayBitmap;
    private IntentFilter filter; //2014-04-11
    private SGAutoOnEventNotifier autoOn;
    private boolean mLed;
    private static boolean mAutoOnEnabled=true;
    private int nCaptureModeN;
    ImageView img_FingerPrint;

    private static String tag="StaffDetails";
    private Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            String data = msg.getData().getString("msg");
            if(data != null){
                //editInfo.append(data);
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_finger_scan);
        lblPlaceFinger=(TextView)findViewById(R.id.lblPlaceFinger);
        img_FingerPrint=findViewById(R.id.img_FingerPrint);


        lblPlaceFinger.setText("Fingerprint Enables Quick and Secured Login");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        db = new DataBaseCreateHelper(getApplicationContext());


        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        loadFingerDLL();
        loadFingers();
    }
    private void loadFingers(){

        try {
            listFinger = db.FingerStaffList();

        }
        catch (Exception e) {

        }
    }

    public void onBackPressed() {

        // Toast.makeText(getApplicationContext(),"Thanks for using application!!",Toast.LENGTH_LONG).show();
        finish();
        return;
    }


    private void SaveRecord() {
        String staffCode =UtilityHelper.createTransactionID();// txtStaffCode.getText().toString();
        while (staffCode==null){
            staffCode= UtilityHelper.createTransactionID();
        }


        String surname = "Ibiang";//txtSurname.getText().toString();
        String otherNames = "Okoi";//txtOtherNames.getText().toString();
        String mobile ="08067415830";//txtMobile.getText().toString();
        int isAdmin=0;
        // if (chkIsAdmin.isChecked()) {
        isAdmin=1;
        // }
        if(staffCode == null){
            //editName.append("ÇëÊäÈëname\n");
            //editName.append("please input name\n");
            return ;
        }
        if(mRegisterTemplate1 == null || mRegisterTemplate2 == null){
            Toast.makeText(getApplicationContext(), "Place Finger on Device and Extract", Toast.LENGTH_LONG).show();
            return ;
        }
        StaffDetailsInfo sdInfo = new StaffDetailsInfo();
        sdInfo.setStaffCode(staffCode);
        sdInfo.setSurname(surname);
        sdInfo.setOtherNames(otherNames);
        sdInfo.setMobile(mobile);
        sdInfo.setIsAdmin(isAdmin);

        int savedId = 0;
        //data.setStaffId(name);
        StaffDetailsInfo sdInfOld= db.StaffDetailsByStaffCodeGet(staffCode);
        if (sdInfOld !=null && sdInfOld.getId()>0) {
            db.StaffDetailsUpdate(sdInfo);
            savedId=sdInfOld.getId();
        }else{
            savedId = db.StaffDetailsAdd(sdInfo);
            Log.d("StaffDetailsAdd", "Saved New Finger" + savedId);
        }
        //update Photo
        try {
            StaffFingerInfo fsInfo=db.FingerStaffByStaffIdGet(savedId);
            if (fsInfo ==null || fsInfo.getId()==0) {
                fsInfo=new StaffFingerInfo();
                fsInfo.setRFIndex(mRegisterTemplate1);
                fsInfo.setRFThumb(mRegisterTemplate2);
                fsInfo.setStaffId(savedId);
                int id=db.FingerStaffAdd(fsInfo);
                Log.d("FingerAdd", "Saved New Finger" + id);
            }else{

                fsInfo.setRFIndex(mRegisterTemplate1);
                fsInfo.setRFThumb(mRegisterTemplate2);
                fsInfo.setStaffId(savedId);
                db.FingerStaffUpdate(fsInfo);
            }
        } catch (Exception e) {

            Log.e(staffCode, e.getMessage() + e.getStackTrace());
        }



        if (savedId>0)
        {
            // clear();
            //StaffPDAUsersUpload();
            //loadFingers();
            //Toast.makeText(getApplicationContext(), "Saved Successfully", Toast.LENGTH_SHORT).show();
            AlertMessageBox.Show(SetUpFingerScan.this,
                    "Success", "Saved Successfully.",
                    AlertMessageBox.AlertMessageBoxIcon.Info);

        }else{
            Toast.makeText(getApplicationContext(), "Saved Not Successful", Toast.LENGTH_SHORT).show();
        }

    }

    private void loadFingerDLL(){

        grayBuffer = new int[JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES*JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES];
        for (int i=0; i<grayBuffer.length; ++i)
            grayBuffer[i] = android.graphics.Color.GRAY;
        grayBitmap = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES, Bitmap.Config.ARGB_8888);
        grayBitmap.setPixels(grayBuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES);
        // mImageViewFingerprint.setImageBitmap(grayBitmap);

        int[] sintbuffer = new int[(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2)*(JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2)];
        for (int i=0; i<sintbuffer.length; ++i)
            sintbuffer[i] = android.graphics.Color.GRAY;
        Bitmap sb = Bitmap.createBitmap(JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2, Bitmap.Config.ARGB_8888);
        sb.setPixels(sintbuffer, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, 0, 0, JSGFPLib.MAX_IMAGE_WIDTH_ALL_DEVICES/2, JSGFPLib.MAX_IMAGE_HEIGHT_ALL_DEVICES/2);
        //mImageViewRegister.setImageBitmap(grayBitmap);
        // mImageViewVerify.setImageBitmap(grayBitmap);

        mMaxTemplateSize = new int[1];

        //USB Permissions
        mPermissionIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
        filter = new IntentFilter(ACTION_USB_PERMISSION);
        registerReceiver(mUsbReceiver, filter);
        sgfplib = new JSGFPLib((UsbManager) getSystemService(Context.USB_SERVICE));
        //this.mToggleButtonSmartCapture.toggle();


        debugMessage("jnisgfplib version: " + sgfplib.Version() + "\n");
        mLed = false;
        //mAutoOnEnabled = false;
        autoOn = new SGAutoOnEventNotifier (sgfplib, this);
        nCaptureModeN = 0;
    }

    private static JSGFPLib sgfplib;

    private static void debugMessage(String message) {
        // this.mEditLog.append(message);
        // this.mEditLog.invalidate(); //TODO trying to get Edit log to update after each line written
    }

    //RILEY
    //This broadcast receiver is necessary to get user permissions to access the attached USB device
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            //DEBUG Log.d(tag,"Enter mUsbReceiver.onReceive()");
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {
                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if(device != null){
                            //DEBUG Log.d(tag, "Vendor ID : " + device.getVendorId() + "\n");
                            //DEBUG Log.d(tag, "Product ID: " + device.getProductId() + "\n");
                            debugMessage("Vendor ID : " + device.getVendorId() + "\n");
                            debugMessage("Product ID: " + device.getProductId() + "\n");
                        }
                        else
                            Log.e(tag, "mUsbReceiver.onReceive() Device is null");
                    }
                    else
                        Log.e(tag, "mUsbReceiver.onReceive() permission denied for device " + device);
                }
            }
        }
    };

    public  Handler fingerDetectedHandler = new Handler(){
        // @Override
        public void handleMessage(Message msg) {
            //Handle the message
            CaptureFingerPrint();
            if (mAutoOnEnabled) {
                //mToggleButtonAutoOn.toggle();
                //EnableControls();
            }
        }
    };


    public static void DumpFile(String fileName, byte[] buffer)
    {
        //Uncomment section below to dump images and templates to SD card

    }

    private  void CaptureFingerPrint(){
        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
        //this.mCheckBoxMatched.setChecked(false);
        byte[] buffer = new byte[mImageWidth*mImageHeight];
        dwTimeStart = System.currentTimeMillis();
        long result = sgfplib.GetImageEx(buffer, 10000,50);
        String NFIQString="";
//		    if (this.mToggleButtonNFIQ.isChecked()) {
//		    	long nfiq = sgfplib.ComputeNFIQ(buffer, mImageWidth, mImageHeight);
//		    	//long nfiq = sgfplib.ComputeNFIQEx(buffer, mImageWidth, mImageHeight,500);
//		    	NFIQString =  new String("NFIQ="+ nfiq);
//		    }
//		    else
//		    	NFIQString = "";
        DumpFile("capture.raw", buffer);
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd-dwTimeStart;
        debugMessage("getImageEx(10000,50) ret:" + result + " [" + dwTimeElapsed + "ms]" + NFIQString +"\n");
        //mTextViewResult.setText("getImageEx(10000,50) ret: " + result + " [" + dwTimeElapsed + "ms] " + NFIQString +"\n");
        mRegisterImage=buffer;
        buffer = null;


        StaffFingerInfo fsInfo=new StaffFingerInfo();
        byte[] fingerTemplate= verifyFingerPrint();

        fsInfo=compareDBdata(fingerTemplate);

        if (fsInfo !=null && fsInfo.getId()>0) {

            lblPlaceFinger.setText("Place Finger on Device");
            mRegisterTemplate1 = new byte[mMaxTemplateSize[0]];
            mRegisterTemplate2 = new byte[mMaxTemplateSize[0]];
            isStep1=true;

            StaffDetailsInfo sdInfo=db.StaffDetailsByIdGet(fsInfo.getStaffId());
           // txtStaffCode.setText(sdInfo.getStaffCode());
           // FillStafDetails(sdInfo);
            AlertMessageBox.Show(SetUpFingerScan.this,
                    "Already Captured", "This Finger Print Already Captured.",
                    AlertMessageBox.AlertMessageBoxIcon.Info);

        }else{
            //clear();
            //bttnSaveStaffDetails.setEnabled(true);
        }



    }

    @Override
    public void onPause() {
        try {
            Log.d(tag, "onPause()");

            autoOn.stop();
            //EnableControls();
            sgfplib.CloseDevice();
            //unregisterReceiver(mUsbReceiver);
            mRegisterImage = null;
            //mVerifyImage = null;
            mRegisterTemplate1 = null;
            mRegisterTemplate2 = null;
            //mVerifyTemplate = null;
//	            mImageViewFingerprint.setImageBitmap(grayBitmap);
//	            mImageViewRegister.setImageBitmap(grayBitmap);
//	            mImageViewVerify.setImageBitmap(grayBitmap);
            super.onPause();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void onResume(){
        Log.d(tag, "onResume()");
        super.onResume();
        registerReceiver(mUsbReceiver, filter);
        long error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
        if (error != SGFDxErrorCode.SGFDX_ERROR_NONE){
            AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
            if (error == SGFDxErrorCode.SGFDX_ERROR_DEVICE_NOT_FOUND)
                dlgAlert.setMessage("Please attach the Fingerprint Device.");

            else
                dlgAlert.setMessage("Fingerprint device initialization failed!");
            dlgAlert.setTitle("SecuGen Fingerprint SDK");
            dlgAlert.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int whichButton){
                            dialog.dismiss();
                            //finish();
                            return;
                        }
                    }
            );
            dlgAlert.setCancelable(false);
            dlgAlert.create().show();
        }
        else {
            UsbDevice usbDevice = sgfplib.GetUsbDevice();
            if (usbDevice == null){
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);
                dlgAlert.setMessage("SDU04P or SDU03P fingerprint sensor not found!");
                dlgAlert.setTitle("SecuGen Fingerprint SDK");
                dlgAlert.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton){
                                dialog.dismiss();
                                //finish();
                                return;
                            }
                        }
                );
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            }
            else {
                sgfplib.GetUsbManager().requestPermission(usbDevice, mPermissionIntent);
                error = sgfplib.OpenDevice(0);
                debugMessage("OpenDevice() ret: " + error + "\n");
                SecuGen.FDxSDKPro.SGDeviceInfoParam deviceInfo = new SecuGen.FDxSDKPro.SGDeviceInfoParam();
                error = sgfplib.GetDeviceInfo(deviceInfo);
                debugMessage("GetDeviceInfo() ret: " + error + "\n");
                mImageWidth = deviceInfo.imageWidth;
                mImageHeight= deviceInfo.imageHeight;
                debugMessage("Image width: " + mImageWidth + "\n");
                debugMessage("Image height: " + mImageHeight + "\n");
                debugMessage("Serial Number: " + new String(deviceInfo.deviceSN()) + "\n");
                sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
                sgfplib.GetMaxTemplateSize(mMaxTemplateSize);
                debugMessage("TEMPLATE_FORMAT_ANSI378 SIZE: " + mMaxTemplateSize[0] + "\n");
                mRegisterTemplate1 = new byte[mMaxTemplateSize[0]];
                mRegisterTemplate2 = new byte[mMaxTemplateSize[0]];

                // mVerifyTemplate = new byte[mMaxTemplateSize[0]];
                boolean smartCaptureEnabled = true;//this.mToggleButtonSmartCapture.isChecked();
                if (smartCaptureEnabled)
                    sgfplib.WriteData((byte)5, (byte)1);
                else
                    sgfplib.WriteData((byte)5, (byte)0);
                if (mAutoOnEnabled){
                    autoOn.start();
                    //DisableControls();
                }
                //Thread thread = new Thread(this);
                //thread.start();
            }
        }
    }

    private byte[] verifyFingerPrint(){
        try {
            byte[] fingerTemplate=null;
            long dwTimeStart = 0, dwTimeEnd = 0;
            long result = -1;
            img_FingerPrint.setImageBitmap(new UtilityHelper().toGrayscale(mRegisterImage,mImageWidth,mImageHeight));
            // DumpFile("register.raw", mRegisterImage);
            result = sgfplib.SetTemplateFormat(SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
            SGFingerInfo fpInfo = new SGFingerInfo();
            if (isStep1) {
                for (int i=0; i< mRegisterTemplate1.length; ++i)
                    mRegisterTemplate1[i] = 0;
                result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate1);
                fingerTemplate=mRegisterTemplate1;
            }else{
                for (int i=0; i< mRegisterTemplate2.length; ++i)
                    mRegisterTemplate2[i] = 0;
                result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate2);
                fingerTemplate=mRegisterTemplate2;
                boolean[] matched = new boolean[1];

                sgfplib.MatchTemplate(mRegisterTemplate1, mRegisterTemplate2, SGFDxSecurityLevel.SL_NORMAL, matched);



                isStep1=true;
                autoOn.start();
                if (matched[0]) {

                    lblPlaceFinger.setText("Save the record to continue");
                    AlertMessageBox.Show(SetUpFingerScan.this,
                            "Finger Match Ok!", "Continue to Save Record",
                            AlertMessageBox.AlertMessageBoxIcon.Info);
                    SaveRecord();
                }else{
                    mRegisterTemplate1 = new byte[mMaxTemplateSize[0]];
                    mRegisterTemplate2 = new byte[mMaxTemplateSize[0]];
                    lblPlaceFinger.setText("Place Finger on device 1");
                    AlertMessageBox.Show(SetUpFingerScan.this,
                            "Finger MisMatch", "Please Enroll Finger Prints Again",
                            AlertMessageBox.AlertMessageBoxIcon.Error);
                }
                return fingerTemplate;
            }

            autoOn.start();
            lblPlaceFinger.setText("Place Finger on device Again 2");
            mRegisterImage = null;
            fpInfo = null;
            isStep1=false;
            //fingerbyte=null;
            return fingerTemplate;



        } catch (Exception e) {
            autoOn.start();
            AlertMessageBox.Show(SetUpFingerScan.this,"Finger Extraction Failed",e.getMessage(),
                    AlertMessageBox.AlertMessageBoxIcon.Error);
        }
        return null;
    }


    private StaffFingerInfo compareDBdata(byte[] mRegisterTemplate){
        try {
            if(listFinger == null || listFinger.isEmpty() || listFinger.size() == 0){

                return null;
            }

            if(mRegisterTemplate !=null){
                for(int i = 0; i < listFinger.size(); i++){

                    byte[]	mVerifyTemplate1=listFinger.get(i).getRFThumb();
                    byte[]	mVerifyTemplate2=listFinger.get(i).getRFIndex();
                    boolean[] matched1 = new boolean[1];
                    boolean[] matched2 = new boolean[1];
                    sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate1, SGFDxSecurityLevel.SL_NORMAL, matched1);
                    sgfplib.MatchTemplate(mRegisterTemplate, mVerifyTemplate2, SGFDxSecurityLevel.SL_NORMAL, matched2);
                    if (matched1[0] && matched2[0]) {
                        //mTextViewResult.setText("MATCHED!!\n");
                        //this.mCheckBoxMatched.setChecked(true);
                        debugMessage("MATCHED!!\n");
                        return listFinger.get(i);
                    }
                    else {
                        //mTextViewResult.setText("NOT MATCHED!!");
                        //this.mCheckBoxMatched.setChecked(false);
                        debugMessage("NOT MATCHED!!\n");
                    }
                }
            }
        } catch (Exception e) {
            Log.e(tag, e.getMessage() + e.getStackTrace());
        }
        return null;
    }


    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy()");

        //unregisterReceiver(screenReceiver);

        sgfplib.CloseDevice();
        mRegisterImage = null;
        //mVerifyImage = null;
        mRegisterTemplate1 = null;
        mRegisterTemplate2=null;
        //mVerifyTemplate = null;
        sgfplib.Close();
        unregisterReceiver(mUsbReceiver);
//	    	if (thread !=null) {
//				thread.interrupt();
//			}
        super.onDestroy();

    }

    public void SGFingerPresentCallback() {
        autoOn.stop();
        fingerDetectedHandler.sendMessage(new Message());

    }


    @Override
    public void run() {
        while (true) {

        }
    }


}
