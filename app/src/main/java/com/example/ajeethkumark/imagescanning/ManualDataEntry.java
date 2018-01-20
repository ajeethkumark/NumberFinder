package com.example.ajeethkumark.imagescanning;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ManualDataEntry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_data_entry);
        ManualDataEntryFragment mdef=new ManualDataEntryFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.relative, mdef, mdef.getClass().getSimpleName())
                .commit();
    }
}
