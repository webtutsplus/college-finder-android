package com.example.collegefinder;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public final class QueryUtils {

    private static final String defaultCollegeImageUrl = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcS2Uzd92OqsPDOWV22tAUZYSkLAqlMqxh7yloM9x1IHF9yazxCEr0bh35-3YSbSKrWENRvJ2ErOBTnAd_H4bpYW9e51g3GDAp02Aw&usqp=CAU&ec=45750089";
    private QueryUtils(){
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Problem", "Problem building the URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String response = "";

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                response = readFromStream(inputStream);
            } else {
                Log.e("Problem", "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e("Problem", "Problem retrieving the college JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return response;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static List<College> extractFeatureFromJson(String collegeJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(collegeJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding earthquakes to
        List<College> colleges = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response string
            JSONObject baseJsonResponse = new JSONObject(collegeJSON);

            // Extract the JSONArray associated with the key called "features",
            // which represents a list of features (or earthquakes).
            JSONArray collegeArray = baseJsonResponse.getJSONArray("colleges");

            // For each earthquake in the earthquakeArray, create an {@link Earthquake} object
            for (int i = 0; i < collegeArray.length(); i++) {

                // Get a single earthquake at position i within the list of earthquakes
                JSONObject currentCollege = collegeArray.getJSONObject(i);

                // Extract the value for the key called "title"
                String title = currentCollege.getString("title");

                // Extract the value for the key called "city"
                String city = currentCollege.getString("city");

                // Extract the value for the key called "latitude"
                String latitude = currentCollege.getString("latitude");

                // Extract the value for the key called "longitude"
                String longitude = currentCollege.getString("longitude");

                // Extract the value for the key called "description"
                String description = currentCollege.getString("description");

                // Extract the value for the key called "established"
                String established = currentCollege.getString("established_in");

                // Extract the value for the key called "college_image_url"

                String college_image_url = currentCollege.isNull("college_image_url")?defaultCollegeImageUrl:currentCollege.getString("college_image_url");

                // Create a new {@link College} object with the city, latitude, longitude
                // from the JSON response.
                College college = new College(title, city, latitude, longitude, description, established,college_image_url);

                // Add the new {@link College} to the list of earthquakes.
                colleges.add(college);
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return colleges;
    }



    public static List<College> fetchTutorialData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Problem", "Problem making the HTTP request.", e);
        }
        List<College> colleges = extractFeatureFromJson(jsonResponse);

        // Return the list of {@link Earthquake}s
        return colleges;
    }
}
