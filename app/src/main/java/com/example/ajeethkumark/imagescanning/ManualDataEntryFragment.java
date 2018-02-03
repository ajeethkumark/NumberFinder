package com.example.ajeethkumark.imagescanning;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManualDataEntryFragment extends Fragment {
   // EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8,ed9,ed10,ed11,ed12,ed13,ed14,ed15,ed16,ed17,ed18,ed19,ed20,ed21,ed22,ed23,ed24,ed25;
    EditText[] ed=new EditText[25];
    TextView saveAnchor,refreshAnchor;
    FloatingActionButton saveFloatButton,refreshFloatbutton;
    final int[] cancelFlag = {0};
    final int[] lastDataFlag = {0};
    int[] userData;
    int userDataReCheck[];
    int hitCount=0;
    int emptyCount,numberCheckCount;
    Boolean flag;
    final String[] e1 = new String[25];
    final ArrayList<Integer> value=new ArrayList<>();
    CommunicationFragment cf;
    View v;
    public ManualDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //final int[] value=new int[14];

        // Inflate the layout for this fragment
         v= inflater.inflate(R.layout.fragment_manual_data_entry, container, false);

        ed[0]=v.findViewById(R.id.ed_no1);
        ed[1]=v.findViewById(R.id.ed_no2);
        ed[2]=v.findViewById(R.id.ed_no3);
        ed[3]=v.findViewById(R.id.ed_no4);
        ed[4]=v.findViewById(R.id.ed_no5);
        ed[5]=v.findViewById(R.id.ed_no6);
        ed[6]=v.findViewById(R.id.ed_no7);
        ed[7]=v.findViewById(R.id.ed_no8);
        ed[8]=v.findViewById(R.id.ed_no9);
        ed[9]=v.findViewById(R.id.ed_no10);
        ed[10]=v.findViewById(R.id.ed_no11);
        ed[11]=v.findViewById(R.id.ed_no12);
        ed[12]=v.findViewById(R.id.ed_no13);
        ed[13]=v.findViewById(R.id.ed_no14);
        ed[14]=v.findViewById(R.id.ed_no15);
        ed[15]=v.findViewById(R.id.ed_no16);
        ed[16]=v.findViewById(R.id.ed_no17);
        ed[17]=v.findViewById(R.id.ed_no18);
        ed[18]=v.findViewById(R.id.ed_no19);
        ed[19]=v.findViewById(R.id.ed_no20);
        ed[20]=v.findViewById(R.id.ed_no21);
        ed[21]=v.findViewById(R.id.ed_no22);
        ed[22]=v.findViewById(R.id.ed_no23);
        ed[23]=v.findViewById(R.id.ed_no24);
        ed[24]=v.findViewById(R.id.ed_no25);
        saveFloatButton=v.findViewById(R.id.fab);
        refreshFloatbutton=v.findViewById(R.id.fab_refresh);
        saveAnchor=v.findViewById(R.id.save_anchor);
        refreshAnchor=v.findViewById(R.id.refresh_anchor);
        existInfo();
        saveFloatButton.setBackgroundColor(Color.parseColor("#0066ff"));

        saveFloatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getContext(),"clicked",Toast.LENGTH_LONG).show();
                emptyCount=0;
                for(int i=0;i<25;i++)
                {
                    e1[i]=ed[i].getText().toString().trim();
                }
                for(int i=0;i<25;i++)
                {
                    if(!e1[i].isEmpty())
                    {
                        emptyCount++;
                    }
                    else
                    {
                        e1[i]="-1";
                    }
                }
                userData=new int[emptyCount];
                userDataReCheck=new int[emptyCount];
                if(emptyCount>=4)
                {
                    numberCheckCount=1;
                    for(int i=0;i<25;i++)
                    {
                       flag= numberCheck(e1[i]);
                       if(!flag)
                       {
                           numberCheckCount=0;
                       }
                    }
                    if(numberCheckCount==1) {
                        if (numberRangeCheck()) {
                            int j=0;
                            value.clear();
                            for (int i = 0; i < 25; i++) {
                                value.add(Integer.parseInt(e1[i]));
                            }
                            for(j=0;j<25;j++)
                            {
                                if(Integer.parseInt(e1[j])==-1)
                                {
                                    break;
                                }
                                userData[j]=Integer.parseInt(e1[j]);
                                Log.d("userData",e1[j]);
                            }
                            showAlert(j);


                        }
                        else
                        {
                            Toast.makeText(getActivity(),"greater than 36 Not allow",Toast.LENGTH_LONG).show();
                        }


                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Don't enter other than Number",Toast.LENGTH_LONG).show();
                    }

                }
                else{
                    Toast.makeText(getActivity(),"pls enter more data to validate",Toast.LENGTH_LONG).show();
                }


            }
        });

        refreshFloatbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                saveFloatButton.setVisibility(View.VISIBLE);
                saveAnchor.setVisibility(View.VISIBLE);
               for(int i=0;i<25;i++)
               {
                   ed[i].setText("");
               }
               //ed[0].requestFocus();
            }
        });

        return v;
    }

    private void existInfo() {
        cf=(CommunicationFragment)getActivity();
        cf.response(1);
    }

    private void showAlert(int j) {
       final int temp;
        temp=j;
        AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
        builder1.setTitle("Please verify the order of winning numbers");
        builder1.setMessage("Current winning number:"+e1[j-1]+"\n"+"previous winning number:"+e1[j-2]);
        builder1.setCancelable(true);
        builder1.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                                    cancelFlag[0]=0;
                                    lastDataFlag[0]=0;
                                    DataBaseHelper.getDataBaseHelperInstance(getContext()).insertManualData(getContext(), value);
                                    Toast.makeText(getContext(), "Data Registered", Toast.LENGTH_LONG).show();
                                    int a=hitResult("direct");
                                    Log.d("HitReturnCheck.........",Integer.toString(a));
                                    displayResult(temp);

            }
        });
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                                    AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
                                     builder2.setTitle("Please verify the order of winning numbers");
                                     builder2.setMessage("Current winning number:"+e1[0]+"\n"+"previous winning number:"+e1[1]);
                                    builder2.setCancelable(true);
                                    builder2.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            lastDataFlag[0] =1;
                                            cancelFlag[0]=0;
                                            Collections.reverse(value);
                                            DataBaseHelper.getDataBaseHelperInstance(getContext()).insertManualData(getContext(), value);
                                            Toast.makeText(getContext(), "Data Registered", Toast.LENGTH_LONG).show();
                                            displayResult(temp);
                                        }
                                    });
                                    builder2.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            cancelFlag[0] =1;
                                        }
                                    });
                                    AlertDialog alertDialog=builder2.create();
                                    alertDialog.show();
               // cancelFlag[0]=1;
               // Toast.makeText(getActivity(),"No funtion available",Toast.LENGTH_LONG).show();

            }
        });
        AlertDialog alertDialog=builder1.create();
        alertDialog.show();
    }
    private int hitResult(String temp)
    {
        int[] reverseData=new int[emptyCount];
        int j=0;
        String temp2=temp;
        for(int i=0;i<userDataReCheck.length;i++)
        {
            userDataReCheck[i]=0;
        }
        if(!(temp2.equals("direct")))
        {
           for(int i=userData.length;i>=0;i--,j++)
           {
               reverseData[j]=userData[i];
           }
           for(int i=0;i<userData.length;i++)
           {
               userData[i]=reverseData[i];
           }
           temp2="direct";
        }
        int flag=0;
            if(temp2.equals("direct"))
            {
                for(int i=0;i<userData.length-1;i++)
                {
                    Cursor cursor=DataBaseHelper.getDataBaseHelperInstance(getContext()).getParticular_db_Data(Integer.toString(userData[i]),getContext(),"ImageData");
                    if(cursor!=null && cursor.getCount()>0)
                    {
                        while (cursor.moveToNext()){


                            if (userData[i + 1] == cursor.getInt(cursor.getColumnIndexOrThrow("after_num"))) {
                                hitCount++;
                                userDataReCheck[i] = 1;
                               // flag=1;
                            }

                        }

                    }
                    if(userDataReCheck[i]!=1)
                    {
                        Cursor res = ManualDBHelper.getManualDBInstance(getContext()).getParticularData(getContext(), Integer.toString(userData[i]));
                        if (res.getCount()>0 && res!=null)
                        {
                            while (res.moveToNext())
                            {
                                if(userData[i+1]==res.getInt(res.getColumnIndexOrThrow("aft_num")))
                                {
                                    hitCount++;
                                }
                            }
                        }
                    }

                }
                DataBaseHelper.getDataBaseHelperInstance(getContext()).closeConnection();
                ManualDBHelper.getManualDBInstance(getContext()).closeConnection();
            }
            if(((hitCount%emptyCount)*100)>=20)
                return 1;
            else
        return 0;
    }


    private void displayResult(int j) {
        InputMethodManager inputManager = (InputMethodManager) getActivity()
                .getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(v.getWindowToken(),0);
        Bundle b=new Bundle();
        if(lastDataFlag[0]==0) {
            b.putString("value", e1[j - 1]);
        }
        else
        {
            b.putString("value",e1[0]);
        }
        b.putInt("flag",1);
        b.putInt("dbData",1);
        FinalResultFragment frf=new FinalResultFragment();
        frf.setArguments(b);
        if(cancelFlag[0] ==0) {
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative, frf).commit();
        }
    }

    private boolean numberRangeCheck()
    {
        for(int i=0;i<25;i++)
        {
            if(Integer.parseInt(e1[i])>36)
            {
                return false;
            }
        }
        return true;
    }

    private boolean numberCheck(String e1) {
        try{
            int i=Integer.parseInt(e1);
            return true;
        }
        catch(NumberFormatException e)
        {
            return false;
        }
        catch(Exception e)
        {
            return false;
        }
    }

}
