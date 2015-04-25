package com.example.moniljhaveri.thebackend;

import android.app.Activity;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ListView;
import java.util.ArrayList;


public class editorpage extends Activity {
    private static String logtag = "editor page";
    String[] filterresources;
    Spinner filterButton;
    Bitmap BW;
    Bitmap editBitmap;
    ImageView editImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorpage);

        setContentView(R.layout.activity_editorpage);
        editImage = (ImageView) findViewById(R.id.image);
        Uri viewUri = getIntent().getData();
        try {
            editBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), viewUri);
            editImage.setImageBitmap(editBitmap);

            //break; //may need
        } catch (Exception e){
            Toast.makeText(editorpage.this, "failed to load", Toast.LENGTH_LONG).show();
            Log.e(logtag, e.toString());
        }
        Button backButton = (Button) findViewById(R.id.back_button); //The back button
        backButton.setOnClickListener(editorListener);
        Button Filter1= (Button) findViewById(R.id.filter_button);
        Filter1.setOnClickListener(editorListener);

        filterButton = (Spinner)findViewById(R.id.Filters); //Filters buttons
        filterresources = getResources().getStringArray(R.array.filters); //options of filters
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,filterresources);
        filterButton.setAdapter(adapter);
    }

    private View.OnClickListener editorListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case (R.id.back_button):
                {
                    Intent intent = new Intent(editorpage.this, MainActivity.class);
                    startActivity(intent);
                    break;
                }
               case (R.id.filter_button):
               {
                   BW = ChangeGrey(editBitmap,5,5.0,6.0,0.0);
                   editImage.setImageBitmap(BW);
               }
            }
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_editorpage, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static Bitmap ChangeGrey(Bitmap src, int depth, double red, double green, double blue) {
        int width = src.getWidth();
        int height = src.getHeight();
        Bitmap finalBitmap = Bitmap.createBitmap(width, height, src.getConfig());
        final double grayScale_Red = 0.3;
        final double grayScale_Green = 0.59;
        final double grayScale_Blue = 0.11;

        int channel_aplha, channel_red, channel_green, channel_blue;
        int pixel;
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                pixel = src.getPixel(x, y);
                channel_aplha = Color.alpha(pixel);
                channel_red = Color.red(pixel);
                channel_green = Color.green(pixel);
                channel_blue = Color.blue(pixel);
                channel_blue = channel_green = channel_red = (int)(grayScale_Red * channel_red + grayScale_Green * channel_green + grayScale_Blue * channel_blue);
                channel_red += (depth * red);

                if(channel_red > 255)
                {
                    channel_red = 255;
                }
                channel_green += (depth * green);
                if(channel_green > 255)
                {
                    channel_green = 255;
                }
                channel_blue += (depth * blue);
                if(channel_blue > 255)
                {
                    channel_blue = 255;
                }
                finalBitmap.setPixel(x, y, Color.argb(channel_aplha, channel_red, channel_green, channel_blue));
            }
        }
        return finalBitmap;
    }

}



