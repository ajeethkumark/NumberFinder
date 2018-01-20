package com.example.ajeethkumark.imagescanning;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static com.example.ajeethkumark.imagescanning.DataBaseHelper.table3Name;
import static com.example.ajeethkumark.imagescanning.DataBaseHelper.tableName;

public class ManualDataEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_data_entry);
        ActionBar ab=getSupportActionBar();
        if(ab!=null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        ManualDataEntryFragment mdef=new ManualDataEntryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.relative, mdef, mdef.getClass().getSimpleName())
                .commit();
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
}
