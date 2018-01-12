package com.example.ajeethkumark.imagescanning;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class ImageDisplay extends AppCompatActivity {

    RecyclerView showTakenCameraNumber;
    DataBaseHelper dbh;
    Bitmap bitmap=null;
    //StringBuilder stringBuilder ;
    String temp=null;
    String data[],innerSeparate[];
    int p,q;
    Button button;
    ProgressBar progressBar;
    TextView verify;
    File imagePath;
    Uri imagePathUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_display);
        button=(Button)findViewById(R.id.prediction);
        verify=(TextView)findViewById(R.id.verfy);
         showTakenCameraNumber=(RecyclerView)findViewById(R.id.taken_camera_number_show_recycler);
        progressBar=(ProgressBar)findViewById(R.id.progress);
        button.setVisibility(View.GONE);
        Intent i=getIntent();
       // ActivityCompat.requestPermissions(ImageDisplay.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},10);
        Bundle getData=getIntent().getExtras();
        if(getData.getInt("flag")==1) {
             imagePath = new File(Environment.getExternalStorageDirectory(), "MyImages/image.jpg");
            imagePathUri = Uri.fromFile(imagePath);
           // Toast.makeText(ImageDisplay.this,"verify:..."+imagePathUri,Toast.LENGTH_LONG).show();
        }
        else
        {
            // imagePath = new File(Environment.getExternalStorageDirectory(), "MyImages/image.jpg");
             imagePathUri = Uri.parse("file:///"+getData.getString("path"));
            // imagePathUri=Uri.fromFile(getData.getString("path"))
            //Toast.makeText(ImageDisplay.this,"verify:..."+getData.getString("path"),Toast.LENGTH_LONG).show();
        }
        //Toast.makeText(ImageDisplay.this,imagePathUri.toString(),Toast.LENGTH_LONG).show();
      // Glide.with(this).load(imagePathUri).into(imageView);
        // Glide.with(this).load(R.drawable.image).into(imageView);


        new AsyncTask<Void, Void, StringBuilder>() {
            StringBuilder stringBuilder=new StringBuilder();
            @Override
            protected StringBuilder doInBackground(Void... params) {
               // StringBuilder stringBuilder=new StringBuilder();
                StringBuilder showCurrentNo=new StringBuilder();
                if(Looper.myLooper()==null) {
                    Looper.prepare();
                }
                try {
                    //Toast.makeText(ImageDisplay.this,"process going on",Toast.LENGTH_LONG).show();
                    bitmap = Glide.
                            with(ImageDisplay.this).
                            load(imagePathUri).
                            asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).
                            into(-1,-1).
                            get();


                  //  Looper.myLooper().quit();
                } catch (final ExecutionException e) {
                    //Log.e(TAG, e.getMessage());
                    //Toast.makeText(ImageDisplay.this,"process going on 888",Toast.LENGTH_LONG).show();
                } catch (final InterruptedException e) {
                    //Log.e(TAG, e.getMessage());
                   // Toast.makeText(ImageDisplay.this,"process going on 9999",Toast.LENGTH_LONG).show();
                }
                catch (RuntimeException e)
                {
                   /// Toast.makeText(ImageDisplay.this,"Take photo properly",Toast.LENGTH_LONG).show();
                }
                catch (Exception e)
                {
                    //Toast.makeText(ImageDisplay.this,"Take camera",Toast.LENGTH_LONG).show();
                }

                if (null != bitmap) {
                    // The full bitmap should be available here

                    // Log.d(TAG, "Image loaded");
                    TextRecognizer textRecognizer=new TextRecognizer.Builder(getApplicationContext()).build();
                    if(textRecognizer.isOperational()) {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items = textRecognizer.detect(frame);

                        for (int i = 0; i < items.size(); i++){
                            TextBlock item = items.valueAt(i);
                            stringBuilder.append(item.getValue());
                            stringBuilder.append("\n");
                        }
                        // textView.setText(stringBuilder.toString());
                        // imagePath.delete();
                        //Toast.makeText(ImageDisplay.this,"process completed",Toast.LENGTH_LONG).show();
                        dbh=DataBaseHelper.getDataBaseHelperInstance(ImageDisplay.this);
                        showCurrentNo=dbh.insertData(ImageDisplay.this,stringBuilder);


                    }
                    else
                        Log.d("check","something wrong");
                    //Toast.makeText(ImageDisplay.this,"something wrong",Toast.LENGTH_LONG).show();
                }
               // return stringBuilder.toString();
                return showCurrentNo;
            }

            @Override
            protected void onPostExecute(StringBuilder sb) {
                try {

                    progressBar.setVisibility(View.GONE);
                    showTakenCameraNumber.setLayoutManager(new GridLayoutManager(ImageDisplay.this, 14));
                    CurrentNumberShow adpter = new CurrentNumberShow(sb);
                    showTakenCameraNumber.setAdapter(adpter);
                    // dba.getData();

                    button.setVisibility(View.VISIBLE);
                    verify.setText(stringBuilder.toString());
                }
                catch(NumberFormatException e)
                {
                    Toast.makeText(ImageDisplay.this,"Take Photo Again",Toast.LENGTH_LONG).show();

                    button.setVisibility(View.VISIBLE);
                }
                catch (Exception e)
                {
                    Toast.makeText(ImageDisplay.this,"Take photo Again",Toast.LENGTH_LONG).show();
                    button.setVisibility(View.VISIBLE);
                }



            }
        }.execute();

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent i=new Intent(ImageDisplay.this,PredictionNumber.class);
            startActivity(i);
        }
    });
    }
    public class CurrentNumberShow extends RecyclerView.Adapter<RecyclerView.ViewHolder>
    {
        StringBuilder sb;
        String[] separate;
        int colorCheck[]={3,9,12,18,21,27,30,36,5,14,23,32,1,7,16,19,25,34};
        int colorFind,verifyBind;
        CurrentNumberShow(StringBuilder sb){
            Log.i("NUMBER....",sb.toString());
            this.sb=sb;
            separate=this.sb.toString().split("-");
           /* for(int i=0;i<separate.length;i++)
            {
                Toast.makeText(ImageDisplay.this,Integer.parseInt(separate[i])+"...",Toast.LENGTH_SHORT).show();
            }*/
        }


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType==1) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.display_number, parent, false);
                verifyBind=1;
                return new MyViewHolder(view);
            }
            else {
                View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view,null,false);
                verifyBind=0;
                return new EmptyHolder(view);

            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if(verifyBind==1) {
                MyViewHolder holder1=(MyViewHolder)holder;

                if (colorApply(separate[position])) {
                    holder1.linearLayout.setBackgroundColor(Color.parseColor("#ff0000"));
                } else if (Integer.parseInt(separate[position]) == 1000) {
                    holder1.linearLayout.setBackgroundColor(Color.parseColor("#efeff5"));
                    holder1.text.setText("-");

                } else if (Integer.parseInt(separate[position]) == 0) {
                    holder1.linearLayout.setBackgroundColor(Color.parseColor("#006600"));
                }


                holder1.text.setText(separate[position]);
            }
            else {
                EmptyHolder emptyHolder=(EmptyHolder)holder;
            }

        }

        @Override
        public int getItemCount() {
            return separate.length;
        }

        @Override
        public int getItemViewType(int position) {
            try {
                if (Integer.parseInt(separate[position])==0){

                    return 1;
                }
                else return 1;
            }
            catch(Exception e)
            {
                return 0;
            }
        }

        private boolean colorApply(String check) {
            int i;

            colorFind=Integer.parseInt(check);

                for (i = 0; i < colorCheck.length; i++) {
                    if (colorFind == colorCheck[i]) {
                        break;
                    }
                }
                if (i == colorCheck.length)
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
        public class EmptyHolder extends RecyclerView.ViewHolder{

            public EmptyHolder(View itemView) {
                super(itemView);
            }
        }
    }


}
