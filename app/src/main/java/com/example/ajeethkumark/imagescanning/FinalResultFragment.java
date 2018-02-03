package com.example.ajeethkumark.imagescanning;


import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;


/**
 * A simple {@link Fragment} subclass.
 */
public class FinalResultFragment extends Fragment {
    RecyclerView recyclerView;
    TextView text;
    Button prediction,play;
    Cursor cursor;
    CommunicationFragment cf;
    String value;
    StringBuilder afterData=new StringBuilder();
    TreeSet<Integer> treeHotData=new TreeSet<>();
    HashMap<Integer,Float> mapHotData=new HashMap<>();
    ArrayList<Integer> avgProbability=new ArrayList<>();
    ArrayList<Integer> lowProbability=new ArrayList<>();

    ArrayList<Integer> avgProbabilityOptimize=new ArrayList<>();
    ArrayList<Integer> lowProbabilityOptimize=new ArrayList<>();

    public static int LAST_DB_NUMBER;


    public FinalResultFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_final_result, container, false);

        int flag=1,dbData;
        String j;
        // Inflate the layout for this fragment

        recyclerView=v.findViewById(R.id.recyclerview);
        text=v.findViewById(R.id.textinfo);
        prediction=v.findViewById(R.id.prediction);
        play=v.findViewById(R.id.play);
        existInfo();
       // recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),10));............................................................................................
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle b=getArguments();
        value=b.getString("value");
        flag=b.getInt("flag");
        dbData=b.getInt("dbData");

        if(dbData==1)
        {
            LAST_DB_NUMBER=Integer.parseInt(value);
        }
        else if(dbData==2)
        {
            ArrayList<Integer> list=new ArrayList<>();
            list.add(LAST_DB_NUMBER);
            list.add(Integer.parseInt(value));
            DataBaseHelper.getDataBaseHelperInstance(getActivity()).insertManualData(getActivity(),list);
            LAST_DB_NUMBER=Integer.parseInt(value);
        }

       if(flag==1) {
           int manualDataCount=0,hit=0;

            cursor = DataBaseHelper.getDataBaseHelperInstance(getActivity()).getParticular_db_Data(value, getActivity(), "ManualData");
            DataBaseHelper.getDataBaseHelperInstance(getActivity()).closeConnection();

            if (cursor != null && cursor.getCount() > 0) {
                text.setText(value);
                while (cursor.moveToNext())
                {
                    afterData.append(cursor.getInt(cursor.getColumnIndexOrThrow("after_num")));
                    afterData.append("//");
                    treeHotData.add(cursor.getInt(cursor.getColumnIndexOrThrow("after_num")));
                }
                addManualData();
                //Toast.makeText(getActivity(),"entering...",Toast.LENGTH_LONG).show();
                cursor = null;
            } else {
               addManualData();
            }
       }

       // recyclerView.setAdapter(new MyAdapter(treeHotData)); .........................................................................................................................
            recyclerView.setAdapter(new FR_Adapter());
       // Toast.makeText(getActivity(),sb.toString(),Toast.LENGTH_LONG).show();
        ManualDBHelper.getManualDBInstance(getActivity()).closeConnection();
        prediction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putInt("dbData",0);
                PredictionNumberFragment pnf=new PredictionNumberFragment();
                pnf.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative,pnf).commit();
            }
        });
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putInt("dbData",2);
                PredictionNumberFragment pnf=new PredictionNumberFragment();
                pnf.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative,pnf).commit();
                //Toast.makeText(getActivity(),"operation not seted",Toast.LENGTH_LONG).show();

            }
        });



        return v;


    }
  private  void addManualData()
    {
        Cursor res=getManualData();
        if(res!=null && res.getCount()>0)
        {
            text.setText(value);
            while (res.moveToNext())
            {
                afterData.append(res.getString(1));
                afterData.append("//");
                mapHotData.put(Integer.parseInt(res.getString(1)),Float.parseFloat(res.getString(2)));
                //Integer.parseInt(res.getString(1))

            }
            //Toast.makeText(getActivity(),afterData.toString(),Toast.LENGTH_LONG).show();
            for(Map.Entry<Integer,Float> entry:mapHotData.entrySet() )
            {
                int key=entry.getKey();
                float pair=entry.getValue();
                if(pair>=(float)0.8)
                {
                    treeHotData.add(key);
                    Log.d("TAG",entry.getValue().toString());
                }
                else if(pair==(float)0.7)
                {
                    avgProbability.add(key);
                }
                else lowProbability.add(key);
            }
        }
        else {
            text.setText(value + "\nNo Data Available");
            afterData.append(-1 + "//");
        }
    }

private Cursor getManualData()
{

    Cursor cursor= ManualDBHelper.getManualDBInstance(getActivity()).getParticularData(getActivity(),value);

    return  cursor;
}

    private void existInfo() {
        cf=(CommunicationFragment)getActivity();
        cf.response(0);
    }



    public class FR_Adapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        String[] dataTitle=new String[3];
        String[] colorTheme=new String[3];
        int j=0,k=0;
        FR_Adapter()
        {
            dataTitle[0]="HOTTEST NUMBERS TO BET ON";
            dataTitle[1]=" NUMBERS WITH AVERAGE PROBABILITY OF WINNING";
            dataTitle[2]="NUMBERS WITH LOW PROBABILITY OF WINNING";
            colorTheme[0]="#009933";
            colorTheme[1]="#ff8000";
            colorTheme[2]="#ff3300";

        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
             View view;
            if(viewType==0)
            {
                view=LayoutInflater.from(getActivity()).inflate(R.layout.display_number,parent,false);
                return new DataHeader(view);
            }
            else {
                view = LayoutInflater.from(getActivity()).inflate(R.layout.inner_rc_finalnumber, parent, false);
                return new InnerRC_FinalResult(view);
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(getItemViewType(position)==0)
            {
                DataHeader obj=(DataHeader)holder;
                obj.textView.setText(dataTitle[j]);
                obj.linearLayout.setBackgroundColor(Color.parseColor(colorTheme[j]));
                j++;
            }
            else {
                InnerRC_FinalResult obj=(InnerRC_FinalResult)holder;
                obj.innerRc.removeAllViews();
                obj.innerRc.setLayoutManager(new GridLayoutManager(getActivity(),10));
                if(k==0)
                    obj.adapter=new InnerRcAdapter();
                else if(k==1)
                    obj.adapter=new InnerRcAdapter(0);
                else
                    obj.adapter=new InnerRcAdapter(1);
                    obj.innerRc.setAdapter(obj.adapter);
                    k++;

            }

        }

        @Override
        public int getItemCount() {
            return 6;
        }

        @Override
        public int getItemViewType(int position) {
            if(position%2==0)
            {
                return 0;
            }
            else return 1;
        }

        public class DataHeader extends RecyclerView.ViewHolder{
            LinearLayout linearLayout;
            TextView textView;
            public DataHeader(View itemView) {
                super(itemView);
                linearLayout=itemView.findViewById(R.id.linearlayout);
                textView=itemView.findViewById(R.id.display_number_text);
            }
        }
        public class InnerRC_FinalResult extends RecyclerView.ViewHolder{
            RecyclerView innerRc;
            RecyclerView.Adapter adapter;

            public InnerRC_FinalResult(View itemView) {
                super(itemView);
                innerRc=itemView.findViewById(R.id.inner_rc);
                adapter=innerRc.getAdapter();
            }
        }
    }
public class InnerRcAdapter extends RecyclerView.Adapter<InnerRcAdapter.MyViewHolder2>
{

    int[] numberSeparate=new int[treeHotData.size()];
    String adapterFlag;

    InnerRcAdapter() {
        adapterFlag="hotdata";
       extractTreeSetData();
    }

    InnerRcAdapter(int a)
    {
        if(a==1) {
            adapterFlag = "lowdata";
            extractTreeSetData();
            int k = lowProbability.size(), count = 0;
            int value[] = new int[k];
            for (int i = 0; i < lowProbability.size(); i++) {
                value[i] = lowProbability.get(i);
            }

            for (int i = 0; i < lowProbability.size(); i++) {
                Log.d("value", Integer.toString(lowProbability.get(i)) + "index of:" + Integer.toString(lowProbability.indexOf(lowProbability.get(i))));
                for (int j = 0; j < numberSeparate.length; j++) {
                    if (lowProbability.get(i) == numberSeparate[j]) {
                        Log.d("TAG", "YES");
                        value[i] = 100;
                        // Log.d("value:",Integer.toString(value[i]));
                    } else
                        Log.d("neumberseparate:", Integer.toString(numberSeparate[j]));
                }

            }
            lowProbabilityOptimize.clear();
            for (int i = 0; i < value.length; i++) {
                Log.d("value", Integer.toString(value[i]));

                if (value[i] != 100) {
                    lowProbabilityOptimize.add(value[i]);
                }
            }
        }
        else {

            adapterFlag="averagedata";
            extractTreeSetData();
            int value[]=new int[avgProbability.size()];
            for(int i=0;i<value.length;i++)
            {
                value[i]=avgProbability.get(i);
            }
            for(int i=0;i<avgProbability.size();i++)
            {
                for(int j=0;j<numberSeparate.length;j++)
                {
                    if(avgProbability.get(i)==numberSeparate[j])
                    {
                        value[i]=100;
                    }
                }
            }
            avgProbabilityOptimize.clear();
            for(int i=0;i<value.length;i++)
            {
                if(value[i]!=100)
                {
                    avgProbabilityOptimize.add(value[i]);
                }
            }

        }


    }
    void extractTreeSetData()
    {
        Iterator<Integer> itr = treeHotData.iterator();
        int i = 0;
        //numberSeparate = new int[treeHotData.size()];
        while (itr.hasNext()) {
            numberSeparate[i] = itr.next();
            i++;
        }
    }


    @Override
    public MyViewHolder2 onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.display_number,parent,false);
        return new MyViewHolder2(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder2 holder, int position) {
        if(adapterFlag.equals("hotdata")) {
            if (numberSeparate[position] == 0) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
            } else if (colorApply(numberSeparate[position])) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            } else {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
            holder.textView.setText(Integer.toString(numberSeparate[position]));
        }
        else if(adapterFlag.equals("averagedata"))
        {
            if(avgProbabilityOptimize.get(position)==0)
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
            }
            else if(colorApply(avgProbabilityOptimize.get(position)))
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            else
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
            holder.textView.setText(Integer.toString(avgProbabilityOptimize.get(position)));
        }
        else {
            if(lowProbabilityOptimize.get(position)==0)
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
            }
            else if(colorApply(lowProbabilityOptimize.get(position)))
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            else
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
            holder.textView.setText(Integer.toString(lowProbabilityOptimize.get(position)));
        }


    }
    private boolean colorApply(int no)
    {
        int colorCheck[]={3,9,12,18,21,27,30,36,5,14,23,32,1,7,16,19,25,34};
        for(int i=0;i<colorCheck.length;i++)
        {
            if(colorCheck[i]==no)
            {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount()
    {
        if(adapterFlag.equals("hotdata"))
        {
            return  numberSeparate.length;
        }
        else if(adapterFlag.equals("averagedata"))
        {
            return avgProbabilityOptimize.size();
        }
        else return lowProbabilityOptimize.size();


    }
     class MyViewHolder2 extends RecyclerView.ViewHolder{
       public TextView textView;
        public LinearLayout linearLayout;
         public MyViewHolder2(View itemView) {
             super(itemView);
             textView=itemView.findViewById(R.id.display_number_text);
             linearLayout=itemView.findViewById(R.id.linearlayout);
         }
     }
}
}
