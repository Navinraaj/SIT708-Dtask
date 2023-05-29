package com.example.trucksharing.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trucksharing.R;
import com.example.trucksharing.data.DataParser;
import com.example.trucksharing.data.DirectionsJSONParser;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsFragment extends Fragment {

    GoogleMap mMap;
    MarkerOptions origin;
    MarkerOptions destination;
    Polyline mPolyline;
    ArrayList<LatLng> mMarkerPoints;

    static final String PICK_UP_PLACE_NAME = "PICK_UP_PLACE_NAME";
    static final String PICK_UP_PLACE_LAT = "PICK_UP_PLACE_LAT";
    static final String PICK_UP_PLACE_LNG = "PICK_UP_PLACE_LNG";

    static final String DROP_OFF_PLACE_NAME = "DROP_OFF_PLACE_NAME";
    static final String DROP_OFF_PLACE_LAT = "DROP_OFF_PLACE_LAT";
    static final String DROP_OFF_PLACE_LNG = "DROP_OFF_PLACE_LNG";

    // TODO: Rename and change types of parameters
    private String pickUpPlaceName;
    private double pickUpPlaceLat;
    private double pickUpPlaceLng;

    private String dropOffPlaceName;
    private double dropOffPlaceLat;
    private double dropOffPlaceLng;

    public static MapsFragment newInstance(String pickUpPlace, double pickUpLat, double pickUpLng, String dropOffPlace, double dropOffLat, double dropOffLng) {
        MapsFragment fragment = new MapsFragment();
        Bundle args = new Bundle();

        args.putString(PICK_UP_PLACE_NAME, pickUpPlace);
        args.putDouble(PICK_UP_PLACE_LAT, pickUpLat);
        args.putDouble(PICK_UP_PLACE_LNG, pickUpLng);

        args.putString(DROP_OFF_PLACE_NAME, dropOffPlace);
        args.putDouble(DROP_OFF_PLACE_LAT, dropOffLat);
        args.putDouble(DROP_OFF_PLACE_LNG, dropOffLng);
        fragment.setArguments(args);
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            ArrayList<MarkerOptions> markerPoints= new ArrayList();

            origin = new MarkerOptions().position(new LatLng(pickUpPlaceLat, pickUpPlaceLng)).title(pickUpPlaceName).snippet("PickUp").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            destination = new MarkerOptions().position(new LatLng(dropOffPlaceLat, dropOffPlaceLng)).title(dropOffPlaceName).snippet("Drop-off").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            markerPoints.add(origin);
            markerPoints.add(destination);

            mMap.addMarker(origin);
            mMap.addMarker(destination);
//            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin.getPosition()));

            LatLngBounds.Builder b = new LatLngBounds.Builder();
            b.include(origin.getPosition());
            b.include(destination.getPosition());

            int width = getResources().getDisplayMetrics().widthPixels;
            int height = getResources().getDisplayMetrics().heightPixels - 320;
            int padding = (int) (Math.min(width, height)*0.33);

            LatLngBounds bounds = b.build();
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width,height,padding);
            mMap.animateCamera(cu);

            drawRoute();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            pickUpPlaceName = getArguments().getString(PICK_UP_PLACE_NAME);
            pickUpPlaceLat = getArguments().getDouble(PICK_UP_PLACE_LAT);
            pickUpPlaceLng = getArguments().getDouble(PICK_UP_PLACE_LNG);

            dropOffPlaceName = getArguments().getString(DROP_OFF_PLACE_NAME);
            dropOffPlaceLat = getArguments().getDouble(DROP_OFF_PLACE_LAT);
            dropOffPlaceLng = getArguments().getDouble(DROP_OFF_PLACE_LNG);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    void drawRoute() {

        // Getting URL to the Google Directions API
        String url = getDirectionsUrl(origin.getPosition(), destination.getPosition());

        DownloadTask downloadTask = new DownloadTask();

        // Start downloading json data from Google Directions API
        downloadTask.execute(url);
    }


    String getDirectionsUrl(LatLng origin,LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Key
        String key = "key=" + getString(R.string.GoogleMap_API_KEY);

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;

        return url;
    }

    /** A method to download json data from url */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception on download", e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    /** A class to download data from Google Directions URL */
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("DownloadTask","DownloadTask : " + data);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /** A class to parse the Google Directions in JSON format */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for(int i=0;i<result.size();i++){
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for(int j=0;j<path.size();j++){
                    HashMap<String,String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(8);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                if(mPolyline != null){
                    mPolyline.remove();
                }
                mPolyline = mMap.addPolyline(lineOptions);

            }else
                Toast.makeText(getContext(),"No route is found", Toast.LENGTH_LONG).show();
        }
    }
}
