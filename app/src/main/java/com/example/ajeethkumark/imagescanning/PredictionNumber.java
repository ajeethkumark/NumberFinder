package com.example.ajeethkumark.imagescanning;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PredictionNumber extends AppCompatActivity {
    RecyclerView recyclerViewNumberList;
    int flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prediction_number);
        recyclerViewNumberList=(RecyclerView)findViewById(R.id.number_list);

        recyclerViewNumberList.setLayoutManager(new GridLayoutManager(this,5));
        MyAdapter adapter=new MyAdapter(36);
        recyclerViewNumberList.setAdapter(adapter);
        Intent i=getIntent();
        Bundle bundle=i.getExtras();
        flag=bundle.getInt("flag");
    }
    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>
    {
        int[] value=new int[37];
        int colorCheck[]={3,9,12,18,21,27,30,36,5,14,23,32,1,7,16,19,25,34};
        int colorFind;
        MyAdapter(int a)
        {
            for(int i=0;i<=a;i++)
            {
                value[i]=i;
            }
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.display_number,parent,false);
           // view.setLayoutParams(new RecyclerView.LayoutParams(60,60));
            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
          //  holder.cardView.setLayoutParams(new ViewGroup.LayoutParams(60,60));
           // viewHolder.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
          //  holder.cardView.setLayoutParams(new ViewGroup.LayoutParams(60,60));
           // holder.cardView.setBackgroundColor(Color.BLUE);
            if(value[position]==0)
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
            }
            else if(colorApply(position)) {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            }

            holder.text.setText(Integer.toString(value[position]));

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
        public int getItemCount() {
            return value.length;
        }


        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView text;
            CardView cardView;
            LinearLayout linearLayout;
            public MyViewHolder(View itemView) {
                super(itemView);
                text=itemView.findViewById(R.id.display_number_text);
                cardView=itemView.findViewById(R.id.card);
                linearLayout=itemView.findViewById(R.id.linearlayout);
               // cardView.setLayoutParams(new ViewGroup.LayoutParams(60,60));
               // itemView.setLayoutParams(new ViewGroup.LayoutParams(90,90));
                itemView.setOnClickListener(this);

            }



            @Override
            public void onClick(View view) {
               // Toast.makeText(view.getContext(),"long click",Toast.LENGTH_SHORT).show();
                /*Intent i=new Intent(PredictionNumber.this,FinalResult.class);
                i.putExtra("value",Integer.toString(getAdapterPosition()));
                startActivity(i);*/
                Intent i=new Intent(PredictionNumber.this,FinalResult.class);
                i.putExtra("value",Integer.toString(getAdapterPosition()));
                i.putExtra("flag",flag);
                startActivity(i);

            }
        }
    }
}
