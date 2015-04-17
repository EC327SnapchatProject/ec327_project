package com.example.moniljhaveri.thebackend;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;


public class MainActivity extends Activity {

    private static String logtag = "CameraApp8";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
    private static int imageGallery_load_image = 1; //this is for accessing the image gallery
    private String selectedImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraButton = (Button) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);

        Button libraryButton = (Button) findViewById(R.id.library_button);
        libraryButton.setOnClickListener(libraryListener);
    }


    private OnClickListener cameraListener = new OnClickListener() {
        public void onClick(View v) {
            takePhoto(v);
        }
    };
    private OnClickListener libraryListener = new OnClickListener() {
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setType("Image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), imageGallery_load_image);
        }
    };

    //private OnClickListener backbuttonListener = (v) ->

    private void takePhoto(View v) {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pictures.jpeg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        imageUri = Uri.fromFile(photo);
        startActivityForResult(intent, TAKE_PICTURE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                if (resultCode == Activity.RESULT_OK) {
                    if (requestCode == imageGallery_load_image) {
                        Uri selectedImageUri = data.getData();
                        selectedImagePath = getPath(selectedImageUri);
                    } else {
                        Uri selectedImage = imageUri;
                        getContentResolver().notifyChange(selectedImage, null);
                        ImageView imageView = (ImageView) findViewById(R.id.image_camera);
                        ContentResolver cr = getContentResolver();
                        Bitmap bitmap;

                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
                            imageView.setImageBitmap(bitmap);
                            Toast.makeText(MainActivity.this, selectedImage.toString(), Toast.LENGTH_LONG).show();

                        } catch (Exception e) {
                            Log.e(logtag, e.toString());
                        }
                    }


                }
        }
    }
    public String getPath(Uri uri1){ //This gets path of the string image
        String res = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri1, projection, null, null, null);
        if ( cursor.moveToFirst()){;
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
}