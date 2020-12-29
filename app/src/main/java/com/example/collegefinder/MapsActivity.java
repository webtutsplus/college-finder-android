package com.example.collegefinder;

import androidx.fragment.app.FragmentActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<College> mCollegeList;
    private String url = "http://134.209.156.1:5000/api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        Button button = new Button(this);
//        button.setText("Search this area");
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lp.gravity=Gravity.FILL_HORIZONTAL;
//        addContentView(button,lp);
//
//        button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                //Intent i=new Intent(this,SecondActivity.class);
//                //startActivity(i);
//
//            }
//        });


    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //Fetch Colleges from API
        CollegeAsyncTask task = new CollegeAsyncTask();
        task.execute(url);
    }


    //Executing network call on background task
    private class CollegeAsyncTask extends AsyncTask<String, Void, List<College>> {
        @Override
        protected List<College> doInBackground(String... url) {
                List<College> result = QueryUtils.fetchTutorialData(url[0]);
                return result;
        }


        @Override
        protected void onPostExecute(List<College> result) {
            mCollegeList = result;
            for  (int i = 0; i < 5; i++) {
                College currentCollege = mCollegeList.get(i);
                Double latitude = Double.parseDouble(currentCollege.getmLatitude());
                Double longitude = Double.parseDouble(currentCollege.getmLongitude());
                String title = currentCollege.getmTitle();
                String city = currentCollege.getmCity();

                LatLng coordinate = new LatLng(latitude, longitude);
                mMap.addMarker(new MarkerOptions().position(coordinate).title(city).snippet(title)).setTag(currentCollege);
            }
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(20.5937, 82.9629)));
            mMap.moveCamera(CameraUpdateFactory.zoomTo(4));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    College college = (College)marker.getTag();
                    Intent collegeInfo = new Intent(getApplicationContext(),InfoActivity.class);
                    collegeInfo.putExtra("title",college.getmTitle());
                    collegeInfo.putExtra("desc",college.getmDescription());
                    collegeInfo.putExtra("established",college.getmEstablished());
                    collegeInfo.putExtra("city",college.getmCity());
                    startActivity(collegeInfo);
                }
            });
        }
    }
}

