package com.webtutsplus.collegefinder;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private List<College> mCollegeList;
    private String url = "http://134.209.156.1:5000/api";
    private String spatialUrl = "http://134.209.156.1:5000/api/spatial_search?";
    private Button searchButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

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
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(20.5937, 82.9629)));
        mMap.moveCamera(CameraUpdateFactory.zoomTo(4));

        Toast.makeText(getApplicationContext(),"Zoom in on a city and tap on the FIND COLLEGES Button", Toast.LENGTH_LONG).show();

        //Attach Click Listener to search Button
        searchButton = (Button)findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Getting the latitude longitude coordinates of the center point of screen
                LatLng center = mMap.getCameraPosition().target;
                double latitude = center.latitude;
                double longitude = center.longitude;

                StringBuilder query = new StringBuilder(spatialUrl);
                query.append("latitude=");
                query.append(latitude);
                query.append("&longitude=");
                query.append(longitude);
                query.append("&radius=10");

                //Marking the center of the map and displaying the coordinates.
                //mMap.addMarker(new MarkerOptions().position(center));
                //Toast.makeText(getApplicationContext(),query.toString(),Toast.LENGTH_LONG).show();

                final CollegeAsyncTask queryTask = new CollegeAsyncTask();
                queryTask.execute(query.toString());

            }
        });

        //Fetch Colleges from API
//        final CollegeAsyncTask task = new CollegeAsyncTask();
//        task.execute(url);
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
            for  (int i = 0; i < mCollegeList.size(); i++) {
                College currentCollege = mCollegeList.get(i);
                Double latitude = Double.parseDouble(currentCollege.getmLatitude());
                Double longitude = Double.parseDouble(currentCollege.getmLongitude());
                String title = currentCollege.getmTitle();
                String city = currentCollege.getmCity();



                LatLng coordinate = new LatLng(latitude, longitude);
                MarkerOptions markerOptions = new MarkerOptions().icon(getBitmapDescriptor(getApplicationContext(), R.drawable.ic_baseline_local_library_24));
                mMap.addMarker(markerOptions.position(coordinate).title(city).snippet(title)).setTag(currentCollege);
            }

            //Center map to India
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(20.5937, 82.9629)));
            //mMap.moveCamera(CameraUpdateFactory.zoomTo(4));
            mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                @Override
                public void onInfoWindowClick(Marker marker) {
                    College college = (College)marker.getTag();
                    Intent collegeInfo = new Intent(getApplicationContext(),InfoActivity.class);
                    collegeInfo.putExtra("title",college.getmTitle());
                    collegeInfo.putExtra("desc",college.getmDescription());
                    collegeInfo.putExtra("established",college.getmEstablished());
                    collegeInfo.putExtra("city",college.getmCity());
                    collegeInfo.putExtra("imageURL",college.getmImageURL());
                    startActivity(collegeInfo);
                }
            });
        }

        private BitmapDescriptor getBitmapDescriptor(Context context, int icon) {
            Drawable vd = ContextCompat.getDrawable(context, icon);
            int w = vd.getIntrinsicWidth();
            int h = vd.getIntrinsicHeight();
            vd.setBounds(0, 0, w, h);
            Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(bitmap);
            vd.draw(c);
            return BitmapDescriptorFactory.fromBitmap(bitmap);
        }
    }
}

