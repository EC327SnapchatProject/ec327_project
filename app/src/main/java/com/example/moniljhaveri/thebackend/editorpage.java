package com.example.moniljhaveri.thebackend;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class editorpage extends Activity {
    private static String logtag = "editor page";
    //String[] filterresources;
    //Spinner filterButton;
    Bitmap BW;
    Bitmap editBitmap;
    ImageView editImage;
    String imagename = "Picture";
    int imageNumber = 1;
    Uri UriBW;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editorpage);
        context = getApplicationContext();
        setContentView(R.layout.activity_editorpage);
        editImage = (ImageView) findViewById(R.id.image);
        Uri viewUri = getIntent().getData();
        try {
            editBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), viewUri);
            editImage.setImageBitmap(editBitmap);

            //break; //may need
        } catch (Exception e) {
            Toast.makeText(editorpage.this, "failed to load", Toast.LENGTH_LONG).show();
            Log.e(logtag, e.toString());
        }
        Button backButton = (Button) findViewById(R.id.back_button); //The back button
        backButton.setOnClickListener(editorListener);
        Button Filter1 = (Button) findViewById(R.id.filter_button);
        Filter1.setOnClickListener(editorListener);
        Button InvertButton = (Button) findViewById(R.id.invert_filter);
        InvertButton.setOnClickListener(editorListener);
        Button Greenify = (Button) findViewById(R.id.green_filter);
        Greenify.setOnClickListener(editorListener);
        Button saveButton = (Button) findViewById(R.id.save_button);
        saveButton.setOnClickListener(editorListener);

//        filterButton = (Spinner)findViewById(R.id.Filters); //Filters buttons
//        filterresources = getResources().getStringArray(R.array.filters); //options of filters
//        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,filterresources);
//        filterButton.setAdapter(adapter);
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
                case (R.id.filter_button): {
                    BW = ChangeGrey(editBitmap, 5, 5.0, 6.0, 0.0);
                    editImage.setImageBitmap(BW);
                    break;
                }
                case (R.id.invert_filter): {
                    BW = InvertColor(editBitmap);
                    editImage.setImageBitmap(BW);
                    break;
                }
                case (R.id.green_filter): {
                    BW = Greenify(editBitmap);
                    editImage.setImageBitmap(BW);
                    break;
                }
                case (R.id.save_button):{
                    Toast.makeText(editorpage.this, "Saved pressed1", Toast.LENGTH_LONG).show();
                    try{
                        MediaStore.Images.Media.insertImage(getContentResolver(), BW,imagename, null);
                    }catch (Exception e){
                        Toast.makeText(editorpage.this, "Save failed", Toast.LENGTH_LONG).show();

                    }
                    //String P = saveBitmap(BW, imagename)




                        //if (saveBitmap(BW)){
                      //  Toast.makeText(editorpage.this, "Saved pressed", Toast.LENGTH_LONG).show();
                    //}
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

        public static Bitmap ChangeGrey(Bitmap src, int depth, double red, double green, double blue) { //Need to cite this
            int width = src.getWidth();
            int height = src.getHeight();
            Bitmap finalBitmap = Bitmap.createBitmap(width, height, src.getConfig());
            final double grayScale_Red = 0.3;
            final double grayScale_Green = 0.59;
            final double grayScale_Blue = 0.11;

            int channel_aplha, channel_red, channel_green, channel_blue;
            int pixel;
            for (int x = 0; x < width; ++x) {
                for (int y = 0; y < height; ++y) {
                    pixel = src.getPixel(x, y);
                    channel_aplha = Color.alpha(pixel);
                    channel_red = Color.red(pixel);
                    channel_green = Color.green(pixel);
                    channel_blue = Color.blue(pixel);
                    channel_blue = channel_green = channel_red = (int) (grayScale_Red * channel_red + grayScale_Green * channel_green + grayScale_Blue * channel_blue);
                    channel_red += (depth * red);

                    if (channel_red > 255) {
                        channel_red = 255;
                    }
                    channel_green += (depth * green);
                    if (channel_green > 255) {
                        channel_green = 255;
                    }
                    channel_blue += (depth * blue);
                    if (channel_blue > 255) {
                        channel_blue = 255;
                    }
                    finalBitmap.setPixel(x, y, Color.argb(channel_aplha, channel_red, channel_green, channel_blue));
                }
            }
            return finalBitmap;
        }

        public static Bitmap InvertColor(Bitmap src) { //android newbie site this, also optimize this
            // create new bitmap with the same settings as source bitmap
            Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
            // color info
            int A, R, G, B;
            int pixelColor;
            // image size
            int height = src.getHeight();
            int width = src.getWidth();

            // scan through every pixel
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // get one pixel
                    pixelColor = src.getPixel(x, y);
                    // saving alpha channel
                    A = Color.alpha(pixelColor);
                    // inverting byte for each R/G/B channel
                    R = 255 - Color.red(pixelColor);
                    G = 255 - Color.green(pixelColor);
                    B = 255 - Color.blue(pixelColor);
                    // set newly-inverted pixel to output image
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }

            // return final bitmap
            return bmOut;

        }

        public static Bitmap Greenify(Bitmap src) {
            Bitmap bmOut = Bitmap.createBitmap(src.getWidth(), src.getHeight(), src.getConfig());
            int A, R, G, B;
            int pixelColor;
            // image size
            int height = src.getHeight();
            int width = src.getWidth();

            // scan through every pixel
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // get one pixel
                    pixelColor = src.getPixel(x, y);
                    // saving alpha channel
                    A = Color.alpha(pixelColor);
                    // inverting byte for each R/G/B channel
                    R = 255 - Color.red(pixelColor);
                    G = 255;
                    B = 255 - Color.blue(pixelColor);
                    // set newly-inverted pixel to output image
                    bmOut.setPixel(x, y, Color.argb(A, R, G, B));
                }
            }

            // return final bitmap
            return bmOut;


        }
//        public Uri getImageUri(Context inContext, Bitmap inImage) {
//            imagename = imagename + String.valueOf(imageNumber)
//            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
//            inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
//            String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, imagename, null);
//            imageNumber++;
//            return Uri.parse(path);
//        }

//    protected String saveBitmap(Bitmap bm, String name) throws Exception {
//        String tempFilePath = Environment.getExternalStorageDirectory() + "/"
//                + getPackageName() + "/" + name + ".jpg";
//        File tempFile = new File(tempFilePath);
//        if (!tempFile.exists()) {
//            if (!tempFile.getParentFile().exists()) {
//                tempFile.getParentFile().mkdirs();
//            }
//        }
//
//        tempFile.delete();
//        tempFile.createNewFile();
//
//        int quality = 100;
//        FileOutputStream fileOutputStream = new FileOutputStream(tempFile);
//
//        BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);
//        bm.compress(CompressFormat.JPEG, quality, bos);
//
//        bos.flush();
//        bos.close();
//
//        bm.recycle();
//
//        return tempFilePath;
//    }


}








