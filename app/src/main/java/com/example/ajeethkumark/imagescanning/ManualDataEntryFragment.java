package com.example.ajeethkumark.imagescanning;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManualDataEntryFragment extends Fragment {
   // EditText ed1,ed2,ed3,ed4,ed5,ed6,ed7,ed8,ed9,ed10,ed11,ed12,ed13,ed14,ed15,ed16,ed17,ed18,ed19,ed20,ed21,ed22,ed23,ed24,ed25;
    EditText[] ed=new EditText[25];
    TextView saveAnchor,refreshAnchor;
    FloatingActionButton saveFloatButton,refreshFloatbutton;
    Button prediction;
    int emptyCount,numberCheckCount;
    Boolean flag;
    final String[] e1 = new String[25];

    public ManualDataEntryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        //final int[] value=new int[14];
        final ArrayList<Integer> value=new ArrayList<>();

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_manual_data_entry, container, false);
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
        prediction=(Button)v.findViewById(R.id.prediction);
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
                    else {
                        e1[i]="-1";
                    }
                }
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

                            value.clear();
                            for (int i = 0; i < 25; i++) {
                                value.add(Integer.parseInt(e1[i]));
                            }
                            DataBaseHelper.getDataBaseHelperInstance(getContext()).insertManualData(getContext(), value);
                            Toast.makeText(getContext(), "Data Registered", Toast.LENGTH_LONG).show();
                            saveFloatButton.setVisibility(View.INVISIBLE);
                            saveAnchor.setVisibility(View.INVISIBLE);
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


              /* if(!e1[0].isEmpty() && !e2[0].isEmpty() && !e3[0].isEmpty() && !e4[0].isEmpty() && !e5[0].isEmpty() && !e6[0].isEmpty()
                        && !e7[0].isEmpty() && !e8[0].isEmpty() && !e9[0].isEmpty() && !e10[0].isEmpty() && !e11[0].isEmpty() && !e12[0].isEmpty()
                        && !e13[0].isEmpty() && !e14[0].isEmpty())
                         {
                        //if(ed1.getText())
                           // Toast.makeText(getContext(),"clicked",Toast.LENGTH_LONG).show();
                             if(numberCheck(e1[0]) && numberCheck(e2[0]) && numberCheck(e3[0]) && numberCheck(e4[0]) && numberCheck(e5[0]) && numberCheck(e6[0])
                                     && numberCheck(e7[0]) && numberCheck(e8[0]) && numberCheck(e9[0]) && numberCheck(e10[0]) && numberCheck(e11[0])
                                     && numberCheck(e12[0]) && numberCheck(e13[0]) && numberCheck(e14[0]))
                                    {
                                        value.clear();
                                        value.add(Integer.parseInt(e1[0]));value.add(Integer.parseInt(e2[0]));value.add(Integer.parseInt(e3[0]));
                                        value.add(Integer.parseInt(e4[0]));value.add(Integer.parseInt(e5[0]));value.add(Integer.parseInt(e6[0]));value.add(Integer.parseInt(e7[0]));
                                        value.add(Integer.parseInt(e8[0]));value.add(Integer.parseInt(e9[0]));value.add(Integer.parseInt(e10[0]));value.add(Integer.parseInt(e11[0]));
                                        value.add(Integer.parseInt(e13[0]));value.add(Integer.parseInt(e14[0]));
                                        DataBaseHelper.getDataBaseHelperInstance(getContext()).insertManualData(getContext(),value);
                                        Toast.makeText(getContext(),"Data Registered",Toast.LENGTH_LONG).show();
                                        saveFloatButton.setVisibility(View.INVISIBLE);
                                        saveAnchor.setVisibility(View.INVISIBLE);

                                    }
                             else{
                                 Toast.makeText(getContext(),"Don't enter other than number",Toast.LENGTH_LONG).show();
                             }
                         }
                else {
                    Toast.makeText(getContext(),"Enter the all data",Toast.LENGTH_LONG).show();
                }*/
            }
        });
        prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),PredictionNumber.class);
                i.putExtra("flag",3);
                startActivity(i);
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
            }
        });

        return v;
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
