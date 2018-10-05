package com.fingarpay.Data;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.fingarpay.helper.BanksInfo;
import com.fingarpay.helper.MyCardInfo;
import com.fingarpay.helper.TransferFundsInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


public class DataBaseCreateHelper extends SQLiteOpenHelper {


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    public static final int PortalId = 0;
    ProgressDialog dialog = null;
    String tag = "DatabaseCreater";
    // Database Name
    // #region private properties

    //static string ConnectionString = new classDAL().ConnectToSqlLiteDB();
    //static SQLiteConnection conn = new SQLiteConnection(ConnectionString);

     // Database Name
    // #region private properties

    //static string ConnectionString = new classDAL().ConnectToSqlLiteDB();
    //static SQLiteConnection conn = new SQLiteConnection(ConnectionString);
    private static String DATABASE_NAME = "mmPayPlusDB.db";
    private String TAG = "DatabaseHelperClass";
    // Student table TABLE_TransferFunds
    private static String TABLE_TransferFunds = "TransferFundsTbl";
    private static String TABLE_Banks = "BanksTbl";
    private static String TABLE_MyCards = "MyCardsTbl";

    //Transactions column Names
    private static String KEY_TransferFundsId = "TransferFundsId";
    private static String KEY_AccountNumber = "ReceiverAccountNo";
    private static String KEY_AccountName = "ReceiverAccountName";
    private static String KEY_DateCreated = "DateCreated";
    private static String KEY_Amount = "Amount";
    private static String KEY_TransactionCode = "TransactionCode";
    private static String KEY_ReceiverBankCode = "ReceiverBankCode";
    private static String KEY_TransactionStatus = "TransactionStatus";
    private static String KEY_IsSuccess = "IsSuccess";
    private static String KEY_TransactionDetails = "TransactionDetails";

    private static String KEY_MyCardId = "MyCardId";
    private static String KEY_MyCardNo = "MyCardNo";
    private static String KEY_MyCardName = "MyCardName";


    //Hymms column Names
    private static String KEY_BroadcastId = "Id";
    private static String KEY_BroadcastDetails = "BroadcastDetails";
    private static String KEY_SenderName = "SenderName";
    private static String KEY_SenderFullName = "SenderFullName";


    //Banks column Names
    private static String KEY_BanksId = "Id";
    private static String KEY_BanksName = "BankName";

    //#endregion

    private static Context mContext;
    public static String dbExternalPath = "/mnt/sdcard/";

    static String DB_PATH = null;
    static String dbInternalPath = null;
    private SQLiteDatabase mDataBase;

    //SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    public DataBaseCreateHelper(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);

        mContext = context;
        if (Build.VERSION.SDK_INT >= 17) {
            //DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
            DB_PATH = mContext.getFilesDir().getAbsolutePath().replace("files",
                    "databases") + File.separator;
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }
        dbInternalPath = DB_PATH + DATABASE_NAME;


    }

    public SQLiteDatabase copyDatabaseToSdCard(String folderName) {
        try {

            File f1 = new File(dbInternalPath);

            if (f1.exists()) {

                //File f2 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+ "/" + DATABASE_NAME + ".db");

                File f2 = new File(dbExternalPath + folderName);
                try {
                    if (!f2.exists()) {
                        f2.mkdirs();
                    }
                    f2 = new File(dbExternalPath + folderName + File.separator + DATABASE_NAME);
                    f2.createNewFile();
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage() + e.getStackTrace());
                }
                InputStream in = new FileInputStream(f1);
                OutputStream out = new FileOutputStream(f2);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                return mDataBase;
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        Log.e("copyDatabaseToSdCard", "Finished");
        return null;
    }

    public boolean copyDatabaseToPhoneMemory(SQLiteDatabase db) {

        try {
            //File fs=new File(dbExternalPath,);

            File f1 = new File(dbExternalPath + DATABASE_NAME);
            try {
                f1.createNewFile();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage() + e.getStackTrace());
            }
            if (f1.exists()) {

                //File f2 = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+ "/" + DATABASE_NAME + ".db");

                File f2 = new File(dbInternalPath);
                f2.createNewFile();
                InputStream in = new FileInputStream(f1);
                OutputStream out = new FileOutputStream(f2);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                f1.delete();
                in.close();
                out.close();
                return true;
            } else {
                //	AlertMessageBox.().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//				AlertMessageBox.Show(mContext,
//                		"File Missing", "The External Database was NOT Found!.Creating internal DB",
//                		AlertMessageBox.AlertMessageBoxIcon.Error);

                return true;
            }
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage() + " in the specified directory.");
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(e.getMessage() + e.getStackTrace());
        }
        //Log.e("Databasehealper", "********************************");
        return false;
    }


    @Override
    public synchronized void close() {
        if (mDataBase != null)
            mDataBase.close();
        super.close();
    }

    public boolean CheckIfdatabaseExist() {
        try {
            File dbFile = new File(dbInternalPath);
            File f1 = new File(dbExternalPath + DATABASE_NAME);
            if (dbFile.exists() && f1.exists()) {
                dbFile.delete();
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.d(TAG, "Output:" + e.getMessage() + e.getStackTrace());
        }
        return false;
    }

    public SQLiteDatabase createDataBase(SQLiteDatabase db) throws SQLException {
        try {
            boolean dbExists = CheckIfdatabaseExist();

            if (!dbExists) {

                copyDatabaseToPhoneMemory(db);
            } else {
                createInternalStorageDB(db);

                //return SQLiteDatabase.openDatabase(dbInternalPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS | SQLiteDatabase.CREATE_IF_NECESSARY);
            }
            return db;
        } catch (Exception e) {

        }

        return null;
    }


    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        mDataBase = db;
        createDataBase(db);
//    	new ReloadDatabaseTask(){
//
//    	}.execute();


    }

    private class ReloadDatabaseTask extends AsyncTask<Void, String, Boolean> {


        @Override
        protected Boolean doInBackground(Void... params) {

            try {

                createDataBase(mDataBase);
                return true;
            } catch (Exception e) {
                Log.e(tag, e.getMessage() + e.getStackTrace());
            }
            return false;
        }

        @Override
        protected void onProgressUpdate(String... holder) {
            // dialog.setIndeterminate(false);
            //dialog.setMax(iListSD.size());
            // dialog.setProgress(progress[0]);
            dialog.setMessage(holder[0]);
            //imgdialog.show();

        }

        @Override
        protected void onPreExecute() {
            //if (dialog==null)
            //{
            dialog = new ProgressDialog(mContext);
            //}

            dialog.setCanceledOnTouchOutside(false);
            dialog.setMessage("Re-Loading Database. Please wait...");
            dialog.show();

        }

        @Override
        protected void onPostExecute(Boolean sfInfo) {

            //publishProgress("Restarting the Application. Please wait..");
            dialog.dismiss();


        }


    }

    private void createInternalStorageDB(SQLiteDatabase db) {
        try {
            String CREATE_Hymms_Table = "CREATE TABLE IF NOT EXISTS " + TABLE_TransferFunds + "("
                    + KEY_TransferFundsId + " INTEGER PRIMARY KEY," + KEY_AccountNumber + " TEXT,"
                    + KEY_Amount + " INTEGER ," + KEY_TransactionCode + " TEXT," + KEY_SenderName + " TEXT,"
                    + KEY_TransactionStatus + " TEXT," + KEY_TransactionDetails + " TEXT,"
                    + KEY_ReceiverBankCode + " TEXT ," + KEY_SenderFullName + " TEXT," + KEY_IsSuccess + " TEXT,"
                    + KEY_AccountName + " TEXT," + KEY_DateCreated + " TEXT" + ")";
            db.execSQL(CREATE_Hymms_Table);
        } catch (Exception e) {
            Log.e(TABLE_TransferFunds, e.getMessage() + e.getStackTrace());
        }



        try {
            String CREATE_Audio_Table = "CREATE TABLE IF NOT EXISTS " + TABLE_Banks + "("
                    + KEY_BanksId + " INTEGER PRIMARY KEY," + KEY_BanksName + " TEXT,"
                    + KEY_ReceiverBankCode + " TEXT,"
                    + KEY_DateCreated + " TEXT" + ")";
            db.execSQL(CREATE_Audio_Table);
        } catch (Exception e) {
            Log.e(TABLE_Banks, e.getMessage() + e.getStackTrace());
        }

        try {
            String CREATE_Table_MyCards = "CREATE TABLE IF NOT EXISTS " + TABLE_MyCards + "("
                    + KEY_MyCardId + " INTEGER PRIMARY KEY," + KEY_BanksName + " TEXT,"
                    + KEY_MyCardNo + " TEXT," + KEY_MyCardName + " TEXT,"
                    + KEY_SenderName + " TEXT," + KEY_DateCreated + " TEXT" + ")";
            db.execSQL(CREATE_Table_MyCards);
        } catch (Exception e) {
            Log.e(TABLE_MyCards, e.getMessage() + e.getStackTrace());
        }

    }


    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            // Drop older table if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TransferFunds);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_Banks);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MyCards);
            onCreate(db);
        } catch (Exception e) {


        }
    }

    public void resetDB(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TransferFunds);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MyCards);
            onCreate(db);
        } catch (Exception e) {


        }
    }

    public String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "dd-MMM-yyyy HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }


    // Adding new contact
    public int TransferFundsAdd(TransferFundsInfo sdInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_AccountNumber, sdInfo.getReceiverAccountNo()); // StudentAttendanceInfo Name
            values.put(KEY_AccountName, sdInfo.getReceiverAccountName()); // StudentAttendanceInfo Phone
            values.put(KEY_Amount, sdInfo.getAmount());
            values.put(KEY_DateCreated, sdInfo.getDateCreated());
            values.put(KEY_TransactionDetails, sdInfo.getTransactionDetails());
            values.put(KEY_TransactionCode, sdInfo.getTransactionCode()); // StudentAttendanceInfo Name
            values.put(KEY_TransactionStatus, sdInfo.getTransactionStatus()); // StudentAttendanceInfo Phone
            values.put(KEY_ReceiverBankCode, sdInfo.getReceiverBankCode());
            values.put(KEY_IsSuccess, sdInfo.isIsSuccess());
            values.put(KEY_SenderName, sdInfo.getSenderUsername());
            values.put(KEY_SenderFullName, sdInfo.getSenderFullName());
            // Inserting Row
            long id = db.insert(TABLE_TransferFunds, null, values);
            if (db.isOpen()) {
                //db.close();

            }
            return (int) id;
        } catch (Exception e) {

            return -1;
        }
    }

    // Getting single contact
    public TransferFundsInfo TransferFundsByIdGet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TransferFunds, null, KEY_TransferFundsId + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            TransferFundsInfo contact = TransferFundsParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }

    public TransferFundsInfo TransferFundsByAccountNoGet(String accountNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TransferFunds, null, KEY_AccountNumber + "=?",
                new String[]{String.valueOf(accountNo)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            TransferFundsInfo contact = TransferFundsParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }

    public TransferFundsInfo TransferFundsByCodeGet(String transactionCode) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TransferFunds, null, KEY_TransactionCode + "=?",
                new String[]{String.valueOf(transactionCode)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            TransferFundsInfo contact = TransferFundsParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }

    public List<TransferFundsInfo> TransferFundsByAccountNoList(String accountno) {
        List<TransferFundsInfo> contactList = new ArrayList<TransferFundsInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TransferFunds + " where " + KEY_AccountNumber + " like '%" + accountno + "%'  order by " + KEY_AccountNumber + " asc";
//
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransferFundsInfo contact = TransferFundsParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }

    public List<TransferFundsInfo> TransferFundsByStatusList(String isSuccess) {
        List<TransferFundsInfo> contactList = new ArrayList<TransferFundsInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TransferFunds + " where " + KEY_IsSuccess + "='" + isSuccess + "'"
                + "  order by " + KEY_DateCreated + " desc";
//
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransferFundsInfo contact = TransferFundsParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }

    private TransferFundsInfo TransferFundsParse(Cursor cursor) {

        TransferFundsInfo SmartGuyInfo = new TransferFundsInfo();

        SmartGuyInfo.setId(cursor.getInt(cursor.getColumnIndex(KEY_TransferFundsId)));
        SmartGuyInfo.setReceiverAccountNo(cursor.getString(cursor.getColumnIndex(KEY_AccountNumber)));
        SmartGuyInfo.setReceiverAccountName(cursor.getString(cursor.getColumnIndex(KEY_AccountName)));
        SmartGuyInfo.setDateCreated(cursor.getString(cursor.getColumnIndex(KEY_DateCreated)));
        SmartGuyInfo.setAmount(cursor.getDouble(cursor.getColumnIndex(KEY_Amount)));
        SmartGuyInfo.setTransactionDetails(cursor.getString(cursor.getColumnIndex(KEY_TransactionDetails)));
        SmartGuyInfo.setTransactionCode(cursor.getString(cursor.getColumnIndex(KEY_TransactionCode)));
        SmartGuyInfo.setTransactionStatus(cursor.getString(cursor.getColumnIndex(KEY_TransactionStatus)));
        SmartGuyInfo.setIsSuccess(cursor.getString(cursor.getColumnIndex(KEY_IsSuccess)));
        SmartGuyInfo.setReceiverBankCode(cursor.getString(cursor.getColumnIndex(KEY_ReceiverBankCode)));
        SmartGuyInfo.setSenderUsername(cursor.getString(cursor.getColumnIndex(KEY_SenderName)));
        SmartGuyInfo.setSenderFullName(cursor.getString(cursor.getColumnIndex(KEY_SenderFullName)));
        return SmartGuyInfo;

    }

    // Getting All Contacts
    public List<TransferFundsInfo> TransferFundsList() {
        List<TransferFundsInfo> contactList = new ArrayList<TransferFundsInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TransferFunds + " order by " + KEY_TransferFundsId + " desc LIMIT 300";
//
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransferFundsInfo contact = TransferFundsParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }


    public List<TransferFundsInfo> TransferFundsByUsernameList(String username) {
        List<TransferFundsInfo> contactList = new ArrayList<TransferFundsInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TransferFunds + " where " + KEY_SenderName + "='" + username + "' order by " + KEY_TransferFundsId + " desc LIMIT 300";
        //
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                TransferFundsInfo contact = TransferFundsParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }


    // Updating single contact
    public int TransferFundsUpdate(TransferFundsInfo sdInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_AccountNumber, sdInfo.getReceiverAccountNo()); // StudentAttendanceInfo Name
        values.put(KEY_AccountName, sdInfo.getReceiverAccountName()); // StudentAttendanceInfo Phone
        values.put(KEY_Amount, sdInfo.getAmount()); // StudentAttendanceInfo Phone
        //values.put(KEY_TransferFundsId, sdInfo.getId());
        values.put(KEY_TransactionDetails, sdInfo.getTransactionDetails());
        values.put(KEY_SenderName, sdInfo.getSenderUsername());
        values.put(KEY_SenderFullName, sdInfo.getSenderFullName());
        values.put(KEY_TransactionCode, sdInfo.getTransactionCode()); // StudentAttendanceInfo Name
        values.put(KEY_TransactionStatus, sdInfo.getTransactionStatus()); // StudentAttendanceInfo Phone
        values.put(KEY_ReceiverBankCode, sdInfo.getReceiverBankCode());
        values.put(KEY_IsSuccess, sdInfo.isIsSuccess());
        // updating row
        return db.update(TABLE_TransferFunds, values, KEY_TransferFundsId + " = ?",
                new String[]{String.valueOf(sdInfo.getId())});
    }

    // Deleting single contact
    public void TransferFundsDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TransferFunds, KEY_TransferFundsId + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    // Getting contacts Count
    public int TransferFundsCount() {
        String countQuery = "SELECT  Count(*) FROM " + TABLE_TransferFunds;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int cnt = cursor.getInt(0);
            cursor.close();

            // return count
            return cnt;
        }
        return 0;
    }



    // Adding new contact


    public int BanksAdd(BanksInfo sdInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_BanksName, sdInfo.getBankName()); // StudentAttendanceInfo Name
            values.put(KEY_ReceiverBankCode, sdInfo.getBankCode());
            // Inserting Row
            long id = db.insert(TABLE_Banks, null, values);
            if (db.isOpen()) {
                //db.close();

            }
            return (int) id;
        } catch (Exception e) {

            return -1;
        }
    }

    //Getting single contact
    public BanksInfo BanksByIdGet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Banks, null, KEY_BanksId + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            BanksInfo contact = BanksParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }

    public BanksInfo BanksByCodeGet(String bankCode) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_Banks, null, KEY_ReceiverBankCode + "=?",
                new String[]{String.valueOf(bankCode)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            BanksInfo contact = BanksParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }


    private BanksInfo BanksParse(Cursor cursor) {

        BanksInfo SmartGuyInfo = new BanksInfo();

        SmartGuyInfo.setId(cursor.getInt(cursor.getColumnIndex(KEY_BanksId)));
        SmartGuyInfo.setBankName(cursor.getString(cursor.getColumnIndex(KEY_BanksName)));
        SmartGuyInfo.setBankCode(cursor.getString(cursor.getColumnIndex(KEY_ReceiverBankCode)));

        return SmartGuyInfo;

    }

    // Getting All Contacts
    public List<BanksInfo> BanksList() {
        List<BanksInfo> contactList = new ArrayList<BanksInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_Banks + " order by " + KEY_BanksName + " asc LIMIT 300";
//
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BanksInfo contact = BanksParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }

    // Updating single contact
    public int BanksUpdate(BanksInfo sdInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_BanksName, sdInfo.getBankName()); // StudentAttendanceInfo Name
        values.put(KEY_ReceiverBankCode, sdInfo.getBankCode()); // StudentAttendanceInfo Phone
        values.put(KEY_BanksId, sdInfo.getId());

        // updating row
        return db.update(TABLE_Banks, values, KEY_BanksId + " = ?",
                new String[]{String.valueOf(sdInfo.getId())});
    }

    //Deleting single contact
    public void BanksByIdDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_Banks, KEY_BanksId + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    // Getting contacts Count
    public int BanksCount() {
        String countQuery = "SELECT  Count(*) FROM " + TABLE_Banks;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int cnt = cursor.getInt(0);
            cursor.close();

            // return count
            return cnt;
        }
        return 0;
    }


    public int MyCardAdd(MyCardInfo sdInfo) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_SenderName, sdInfo.getUsername());
            values.put(KEY_MyCardName, sdInfo.getMyCardName()); // StudentAttendanceInfo Name
            values.put(KEY_MyCardNo, sdInfo.getMyCardNo());
            values.put(KEY_BanksName, sdInfo.getBankName()); // StudentAttendanceInfo Name
            values.put(KEY_DateCreated, getDateTime());
            // Inserting Row
            long id = db.insert(TABLE_MyCards, null, values);
            if (db.isOpen()) {
                //db.close();

            }
            return (int) id;
        } catch (Exception e) {

            return -1;
        }
    }

    //Getting single contact
    public MyCardInfo MyCardByIdGet(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MyCards, null, KEY_MyCardId + "=?",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            MyCardInfo contact = MyCardParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }

    public MyCardInfo MyCardByCardNoGet(String cardNo) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_MyCards, null, KEY_MyCardNo + "=?",
                new String[]{String.valueOf(cardNo)}, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            MyCardInfo contact = MyCardParse(cursor);
            cursor.close();
            return contact;
        }

        return null;
    }


    private MyCardInfo MyCardParse(Cursor cursor) {

        MyCardInfo SmartGuyInfo = new MyCardInfo();

        SmartGuyInfo.setId(cursor.getInt(cursor.getColumnIndex(KEY_MyCardId)));
        SmartGuyInfo.setBankName(cursor.getString(cursor.getColumnIndex(KEY_BanksName)));
        SmartGuyInfo.setMyCardNo(cursor.getString(cursor.getColumnIndex(KEY_MyCardNo)));
        SmartGuyInfo.setMyCardName(cursor.getString(cursor.getColumnIndex(KEY_MyCardName)));

        return SmartGuyInfo;

    }

    // Getting All Contacts
    public List<MyCardInfo> MyCardList() {
        List<MyCardInfo> contactList = new ArrayList<MyCardInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MyCards + " order by " + KEY_MyCardId + " desc LIMIT 300";
//
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyCardInfo contact = MyCardParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }

    public List<MyCardInfo> MyCardByUsernameList(String username) {
        List<MyCardInfo> contactList = new ArrayList<MyCardInfo>();
        // Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_MyCards + " where " + KEY_SenderName + "='" + username + "' order by " + KEY_MyCardId + " desc LIMIT 300";
//
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MyCardInfo contact = MyCardParse(cursor);
                // Adding contact to list
                contactList.add(contact);
            } while (cursor.moveToNext());
            cursor.close();
        }

        // return contact list
        return contactList;
    }


    // Updating single contact
    public int MyCardUpdate(MyCardInfo sdInfo) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SenderName, sdInfo.getUsername());
        values.put(KEY_MyCardName, sdInfo.getMyCardName()); // StudentAttendanceInfo Name
        values.put(KEY_MyCardNo, sdInfo.getMyCardNo());
        values.put(KEY_BanksName, sdInfo.getBankName()); // StudentAttendanceInfo Name
        values.put(KEY_DateCreated, getDateTime());
        values.put(KEY_MyCardId, sdInfo.getId());

        // updating row
        return db.update(TABLE_MyCards, values, KEY_MyCardId + " = ?",
                new String[]{String.valueOf(sdInfo.getId())});
    }

    //Deleting single contact
    public void MyCardByIdDelete(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MyCards, KEY_MyCardId + " = ?",
                new String[]{String.valueOf(id)});
        //db.close();
    }

    //Getting contacts Count
    public int MyCardCount() {
        String countQuery = "SELECT  Count(*) FROM " + TABLE_MyCards;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        if (cursor != null) {
            cursor.moveToFirst();

            int cnt = cursor.getInt(0);
            cursor.close();

            // return count
            return cnt;
        }
        return 0;
    }


    public String getGMTDateTime(){
         DateFormat df = DateFormat.getTimeInstance();
         df.setTimeZone(TimeZone.getTimeZone("gmt"));
         String gmtTime = df.format(new Date());

         return  gmtTime;
     }


 
}

