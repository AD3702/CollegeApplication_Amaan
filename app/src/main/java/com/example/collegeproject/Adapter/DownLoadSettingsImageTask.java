package com.example.collegeproject.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.collegeproject.Fragment.SettingsFragment;
import com.tuyenmonkey.mkloader.MKLoader;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

public class DownLoadSettingsImageTask extends AsyncTask<String, Void, Bitmap> {
    CircleImageView imageView;
    MKLoader mkLoader;
    ImageView imageView_settings_alpha;
    LinearLayout linearLayout;

    public DownLoadSettingsImageTask(CircleImageView imageView, MKLoader mkLoader, ImageView imageView_settings_alpha/*, LinearLayout linearLayout*/) {
        this.imageView = imageView;
        this.mkLoader = mkLoader;
        this.imageView_settings_alpha = imageView_settings_alpha;
        /*this.linearLayout = linearLayout;*/
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
        imageView.setImageBitmap(result);
        mkLoader.setVisibility(View.INVISIBLE);
        imageView_settings_alpha.setVisibility(View.INVISIBLE);
        SettingsFragment.settingsFragment.setclickable_settings();
    }
}

