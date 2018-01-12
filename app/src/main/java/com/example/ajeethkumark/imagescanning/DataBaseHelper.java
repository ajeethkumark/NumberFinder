package com.example.ajeethkumark.imagescanning;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Ajeethkumar k on 12/23/2017.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    public static final int version=3;
    public static final String databaseName="ImageScanning.db";
    public static final String tableName="ImageData";
    public static final String col_1="num_id";
    public static final String col_2="table_id";
    public static final String col_3="number";
    public static final String col_4="before_num";
    public static final String col_5="after_num";
    Context context;
    DataBaseHelper dbh;
    SQLiteDatabase sqLiteDatabase;
    ContentValues cv;

    public DataBaseHelper(Context context) {
        super(context, databaseName, null, version);
        this.context= context;
       // openConnection(context);
        //this.sqLiteDatabase=this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
      //  Toast.makeText(context,"table created is called",Toast.LENGTH_SHORT).show();
        sqLiteDatabase.execSQL("create table "+tableName+ " ("+col_1+" INTEGER  PRIMARY KEY AUTOINCREMENT ,"+col_2+" INTEGER ,"+col_3+" INTEGER,"+col_4+" TEXT ,"+col_5+" TEXT)");
       // Toast.makeText(context,"table created",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableName);
        onCreate(sqLiteDatabase);
       // Toast.makeText(context,"table is droped",Toast.LENGTH_SHORT).show();

    }
    public StringBuilder insertData(Context context,StringBuilder sb)
    {
        StringBuilder returnBuilder = new StringBuilder();
        try {
            this.sqLiteDatabase = openConnection(context);
            StringBuilder numberData = sb;

            String temp, db_temp_dataBefore, db_temp_dataAfter;
            String outerSeparate[], innerSeparate[];
            int i, j, joinNumber = 0, dummy, db_temp_key_value, flag = 0;
            outerSeparate = numberData.toString().split("\\n");
            for (i = 0; i < outerSeparate.length; i++) {
                temp = outerSeparate[i];
                innerSeparate = temp.split(" ");
                if (innerSeparate.length > 1) {
                    for (j = 0; innerSeparate.length > j; j++) {
                        if (j == 0) {
                            if (!(innerSeparate[j + 1].length() >= 3) && !(innerSeparate[j].length() >= 3) && numberCheck(innerSeparate[j], innerSeparate[j + 1])) {
                                cv = new ContentValues();
                                cv.put(col_2, 1);
                                cv.put(col_3, innerSeparate[j]);
                                cv.put(col_4, innerSeparate[j + 1]);
                                cv.put(col_5, -1);
                                long result = this.sqLiteDatabase.insert(tableName, null, cv);
                                if (result != -1) {
                                    returnBuilder.append(innerSeparate[j] + "-");
                                    Log.d("DataInsertInfo:", "Data Inserted First");
                                    // Toast.makeText(context, innerSeparate[j + 1], Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("DataInsertInfo", "Data Not Inserted First");
                                    // Toast.makeText(context, innerSeparate[j + 1] + "...fail", Toast.LENGTH_LONG).show();
                                }
                            }
                        /*else
                        {

                            cv=new ContentValues();
                            cv.put(col_2,1);
                            cv.put(col_3,1000);
                            cv.put(col_4,1000);
                            cv.put(col_5,1000);
                            long result = this.sqLiteDatabase.insert(tableName, null, cv);
                            if (result != -1) {
                                returnBuilder.append("1000"+" ");
                                Log.d("DataInsertInfo:", "Data Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1], Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("DataInsertInfo", "Data Not Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1] + "...fail", Toast.LENGTH_LONG).show();
                            }

                        }*/
                        }
                        //   else if(j!=0 && j!=((innerSeparate.length)-1))
                        //  {
                        else if (!(j == ((innerSeparate.length) - 1)) && !(innerSeparate[j].length() >= 3) && numberCheck(innerSeparate[j], "20")) {
                            cv = new ContentValues();
                            cv.put(col_2, 1);
                            cv.put(col_3, innerSeparate[j]);
                            if (numberCheck(innerSeparate[j + 1], innerSeparate[j]) && !(innerSeparate[j + 1].length() >= 3)) {
                                cv.put(col_4, innerSeparate[j + 1]);
                            } else {
                                cv.put(col_4, -1);
                            }
                            if (numberCheck(innerSeparate[j], innerSeparate[j - 1]) && !(innerSeparate[j - 1].length() >= 3)) {
                                cv.put(col_5, innerSeparate[j - 1]);
                            } else {
                                cv.put(col_5, -1);
                            }
                            long result = this.sqLiteDatabase.insert(tableName, null, cv);
                            if (result != -1) {
                                returnBuilder.append(innerSeparate[j] + "-");
                                Log.d("DataInsertInfo:", "Data Inserted First");
                                //Toast.makeText(context, innerSeparate[j + 1], Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("DataInsertInfo", "Data Not Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1] + "...fail", Toast.LENGTH_LONG).show();
                            }
                            //  }
                      /*  else
                        {
                            cv=new ContentValues();
                            cv.put(col_2,1);
                            cv.put(col_3,1000);
                            cv.put(col_4,1000);
                            cv.put(col_5,1000);
                            long result = this.sqLiteDatabase.insert(tableName, null, cv);
                            if (result != -1) {
                                returnBuilder.append("1000"+" ");
                                Log.d("DataInsertInfo:", "Data Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1], Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("DataInsertInfo", "Data Not Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1] + "...fail", Toast.LENGTH_LONG).show();
                            }

                        }*/
                        } else if (j == (innerSeparate.length) - 1) {
                            if (!(innerSeparate[j].length() >= 3) && numberCheck(innerSeparate[j - 1], innerSeparate[j]) && !(innerSeparate[j - 1].length() >= 3)) {
                                cv = new ContentValues();
                                cv.put(col_2, 1);
                                cv.put(col_3, innerSeparate[j]);
                                cv.put(col_4, -1);
                                cv.put(col_5, innerSeparate[j - 1]);
                                long result = this.sqLiteDatabase.insert(tableName, null, cv);
                                if (result != -1) {
                                    returnBuilder.append(innerSeparate[j] + "-");
                                    Log.d("DataInsertInfo:", "Data Inserted First");
                                    // Toast.makeText(context, innerSeparate[j - 1], Toast.LENGTH_LONG).show();
                                } else {
                                    Log.d("DataInsertInfo", "Data Not Inserted First");
                                    // Toast.makeText(context, innerSeparate[j + 1] + "...fail", Toast.LENGTH_LONG).show();
                                }
                            }
                       /* else
                        {
                            cv=new ContentValues();
                            cv.put(col_2,1);
                            cv.put(col_3,1000);
                            cv.put(col_4,1000);
                            cv.put(col_5,1000);
                            long result = this.sqLiteDatabase.insert(tableName, null, cv);
                            if (result != -1) {
                                returnBuilder.append("1000"+" ");
                                Log.d("DataInsertInfo:", "Data Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1], Toast.LENGTH_LONG).show();
                            } else {
                                Log.d("DataInsertInfo", "Data Not Inserted First");
                                // Toast.makeText(context, innerSeparate[j + 1] + "...fail", Toast.LENGTH_LONG).show();
                            }
                        }*/
                        }

                    }
                }

            }


            return returnBuilder;
        }
        catch(Exception e)
        {
            Toast.makeText(context,"Take the photo clearly",Toast.LENGTH_LONG).show();
            returnBuilder=new StringBuilder();
            return  returnBuilder;
        }
        finally {
            closeConnection();
        }

    }
    public boolean numberCheck(String no1,String no2)
    {
        try{
           int temp=Integer.parseInt(no1)+Integer.parseInt(no2);
           if(Integer.parseInt(no1)<37 && Integer.parseInt(no2)<37)
               return true;
           else return false;

        }
        catch (NumberFormatException e)
        {
            return  false;
        }
    }
    public static DataBaseHelper getDataBaseHelperInstance(Context context)
    {
        return new DataBaseHelper(context);
    }
    public SQLiteDatabase openConnection(Context context)
    {
        this.sqLiteDatabase=DataBaseHelper.getDataBaseHelperInstance(context).getWritableDatabase();
        return sqLiteDatabase;

    }
    public void closeConnection()
    {
       // this.sqLiteDatabase=DataBaseHelper.getDataBaseHelperInstance()
        if(this.sqLiteDatabase!=null && this.sqLiteDatabase.isOpen())
        {
            this.sqLiteDatabase.close();
        }
    }
    public Cursor getParticular_db_Data(String value,Context context)
    {
        if(sqLiteDatabase==null )
        {
            openConnection(context);
        }
        Cursor cursor=sqLiteDatabase.rawQuery("select * from ImageData where number = "+Integer.parseInt(value)+" AND table_id=1",null);

        return cursor;
    }
    public Cursor getData(Context context)
    {
        DataBaseHelper dbh=new DataBaseHelper(context);
        this.sqLiteDatabase=DataBaseHelper.getDataBaseHelperInstance(context).getWritableDatabase();
        Cursor res = sqLiteDatabase.rawQuery("select * from " + tableName , null);
        return res;
    }
}