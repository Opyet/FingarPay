package com.fingarpay;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
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
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fingarpay.Data.DataBaseCreateHelper;
import com.fingarpay.helper.AlertMessageBox;
import com.fingarpay.helper.FingerPrintCommandManager;
import com.fingarpay.helper.SaveAsBMP;
import com.fingarpay.helper.StaffDetailsInfo;
import com.fingarpay.helper.StaffFingerInfo;
import com.fingarpay.helper.UtilityHelper;

import java.util.List;
import java.util.Locale;

import SecuGen.FDxSDKPro.JSGFPLib;
import SecuGen.FDxSDKPro.SGAutoOnEventNotifier;
import SecuGen.FDxSDKPro.SGFDxDeviceName;
import SecuGen.FDxSDKPro.SGFDxErrorCode;
import SecuGen.FDxSDKPro.SGFDxSecurityLevel;
import SecuGen.FDxSDKPro.SGFDxTemplateFormat;
import SecuGen.FDxSDKPro.SGFingerInfo;
import SecuGen.FDxSDKPro.SGFingerPresentEvent;

public class MainActivity extends AppCompatActivity implements SGFingerPresentEvent,java.lang.Runnable {
    private Button registerBtn;
    private Button pinBtn;
    private  String tag=getClass().getName();
    public static String deviceId="fingerDevice";

    private FingerPrintCommandManager fp ;
    private DataBaseCreateHelper dbServer;

    private PendingIntent mPermissionIntent;
    private byte[] mRegisterImage;
    private byte[] mVerifyImage;
    private byte[] mRegisterTemplate;
    //private byte[] mVerifyTemplate;
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
    List<StaffFingerInfo> listFinger ;

    public static  String invigilatorNames;
    private static int staffId;
    public  static  boolean isAdmin;
    int trialCnt=0;
    TextToSpeech tts;

    private static JSGFPLib sgfplib;

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

        try {
            // tts = new TextToSpeech(this, this);
            tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        int result=  tts.setLanguage(Locale.UK);
                        if (result == TextToSpeech.LANG_MISSING_DATA
                                || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                            Log.e("TTS", "This Language is not supported");
                        } else {
                            // btnSpeak.setEnabled(true);
                            // readText("Is Good to be Good!");
                        }
                    }
                }
            });


        } catch (Exception e) {

        }

        dbServer = new DataBaseCreateHelper(getApplicationContext());
        deviceId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        loadFingerDLL();
        try {
            listFinger = dbServer.FingerStaffList();

        }
        catch (Exception e) {
            return;

        }
    }


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

    //RILEY
    //This message handler is used to access local resources not
    //accessible by SGFingerPresentCallback() because it is called by
    //a separate thread.
    public Handler fingerDetectedHandler = new Handler(){
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
    private void readText(String msg) {

        tts.speak(msg, TextToSpeech.QUEUE_FLUSH, null);

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
        sgfplib = new JSGFPLib((UsbManager)getSystemService(Context.USB_SERVICE));
        //this.mToggleButtonSmartCapture.toggle();


        debugMessage("jnisgfplib version: " + sgfplib.Version() + "\n");
        mLed = false;
        //mAutoOnEnabled = false;
        autoOn = new SGAutoOnEventNotifier (sgfplib, this);
        nCaptureModeN = 0;
    }

    private  void CaptureFingerPrint(){
        long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
        //this.mCheckBoxMatched.setChecked(false);
        byte[] buffer = new byte[mImageWidth*mImageHeight];
        dwTimeStart = System.currentTimeMillis();
        long result = sgfplib.GetImageEx(buffer, 10000,50);
        String NFIQString="";
//	    if (this.mToggleButtonNFIQ.isChecked()) {
//	    	long nfiq = sgfplib.ComputeNFIQ(buffer, mImageWidth, mImageHeight);
//	    	//long nfiq = sgfplib.ComputeNFIQEx(buffer, mImageWidth, mImageHeight,500);
//	    	NFIQString =  new String("NFIQ="+ nfiq);
//	    }
//	    else
//	    	NFIQString = "";
        //DumpFile("capture.raw", buffer);
        dwTimeEnd = System.currentTimeMillis();
        dwTimeElapsed = dwTimeEnd-dwTimeStart;
        debugMessage("getImageEx(10000,50) ret:" + result + " [" + dwTimeElapsed + "ms]" + NFIQString +"\n");
        //mTextViewResult.setText("getImageEx(10000,50) ret: " + result + " [" + dwTimeElapsed + "ms] " + NFIQString +"\n");
        mRegisterImage=buffer;
        buffer = null;
        verifyFingerPrint( mRegisterImage);


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
            mVerifyImage = null;
            mRegisterTemplate = null;
            //mVerifyTemplate = null;
//            mImageViewFingerprint.setImageBitmap(grayBitmap);
//            mImageViewRegister.setImageBitmap(grayBitmap);
//            mImageViewVerify.setImageBitmap(grayBitmap);
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
        long error =-1;
       try {
            error = sgfplib.Init( SGFDxDeviceName.SG_DEV_AUTO);
       }catch(Exception ex){
           Log.e(tag,ex.getMessage() + ex.getStackTrace());
           return;
       }
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
            dlgAlert.setCancelable(true);
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
                debugMessage("TEMPLATE_FORMAT_SG400 SIZE: " + mMaxTemplateSize[0] + "\n");
                mRegisterTemplate = new byte[mMaxTemplateSize[0]];
                //mVerifyTemplate = new byte[mMaxTemplateSize[0]];
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

    @Override
    public void onDestroy() {
        Log.d(tag, "onDestroy()");
        sgfplib.CloseDevice();
        mRegisterImage = null;
        mVerifyImage = null;
        mRegisterTemplate = null;
        //mVerifyTemplate = null;
        sgfplib.Close();
        unregisterReceiver(mUsbReceiver);
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void verifyFingerPrint(byte[] mRegisterImage){
        try {
            long dwTimeStart = 0, dwTimeEnd = 0, dwTimeElapsed = 0;
            long result = -1;
            DumpFile("register.raw", mRegisterImage);
            result = sgfplib.SetTemplateFormat(SecuGen.FDxSDKPro.SGFDxTemplateFormat.TEMPLATE_FORMAT_ISO19794);
            SGFingerInfo fpInfo = new SGFingerInfo();
            for (int i=0; i< mRegisterTemplate.length; ++i)
                mRegisterTemplate[i] = 0;
                result = sgfplib.CreateTemplate(fpInfo, mRegisterImage, mRegisterTemplate);


            //Toast.makeText(getApplicationContext(),"mRegisterTemplate Create:" + result, Toast.LENGTH_SHORT).show();
            //  DumpFile("register.min", mRegisterTemplate);
            dwTimeEnd = System.currentTimeMillis();
            dwTimeElapsed = dwTimeEnd-dwTimeStart;
            debugMessage("CreateTemplate() ret:" + result + " [" + dwTimeElapsed + "ms]\n");
            // mImageViewRegister.setImageBitmap(this.toGrayscale(mRegisterImage));
            //mImageViewRegister.setImageBitmap(bitmap);

            //SaveImage(bitmap);
            //SaveImage(this.toGrayscale(mRegisterImage));
           // final byte[] fingerbyte= new SaveAsBMP().saveAndGetBMP(new UtilityHelper().toGrayscale(mRegisterImage,mImageWidth,mImageHeight));


           // StaffFingerInfo fsInfo=new StaffFingerInfo();
            StaffFingerInfo fsInfo=compareDBdata(mRegisterTemplate);

            if (fsInfo !=null && fsInfo.getId()>0) {
                trialCnt=0;
                Intent frmIntent= new Intent();
                frmIntent.setClass(MainActivity.this , HomeScreenActivity.class );

                StaffDetailsInfo sdInfo= dbServer.StaffDetailsByIdGet(fsInfo.getStaffId());
                if (sdInfo !=null && sdInfo.getId()>0) {
                    if (sdInfo.getIsAdmin()==1) {
                        isAdmin=true;
                    }
                    staffId=sdInfo.getId();
                    invigilatorNames="(" + sdInfo.getStaffCode() + ")" + sdInfo.getSurname() + ", " + sdInfo.getOtherNames();
                }
                //Toast.makeText(getApplicationContext(), invigilatorNames, Toast.LENGTH_LONG).show();
                readText("Access Granted!");
                startActivity(frmIntent);

                finish();
            }else{
                ++trialCnt;

                if (trialCnt>2) {
                    try {

                        AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                        dlgAlert.setMessage("The Fingerprint was NOT found on device. Do you want to go online and Search?");
                        dlgAlert.setTitle("Finger Print Online Search Engine");
                        dlgAlert.setPositiveButton("YES",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int whichButton){
                                        autoOn.start();
                                        dialog.dismiss();
                                        trialCnt=0;
                                        //FingerImageVerification(fingerbyte,mRegisterTemplate,"",deviceId);
                                        return;
                                    }
                                }
                        );
                        dlgAlert.setNegativeButton("NO",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int whichButton){
                                        dialog.dismiss();
                                        trialCnt=0;
                                        autoOn.start();
                                        return;
                                    }
                                }
                        );
                        dlgAlert.setCancelable(false);
                        dlgAlert.create().show();



                    } catch (Exception e) {

                        Log.e(tag, e.getMessage() + e.getStackTrace());
                    }
                }else{
                    autoOn.start();
                    readText("Access Denied. Try Again or Use another Finger.");
//						 AlertMessageBox.Show(LoginActivity.this,
//			                		"Finger MisMatch", "Please Try Again. Trial Count : " + trialCnt,
//			                		AlertMessageBox.AlertMessageBoxIcon.Error);
                }

            }
            mRegisterImage = null;
            fpInfo = null;


        } catch (Exception e) {
            AlertMessageBox.Show(MainActivity.this,"Finger Verification Failed",e.getMessage(),
                    AlertMessageBox.AlertMessageBoxIcon.Error);
        }

    }


    private StaffFingerInfo compareDBdata(byte[] mRegisterTemplate){
        try {
            if(listFinger == null || listFinger.isEmpty() || listFinger.size() == 0){

                return null;
            }
            boolean[] matched1 = new boolean[1];
            if(mRegisterTemplate !=null){
                for(int i = 0; i < listFinger.size(); i++){

                    for (int j = 0; j < 4; j++) {
                        byte[]	mVerifyTemplate=null;
                        switch (j) {
                            case 0:
                                mVerifyTemplate=listFinger.get(i).getRFThumb();
                                break;
                            case 1:
                                mVerifyTemplate=listFinger.get(i).getRFIndex();
                                break;
                            case 2:
                                mVerifyTemplate=listFinger.get(i).getLFThumb();
                                break;
                            case 3:
                                mVerifyTemplate=listFinger.get(i).getLFIndex();
                                break;
                            default:
                                break;
                        }

                        if ( mVerifyTemplate !=null) {
                            sgfplib.MatchTemplate(mVerifyTemplate, mRegisterTemplate, SGFDxSecurityLevel.SL_NORMAL, matched1);//(mRegisterTemplate, mVerifyTemplate1, SGFDxSecurityLevel.SL_NORMAL, matched1);

                            if (matched1[0] ) {
                                //mTextViewResult.setText("MATCHED!!\n");
                                //this.mCheckBoxMatched.setChecked(true);
                                debugMessage("MATCHED!!\n");
                                //mVerifyTemplate1=null;
                                //mVerifyTemplate2=null;
                                mVerifyTemplate=null;
                                return listFinger.get(i);

                            }
                        }


                    }

                }
                if (!matched1[0] ) {

                    debugMessage("MATCH NOT Found!!\n");
                }
            }
        } catch (Exception e) {
            Log.e(tag, e.getMessage() + e.getStackTrace());
        }
        return null;
    }


    @Override
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
