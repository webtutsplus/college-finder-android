package com.example.collegefinder;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class InfoActivity extends AppCompatActivity {
    private TextView titleView;
    private TextView descriptionView;
    private TextView establishedView;
    private TextView cityView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        titleView = (TextView) findViewById(R.id.titleLabel);
        descriptionView = (TextView) findViewById(R.id.descriptionLabel);
        establishedView = (TextView) findViewById(R.id.establishedLabel);
        cityView = (TextView) findViewById(R.id.cityLabel);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("desc");
        String established = getIntent().getStringExtra("established");
        String city = getIntent().getStringExtra("city");

        titleView.setText(title);
        descriptionView.setText(description);
        establishedView.setText("Established in - "+established);
        cityView.setText(city);


    }
}
