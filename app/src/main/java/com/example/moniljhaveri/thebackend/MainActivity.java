package com.example.moniljhaveri.thebackend;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button cameraButton = (Button) findViewById(R.id.camera_button);
        cameraButton.setOnClickListener(cameraListener);
    }

    private OnClickListener cameraListener = new OnClickListener() {
        public void onClick(View v) {
            takePhoto(v);
        }
    };

//    private void takePhoto(View v) {
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pictures.jpeg");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
//        imageUri = Uri.fromFile(photo);
//        startActivityForResult(intent, TAKE_PICTURE);
//    }

    public void takePhoto(View v) {
        // tell the phone we want to use the camera
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // create a new temp file called pic.jpg in the "pictures" storage area of the phone
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "pic.jpg");
        // take the return data and store it in the temp file "pic.jpg"
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
        // stor the temp photo uri so we can find it later
        imageUri = Uri.fromFile(photo);
        // start the camera
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