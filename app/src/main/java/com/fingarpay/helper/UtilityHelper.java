package com.fingarpay.helper;
import java.nio.ByteBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.UUID;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

public class UtilityHelper {

	//Webservice 
	public static final String NAMESPACE = "http://tempuri.org/";
	//	public static final String SOAP_ACTION = NAMESPACE + "IMobileTracker/";
	public static final String SERVER_URL ="http://184.107.228.154/fingarpayservice/FingerPayService.asmx";

	public static final String URL_Host ="http://www.mnTracker.com/";
	public static final String Host_URLResponse ="http://www.eazypayplus.com/PublicFolder/PaymentResponse.aspx";
	public static final String METHOD_NAME_UsersAdd = "UsersAdd";
	public static final String METHOD_NAME_UsersLoginList = "UsersLoginList";

	public static final String METHOD_UsersLoginList = "UsersLoginList";

	public static final String METHOD_NAME_VerifyCard = "VerifyCardDetails";
	public static final String METHOD_NAME_VerifyAccountNumber = "VerifyAccountNumber";
	public static final String METHOD_NAME_GetTotalChargeable = "GetTotalChargeable";
	public static final String METHOD_NAME_BanksList = "BanksList";
	public static final String METHOD_NAME_TransferFromCardToAccountNumber = "TransferFromCardToAccountNumberNew";

	public static final String METHOD_NAME_UsersEmailRecoveryCodeSend = "UsersEmailRecoveryCodeSend";
	public static final String METHOD_NAME_UsersAutoChangePassword = "UsersAutoChangePassword";
	public static final String METHOD_NAME_TransactionList = "TransactionList";
	public static final String METHOD_NAME_TransactionByUsernameList = "TransactionByUsernameList";
	public static final String METHOD_NAME_TransactionByCodeGet = "TransactionByCodeGet";

	public static final String METHOD_NAME_BVNSearch = "BVNSearch";
	public static final String METHOD_NAME_MatchWithBVN = "MatchWithBVN";
	public static final String METHOD_NAME_MatchWithMobileNo = "MatchWithMobileNo";

	public static final String base64EncodedPublicKey ="";
	public static boolean hasNetwork(Activity act)
	{
			try {
				boolean haveConnectedWifi = false;
				boolean haveConnectedMobile = false;
				
				
				
				ConnectivityManager cm = (ConnectivityManager) act.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
				
				//ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				//NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				
				NetworkInfo[] netInfo = cm.getAllNetworkInfo();
				for (NetworkInfo ni : netInfo) {
				if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
				haveConnectedWifi = true;
				if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
				haveConnectedMobile = true;
				}
				if (haveConnectedMobile || haveConnectedWifi) {
					 String command = "ping -c 1 google.com";
					 return (Runtime.getRuntime().exec (command).waitFor() == 0);
				}else{
					return false;
				}
				//return haveConnectedWifi || haveConnectedMobile;
			} catch (Exception e) {
				Log.e("Connection", e.getMessage() + e.getStackTrace());

			}
			return false;
	}

	public static String getValidDateAsString() {
		String dateToday=null;
		try
		{
		 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.getDefault());

		  dateToday=dateFormat.format(new Date());
		 
		
		} catch (Exception e) {
			
				Log.e("DateFormat", e.getMessage() + e.getStackTrace());
		}
		return dateToday;
		    
		}
	public static String getValidDateAsString(String dateSaved) {
		 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss", Locale.getDefault());

		 String dateToday=dateFormat.format(new Date());
		 String datetoUse=dateSaved;
		 //Date convertedDate = new Date();
		try {
			
			if (dateSaved==null) {
				datetoUse=dateFormat.format(new Date());
			}else
			{
				
				 try {
					String[] parts = dateSaved.split("T")[0].split("-");
		 			 String[] parts2 = dateSaved.split("T")[1].split(":");
		 			
		 		    // Date DateCreated= new Date(Date.UTC(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),Integer.parseInt(parts2[0]), Integer.parseInt(parts2[1]), 0));
		 			 if (parts[0].length()==4) {
		 				 String dateFormed=Integer.parseInt(parts[0]) + "-" + Integer.parseInt(parts[1]) + "-" + Integer.parseInt(parts[2]) + " " + Integer.parseInt(parts2[0]) + ":" + Integer.parseInt(parts2[1]);
		 				 dateSaved=dateFormed;
		 				
		 			}
				} catch (Exception e) {
					dateSaved=datetoUse;
				}


				 DateFormat inputFormatter1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss",Locale.getDefault());
				Date date1 = inputFormatter1.parse(dateSaved);

				datetoUse = inputFormatter1.format(date1); // 
		    	return datetoUse;
			}		 


		} catch (Exception e) {
			
				Log.e("DateFormat", e.getMessage() + e.getStackTrace());
		}
		return dateToday;
		    
		}

	private static Date getValidDateAsDate(String dateSaved) {
		 	 SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm", Locale.getDefault());

		 	 String dateToday=dateFormat.format(new Date());
		 	 String datetoUse=dateSaved;
		 	 //Date convertedDate = new Date();
		 	try {
		 		
		 		if (dateSaved==null) {
		 			datetoUse=dateFormat.format(new Date());
		 		}else
		 		{
		 			
		 			 try {
		 				String[] parts = dateSaved.split("T")[0].split("-");
			 			 String[] parts2 = dateSaved.split("T")[1].split(":");
			 			
			 		    // Date DateCreated= new Date(Date.UTC(Integer.parseInt(parts[0]), Integer.parseInt(parts[1]), Integer.parseInt(parts[2]),Integer.parseInt(parts2[0]), Integer.parseInt(parts2[1]), 0));
			 			 if (parts[0].length()==4) {
			 				 String dateFormed=Integer.parseInt(parts[2]) + "-" + Integer.parseInt(parts[1]) + "-" + Integer.parseInt(parts[0]) + " " + Integer.parseInt(parts2[0]) + ":" + Integer.parseInt(parts2[1]);
			 				 dateSaved=dateFormed;
			 				
			 			}
					} catch (Exception e) {
						
						dateSaved=datetoUse;
					}

		 
		 			 DateFormat inputFormatter1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm",Locale.getDefault());
		 			Date date1 = inputFormatter1.parse(dateSaved);

		 			datetoUse = inputFormatter1.format(date1); // 
		 	    	return inputFormatter1.parse(datetoUse);
		 		}		 


		 	} catch (Exception e) {
		 		
		 			Log.e("DateFormat", e.getMessage() + e.getStackTrace());
		 	}
		 	return new Date();
		 	    
		 	}



	  public static Long getDateInMilliseconds(String datesaved){
		  Date d=getValidDateAsDate(datesaved);
		  long millisecond = d.getTime();
		return millisecond;
		  
	  }

	public static String createTransactionID(){
		try {

			return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		}catch(Exception ex){


		}
		return  null;
	}
	//Converts image to grayscale (NEW)
	    public Bitmap toGrayscale(byte[] mImageBuffer,int mImageWidth,int mImageHeight)
	    {        
	        byte[] Bits = new byte[mImageBuffer.length * 4];
	        for (int i = 0; i < mImageBuffer.length; i++) {
	                        Bits[i * 4] = Bits[i * 4 + 1] = Bits[i * 4 + 2] = mImageBuffer[i]; // Invert the source bits
	                        Bits[i * 4 + 3] = -1;// 0xff, that's the alpha.
	        }

	        Bitmap bmpGrayscale = Bitmap.createBitmap(mImageWidth, mImageHeight, Bitmap.Config.ARGB_8888);
	        //Bitmap bm contains the fingerprint img
	        bmpGrayscale.copyPixelsFromBuffer(ByteBuffer.wrap(Bits));
	        return bmpGrayscale;
	    }

	  
	    
	    //Converts image to grayscale (NEW)
	    public Bitmap toGrayscale(Bitmap bmpOriginal)
	    {        
	        int width, height;
	        height = bmpOriginal.getHeight();
	        width = bmpOriginal.getWidth();    
	        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
	        for (int y=0; y< height; ++y) {
	            for (int x=0; x< width; ++x){
	            	int color = bmpOriginal.getPixel(x, y);
	            	int r = (color >> 16) & 0xFF;
	            	int g = (color >> 8) & 0xFF;
	            	int b = color & 0xFF;           	
	            	int gray = (r+g+b)/3;
	            	color = Color.rgb(gray, gray, gray);
	            	//color = Color.rgb(r/3, g/3, b/3);
	            	bmpGrayscale.setPixel(x, y, color); 
	            }
	        }
	        return bmpGrayscale;
	    }
	 
	    //Converts image to binary (OLD)
	    public Bitmap toBinary(Bitmap bmpOriginal)
	    {        
	        int width, height;
	        height = bmpOriginal.getHeight();
	        width = bmpOriginal.getWidth();    
	        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
	        Canvas c = new Canvas(bmpGrayscale);
	        Paint paint = new Paint();
	        ColorMatrix cm = new ColorMatrix();
	        cm.setSaturation(0);
	        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
	        paint.setColorFilter(f);
	        c.drawBitmap(bmpOriginal, 0, 0, paint);
	        return bmpGrayscale;
	    }


}
