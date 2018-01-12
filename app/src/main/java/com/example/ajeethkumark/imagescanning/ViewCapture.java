package com.example.ajeethkumark.imagescanning;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ViewCapture extends AppCompatActivity {
    RecyclerView recyclerviewNumberList;
    String[] str;
    int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_capture);
        recyclerviewNumberList=(RecyclerView)findViewById(R.id.numberlist);

        recyclerviewNumberList.setLayoutManager(new GridLayoutManager(ViewCapture.this,14));

        Cursor res=DataBaseHelper.getDataBaseHelperInstance(ViewCapture.this).getData(ViewCapture.this);
        if(res.getCount()>0) {
            str = new String[res.getCount()];
            while (res.moveToNext()) {
                str[i] = res.getString(res.getColumnIndexOrThrow("number"));
                i++;
            }
            MyAdapter adapter = new MyAdapter(str.length);
            recyclerviewNumberList.setAdapter(adapter);
            DataBaseHelper.getDataBaseHelperInstance(ViewCapture.this).closeConnection();
        }
        else{
            Toast.makeText(ViewCapture.this,"No Data Available",Toast.LENGTH_SHORT).show();
        }

    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {
        int[] value;
        int colorCheck[]={3,9,12,18,21,27,30,36,5,14,23,32,1,7,16,19,25,34};
        int colorFind;
        MyAdapter(int a)
        {
            value=new int[a];
            for(int i=0;i<a;i++)
            {
                value[i]=Integer.parseInt(str[i]);
            }
        }

        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_number,parent,false);
            return new MyAdapter.MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyAdapter.MyViewHolder holder, int position) {
            //#006600
            if(value[(value.length-1)-position]==0)
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
            }
            else if(colorApply(value[(value.length-1)-position]))
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            else
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            }

            holder.text.setText(Integer.toString(value[(value.length-1)-position]));

        }

        @Override
        public int getItemCount() {
            return value.length;
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




        public class MyViewHolder extends RecyclerView.ViewHolder {
            TextView text;
            LinearLayout linearLayout;
            public MyViewHolder(View itemView) {
                super(itemView);

                text=itemView.findViewById(R.id.display_number_text);
                linearLayout=itemView.findViewById(R.id.linearlayout);



            }




        }
    }
}
