package com.example.ajeethkumark.imagescanning;


import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class PredictionNumberFragment extends Fragment {
    RecyclerView recyclerView;
    Button resetButton;
    int dbData;

    public PredictionNumberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_prediction_number, container, false);
        recyclerView=v.findViewById(R.id.numberlist);
        resetButton=v.findViewById(R.id.resetbutton);
        Bundle b=getArguments();
        dbData=b.getInt("dbData");
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),5));
        recyclerView.setAdapter(new PredictionAdaptor());
        if(dbData==2)
        {
            resetButton.setVisibility(View.GONE);
        }
        else {
            resetButton.setVisibility(View.VISIBLE);
        }
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Are you sure want to exits");
                builder.setCancelable(true);
                builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getActivity().finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();
            }
        });
        return v;
    }

    public class PredictionAdaptor extends RecyclerView.Adapter<PredictionAdaptor.MyViewHolder>{

        int colorCheck[]={3,9,12,18,21,27,30,36,5,14,23,32,1,7,16,19,25,34};

        @Override
        public PredictionAdaptor.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.display_number,parent,false);

            return new MyViewHolder(v);
        }

        @Override
        public void onBindViewHolder(PredictionAdaptor.MyViewHolder holder, int position) {
            if(position==0)
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
            }
            else if(colorApply(position))
            {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
            }
            else {
                holder.linearLayout.setBackgroundColor(Color.parseColor("#000000"));
            }
            holder.textView.setText(Integer.toString(position));

        }
        private boolean colorApply(int no)
        {
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
        public int getItemCount() {
            return 37;
        }
        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            TextView textView;
            LinearLayout linearLayout;
            public MyViewHolder(View itemView) {
                super(itemView);
                textView=itemView.findViewById(R.id.display_number_text);
                linearLayout=itemView.findViewById(R.id.linearlayout);
                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                Bundle b=new Bundle();
                b.putString("value",Integer.toString(getAdapterPosition()));
                b.putInt("flag",1);
                b.putInt("dbData",dbData);
                FinalResultFragment frf=new FinalResultFragment();
                frf.setArguments(b);
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.relative,frf).commit();
            }
        }
    }


}
