package com.example.collegeproject.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.view.View;

import com.example.collegeproject.Fragment.SettingsFragment;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


public class Download_Image_On_Settings_Update extends AsyncTask<String, Void, Bitmap> {

    Context context;
    MKLoader mkLoader;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String IMAGE_UPLOAD_URI = "image_upload_URI";

    public Download_Image_On_Settings_Update(Context context, MKLoader mkLoader) {
        this.context = context;
        this.mkLoader = mkLoader;
    }

    /*
        doInBackground(Params... params)
            Override this method to perform a computation on a background thread.
     */
    protected Bitmap doInBackground(String... urls) {
        String urlOfImage = urls[0];
        Bitmap logo = null;
        try {
            InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
            logo = BitmapFactory.decodeStream(is);
        } catch (Exception e) { // Catch the download exception
            e.printStackTrace();
        }
        return logo;
    }

    /*
        onPostExecute(Result result)
            Runs on the UI thread after doInBackground(Params...).
     */
    protected void onPostExecute(Bitmap result) {
        Uri image_uri_onlogin = getImageUri(result);
        sharedPreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(IMAGE_UPLOAD_URI, String.valueOf(image_uri_onlogin));
        editor.commit();
        mkLoader.setVisibility(View.INVISIBLE);
        SettingsFragment.settingsFragment.setclickable_settings();
        SettingsFragment.settingsFragment.imageView_settings_aplha.setVisibility(View.INVISIBLE);
    }

    public Uri getImageUri(Bitmap inImage) {
        File tempDir = Environment.getExternalStorageDirectory();
        tempDir = new File(tempDir.getAbsolutePath() + "/.temp/");
        tempDir.mkdir();
        File tempFile = null;
        try {
            tempFile = File.createTempFile("title", ".jpg", tempDir);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        byte[] bitmapData = bytes.toByteArray();

        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(tempFile);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(tempFile);
    }

}

