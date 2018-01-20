package com.example.ajeethkumark.imagescanning;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;
import java.security.Permission;

import static com.example.ajeethkumark.imagescanning.DataBaseHelper.tableName;


public class MainActivity extends AppCompatActivity {
    Button scanButton,clearButton,playButton,viewCapture,uploadImage,manualData;
    int  CAMERA_PIC_REQUEST=0,SELECT_IMAGE=10;
    File imageFilePath;
    String path="hello";
    int flag=0,permissionFlag=0;
    Cursor res=null;
     final static int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE=12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        scanButton = (Button) findViewById(R.id.scan_button);
        clearButton=(Button)findViewById(R.id.clear_button);
        playButton=(Button)findViewById(R.id.prediction);
        viewCapture=(Button)findViewById(R.id.viewCapture);
        uploadImage=(Button)findViewById(R.id.upload_image);
        manualData=(Button)findViewById(R.id.manual_data);
        getSupportActionBar().setTitle("Roulette Table Scanner");
        Cursor res=DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).getData(MainActivity.this);
        if(!(res.getCount()>0))
        {
            clearButton.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
            viewCapture.setVisibility(View.GONE);
        }
        DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).closeConnection();

        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
                if(!(imagesFolder.exists())) {
                    imagesFolder.mkdirs();
                }
                imageFilePath = new File(imagesFolder, "image.jpg");
                path=imageFilePath.toString();
                Uri uriSavedImage = Uri.fromFile(imageFilePath);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
                startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);*/
             permissionFlag=0;
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    startCameraProcess();
                }
                else
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                     {

                         ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                     }
                     else
                     {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                     }
                }


            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,PredictionNumber.class);
                startActivity(i);
            }
        });
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog alertDialog=new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle("Data Refresh");
                alertDialog.setMessage("Are you sure to clear the data?");

                alertDialog.setButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).openConnection(MainActivity.this).execSQL("Delete from " + tableName);
                        clearButton.setVisibility(View.GONE);
                        playButton.setVisibility(View.GONE);
                        viewCapture.setVisibility(View.GONE);

                    }
                });
                // alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.show();
                DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).closeConnection();

            }
        });
        viewCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ViewCapture.class);
                startActivity(i);
            }
        });
        uploadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);//
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);*/
                permissionFlag=1;
                if(ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED)
                {
                    Intent i = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                    startActivityForResult(i, SELECT_IMAGE);
                }
                else
                {
                    if(ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                    {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                    }
                    else
                    {
                        ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
                    }
                }


            }
        });
        manualData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,ManualDataEntry.class);
                startActivity(i);
            }
        });
        if(!PreferenceManager.getDefaultSharedPreferences(this).getBoolean("insertData",false))
        {
            DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).insertPermanentData(MainActivity.this);
            PreferenceManager.getDefaultSharedPreferences(this).edit().putBoolean("valuesInsert",true).apply();
        }



    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
        {
            if(PackageManager.PERMISSION_GRANTED==grantResults[0] && permissionFlag!=1) {
                startCameraProcess();
            }
            else if(PackageManager.PERMISSION_GRANTED==grantResults[0] && permissionFlag!=0){
                Intent i = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                startActivityForResult(i, SELECT_IMAGE);
            }
        }
    }

    private void startCameraProcess() {
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        File imagesFolder = new File(Environment.getExternalStorageDirectory(), "MyImages");
        if(!(imagesFolder.exists())) {
            imagesFolder.mkdirs();
        }
        imageFilePath = new File(imagesFolder, "image.jpg");
        //path=imageFilePath.toString();
      // Uri uriSavedImage = Uri.fromFile(imageFilePath);
        Uri uriSavedImage= FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getApplicationContext().getPackageName() + ".provider", imageFilePath);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);
        startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);
        //Intent i=new Intent(MainActivity.this,ImageDisplay.class);
       // startActivity(i);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_PIC_REQUEST) {
            switch(resultCode)
            {
                case Activity.RESULT_OK:
                {
                    //Bitmap image = (Bitmap) data.getExtras().get("data");
                    // ImageView imageview = (ImageView) findViewById(R.id.ImageView01);
                    // imageview.setImageBitmap(image);
                    // final Bitmap bitmap= BitmapFactory.decodeResource(data.getExtras().get("data"));
                    Intent i=new Intent(MainActivity.this,ImageDisplay.class);
                  //  i.putExtra("BitmapImage",image);
                    i.putExtra("filePath",path);
                    i.putExtra("flag",1);
                    startActivityForResult(i,90);
                    break;
                }
                case Activity.RESULT_CANCELED:
                {
                    break;
                }
                default: break;
            }

        }
        else if(requestCode==SELECT_IMAGE)
        {
            switch (resultCode)
            {
                case Activity.RESULT_OK:
                {
                    if(data!=null) {
                       /* Uri path=data.getData();
                        Toast.makeText(MainActivity.this, path.toString(), Toast.LENGTH_LONG).show();
                        break;*/
                       /* Uri selectedImage = data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};

                        Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
                        cursor.moveToFirst();

                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String filePath = cursor.getString(columnIndex);
                        cursor.close();
                        Toast.makeText(MainActivity.this, filePath, Toast.LENGTH_LONG).show();

                        Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                        Toast.makeText(MainActivity.this, yourSelectedImage.toString(), Toast.LENGTH_LONG).show();*/
            /* Now you have choosen image in Bitmap format in object "yourSelectedImage". You can use it in way you want! */

                            Uri selectedImageUri = data.getData();
                            String selectedImagePath = getPath(selectedImageUri);
                            Log.i("Image Path ........: " , selectedImagePath);
                            //  Toast.makeText(MainActivity.this,selectedImagePath,Toast.LENGTH_LONG).show();
                            Intent i=new Intent(MainActivity.this,ImageDisplay.class);
                            i.putExtra("flag",2);
                            i.putExtra("path",selectedImagePath);
                            startActivity(i);



                        break;
                    }
                }
                case Activity.RESULT_CANCELED:
                {
                   // Toast.makeText(MainActivity.this, "cancel", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    @Override
    protected void onResume() {
        super.onResume();
       // Toast.makeText(MainActivity.this,"Resume called",Toast.LENGTH_SHORT).show();
        Cursor res=DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).getData(MainActivity.this);
        if(!(res.getCount()>0))
        {
            clearButton.setVisibility(View.GONE);
            playButton.setVisibility(View.GONE);
            viewCapture.setVisibility(View.GONE);
        }
        else
        {
            clearButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.VISIBLE);
            viewCapture.setVisibility(View.VISIBLE);
        }
        DataBaseHelper.getDataBaseHelperInstance(MainActivity.this).closeConnection();
    }
}
