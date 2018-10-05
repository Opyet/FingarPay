package com.fingarpay.helper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.fingarpay.R;


/**
 * Created with IntelliJ IDEA.
 * User: ServusKevin
 * Date: 4/13/13
 * Time: 9:47 AM
 * To change this template use File | Settings | File Templates.
 */
public class AlertMessageBox {
    public enum AlertMessageBoxIcon
    {
        Error,
        Info,


    }
    public static void Show(Context context,String title,String message,AlertMessageBoxIcon alertMessageBoxIcon)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        switch (alertMessageBoxIcon)
        {
            case Error:
                alertDialog.setIcon(R.drawable.error);
                break;
            case Info:
                alertDialog.setIcon(R.drawable.info);
                break;
        }
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

             alertDialog.show();
    }
}


