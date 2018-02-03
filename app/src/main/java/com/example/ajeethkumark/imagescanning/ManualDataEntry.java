package com.example.ajeethkumark.imagescanning;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import static com.example.ajeethkumark.imagescanning.DataBaseHelper.table3Name;
import static com.example.ajeethkumark.imagescanning.DataBaseHelper.tableName;

public class ManualDataEntry extends AppCompatActivity implements  CommunicationFragment{

    int existFlag=0,hitCount=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_data_entry);

        ManualDataEntryFragment mdef=new ManualDataEntryFragment();
        FragmentManager fm=getSupportFragmentManager();
       // Toast.makeText(ManualDataEntry.this,"hello..",Toast.LENGTH_LONG).show();
       // fm.popBackStack(mdef.getTag(),0);
        FragmentTransaction tr=fm.beginTransaction();
                tr.replace(R.id.relative, mdef, mdef.getClass().getSimpleName());
                tr.commit();
    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataBaseHelper.getDataBaseHelperInstance(ManualDataEntry.this).openConnection(ManualDataEntry.this).execSQL("Delete from " + table3Name);
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(ManualDataEntry.this,"back pressed",Toast.LENGTH_LONG).show();
        if(existFlag!=1) {
            PredictionNumberFragment pnf = new PredictionNumberFragment();
            Bundle b = new Bundle();
            b.putInt("dbData", 0);
            pnf.setArguments(b);
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction tr = fm.beginTransaction();
            tr.replace(R.id.relative, pnf).commit();
            //super.onBackPressed();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    public void response(int value) {
        existFlag=value;

    }

    @Override
    public void hitResponse(int hitValue) {

    }
}
