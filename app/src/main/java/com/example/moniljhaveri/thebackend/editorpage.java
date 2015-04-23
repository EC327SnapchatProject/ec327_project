package com.example.moniljhaveri.thebackend;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


public class editorpage extends Activity {
    private static String logtag = "editor page";
    String[] filterresources;
    Spinner filterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorpage);

        setContentView(R.layout.activity_editorpage);
        ImageView editImage = (ImageView) findViewById(R.id.image);
        Uri viewUri = getIntent().getData();
        Bitmap editBitmap;
        try {
            editBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), viewUri);
            editImage.setImageBitmap(editBitmap);
            //break; //may need
        } catch (Exception e){
            Toast.makeText(editorpage.this, "failed to load", Toast.LENGTH_LONG).show();
            Log.e(logtag, e.toString());
        }
        Button backButton = (Button) findViewById(R.id.back_button); //The backbutton
        backButton.setOnClickListener(editorListener);

        filterButton = (Spinner)findViewById(R.id.Filters);
        filterresources = getResources().getStringArray(R.array.filters);
        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,filterresources);
        filterButton.setAdapter(adapter);
    }
    private View.OnClickListener editorListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case (R.id.back_button): {
                    Intent intent = new Intent(editorpage.this, MainActivity.class);
                    startActivity(intent);
                    break;
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
}
