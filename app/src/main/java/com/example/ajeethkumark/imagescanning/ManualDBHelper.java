package com.example.ajeethkumark.imagescanning;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by Ajeethkumar k on 1/25/2018.
 */

public class ManualDBHelper extends SQLiteAssetHelper {
    private static final String DB_NAME = "manualdatatable.sqlite";
    private static final int DB_VERSION = 1;
    ManualDBHelper db;
    SQLiteDatabase sqLiteDatabase;
    public ManualDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public static ManualDBHelper getManualDBInstance(Context context)
    {
        return new ManualDBHelper(context);
    }
    public SQLiteDatabase openConnection(Context context)
    {
        this.sqLiteDatabase=ManualDBHelper.getManualDBInstance(context).getWritableDatabase();
        return sqLiteDatabase;
    }
    public void closeConnection()
    {
        if(this.sqLiteDatabase!=null )
        {
            if(this.sqLiteDatabase.isOpen())
                this.sqLiteDatabase.close();
        }
    }
    public Cursor getAllManualData(SQLiteDatabase sqlite)
    {
        this.sqLiteDatabase=sqlite;
        Cursor res = sqLiteDatabase.rawQuery("select * from sheet1" , null);
        return res;

    }
    public Cursor getParticularData(Context context,String value)
    {
        if(this.sqLiteDatabase==null)
        {
            this.sqLiteDatabase=openConnection(context);
        }
        Cursor cursor=this.sqLiteDatabase.rawQuery("select * from sheet1 where num ="+value,null);
        return cursor;
    }

}
