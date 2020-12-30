package com.webtutsplus.collegefinder;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import java.io.InputStream;
import java.net.URL;

public class InfoActivity extends AppCompatActivity {
    private TextView titleView;
    private TextView descriptionView;
    private TextView establishedView;
    private TextView cityView;
    private ImageView imageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        titleView = (TextView) findViewById(R.id.titleLabel);
        descriptionView = (TextView) findViewById(R.id.descriptionLabel);
        establishedView = (TextView) findViewById(R.id.establishedLabel);
        cityView = (TextView) findViewById(R.id.cityLabel);
        imageView = (ImageView) findViewById(R.id.imageLabel);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("desc");
        String established = getIntent().getStringExtra("established");
        String city = getIntent().getStringExtra("city");
        String imageURL = getIntent().getStringExtra("imageURL");

        //Getting college image from URL
        DownloadImageTask downloadImageTask = new DownloadImageTask(imageView);
        downloadImageTask.execute(imageURL);

        titleView.setText(title);
        descriptionView.setText(description);
        establishedView.setText("Established in - "+established);
        cityView.setText(city);



    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
