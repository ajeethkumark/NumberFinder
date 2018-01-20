package com.example.ajeethkumark.imagescanning;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FinalResult extends AppCompatActivity {
    TextView outDataHeader,inDataHeader,outDataText,inDataText,showNumber,number;
    StringBuilder afterData=new StringBuilder();
    StringBuilder beforeData=new StringBuilder();
    RecyclerView recyclerAfterData,recyclerBeforData;
    MyNumberAdapter adapter;
    String tableName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_result);
        outDataHeader=(TextView)findViewById(R.id.out_data_header);
        inDataHeader=(TextView)findViewById(R.id.in_data_header);
        outDataText=(TextView)findViewById(R.id.out_data);
        inDataText=(TextView)findViewById(R.id.in_data);
        showNumber=(TextView)findViewById(R.id.show_number);
        number=(TextView)findViewById(R.id.number);
        recyclerAfterData=(RecyclerView)findViewById(R.id.recycle_afternumber);
        recyclerBeforData=(RecyclerView)findViewById(R.id.recycle_beforenumber);
        recyclerAfterData.setLayoutManager(new LinearLayoutManager(this));
        recyclerBeforData.setLayoutManager(new LinearLayoutManager(this));
        Intent i=getIntent();
        Bundle extras = i.getExtras();
        String tmp = extras.getString("value");
        int flag=extras.getInt("flag");
        showNumber.setText(tmp);
        ActionBar ab=getSupportActionBar();
        if(ab!=null)
        {
            ab.setDisplayHomeAsUpEnabled(true);
        }
        if(flag==1){
            tableName="ImageData";
        }
        else {
            tableName="ManualData";
        }
        Cursor cursor=DataBaseHelper.getDataBaseHelperInstance(FinalResult.this).getParticular_db_Data(tmp,FinalResult.this,tableName);
        if(cursor!=null && cursor.getCount()>0)
        {
           // Toast.makeText(FinalResult.this,"cursor not null",Toast.LENGTH_SHORT).show();
            while (cursor.moveToNext())
            {
                afterData.append(cursor.getString(cursor.getColumnIndexOrThrow("after_num")));
                afterData.append("//");
                if(flag==1) {
                    beforeData.append(cursor.getString(cursor.getColumnIndexOrThrow("before_num")));
                    beforeData.append("//");
                }
            }
            cursor=null;
        }
        else
        {
            //Toast.makeText(FinalResult.this,"cursor is null",Toast.LENGTH_SHORT).show();
        }
        //PermanentData
        /*cursor=DataBaseHelper.getDataBaseHelperInstance(FinalResult.this).getParticular_db_Data(tmp,FinalResult.this,"PermanentData");
        if(cursor!=null && cursor.getCount()>0)
        {
            while(cursor.moveToNext())
            {
                if(Integer.parseInt(tmp)==cursor.getInt(cursor.getColumnIndexOrThrow("number")))
                {
                    afterData.append(cursor.getInt(cursor.getColumnIndexOrThrow("after_num")));
                    afterData.append("//");
                }
            }
        }*/
        if(afterData!=null && afterData.length()>0)
        {
            //Toast.makeText(FinalResult.this,"after data not null",Toast.LENGTH_SHORT).show();
            //outDataText.setText(afterData.toString());
            outDataText.setVisibility(View.GONE);
            adapter=new MyNumberAdapter(afterData);
            recyclerAfterData.setAdapter(adapter);
        }
        else {
            outDataText.setVisibility(View.VISIBLE);
            outDataText.setText("No Data Available");
        }
        if(beforeData!=null && beforeData.length()>0)
        {
            //inDataText.setText(beforeData.toString());
            inDataText.setVisibility(View.GONE);
            adapter=new MyNumberAdapter(beforeData);
            recyclerBeforData.setAdapter(adapter);

        }
        else {
            inDataText.setVisibility(View.VISIBLE);
            inDataText.setText("No Data Available");
        }
        DataBaseHelper.getDataBaseHelperInstance(FinalResult.this).closeConnection();

    }

    @Override
    public boolean onSupportNavigateUp() {
        this.finish();
        return true;
    }

    public class MyNumberAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        int colorCheck[]={3,9,12,18,21,27,30,36,5,14,23,32,1,7,16,19,25,34};
        int colorFind;
        String separate[];
        int numberSeparate[],temp;
        MyNumberAdapter(StringBuilder ssb)
        {

            separate=ssb.toString().split("//");
            numberSeparate=new int[separate.length];
            for(int i=0;i<numberSeparate.length;i++)
            {
                numberSeparate[i]=Integer.parseInt(separate[i]);
                Log.i("check....",Integer.toString(numberSeparate[i]));
            }
            for(int i=0;i<numberSeparate.length;i++)
            {
                for(int j=i+1;j<numberSeparate.length;j++) {
                    if(numberSeparate[i]>numberSeparate[j]) {
                        temp = numberSeparate[i];
                        numberSeparate[i] = numberSeparate[j];
                        numberSeparate[j] = temp;
                    }
                }
                Log.i("TAG.....",Integer.toString(numberSeparate[i]));
            }
            Log.d("check......",numberSeparate.toString());
            //Toast.makeText(FinalResult.this,numberSeparate.toString(),Toast.LENGTH_SHORT).show();
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==1)
            {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view,null,false);
                return new EmptyHolder(view);
            }
            else {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_number, null, false);
                return new MyViewHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if(numberSeparate[position]==-1)
            {
                EmptyHolder emptyHolder=(EmptyHolder)holder;
            }
            else {
                MyViewHolder myViewHolder=(MyViewHolder)holder;
                if (numberSeparate[position] == 0) {
                    //green: #006600
                    myViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
                } else if (colorApply(numberSeparate[position])) {
                    //red: #ff0000
                    myViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
                }
                else
                {
                    //black: #000000
                    myViewHolder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
                }

                myViewHolder.text.setText(Integer.toString(numberSeparate[position]));
            }
        }

        private boolean colorApply(int check) {
            int i;
            colorFind=check;
            for(i=0;i<colorCheck.length;i++)
            {
                if(colorFind==colorCheck[i])
                {
                    break;
                }
            }
            if(i==colorCheck.length)
                return false;
            else
                return true;
        }


        @Override
        public int getItemCount()
        {
            return numberSeparate.length;
        }
        public class MyViewHolder extends RecyclerView.ViewHolder{
            TextView text;
            LinearLayout linearLayout;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,90));
                text=itemView.findViewById(R.id.display_number_text);
                linearLayout=itemView.findViewById(R.id.linearlayout);

            }

        }
        public class EmptyHolder extends RecyclerView.ViewHolder{


            public EmptyHolder(View itemView) {
                super(itemView);
            }
        }

         @Override
       public int getItemViewType(int position) {
            if(numberSeparate[position]==-1)
            {
                return 1;
            }

            return 0;
        }


    }
}
