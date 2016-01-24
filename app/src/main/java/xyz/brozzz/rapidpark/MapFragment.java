package xyz.brozzz.rapidpark;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


/**
 * Created by arun on 14-Jul-15.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback {
    MapView mapView;
    GoogleMap map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.setPadding(0, 70, 0, 0);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        map.setMyLocationEnabled(true);

        LatLng position = new LatLng(12.9884871, 80.2355152);
        Marker iitm = map.addMarker(new MarkerOptions().position(position)
                .title("iit madras"));
        iitm.showInfoWindow();
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(position, 14));
        map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        //map.getUiSettings().setMyLocationButtonEnabled(false);


        View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);

        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
        //  new GetSuggestion().execute("crc");

        return v;
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d("hay", "map is redy");
    }

   /* private class GetSuggestion extends AsyncTask<String, String, Void> {
        InputStream inputStream = null;
        String result = "";
        String url_select;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            url_select = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + params[0] + "&types&location=12.9884871,80.2355152&radius=1&key=AIzaSyBEa_dL1oJOG1_oGdpBmsJaampKn5DtOto";
            Log.d("salfk", url_select);
            ArrayList<NameValuePair> param = new ArrayList<NameValuePair>();

            try {
                // Set up HTTP post

                // HttpClient is more then less deprecated. Need to change to URLConnection
                HttpClient httpClient = new DefaultHttpClient();

                HttpGet httpPost = new HttpGet(url_select);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();

                // Read content & Log
                inputStream = httpEntity.getContent();
            } catch (UnsupportedEncodingException e1) {
                Log.e("UnsupportedEncodingE", e1.toString());
                e1.printStackTrace();
            } catch (ClientProtocolException e2) {
                Log.e("ClientProtocolException", e2.toString());
                e2.printStackTrace();
            } catch (IllegalStateException e3) {
                Log.e("IllegalStateException", e3.toString());
                e3.printStackTrace();
            } catch (IOException e4) {
                Log.e("IOException", e4.toString());
                e4.printStackTrace();
            }
            // Convert response to string using String Builder
            try {
                BufferedReader bReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"), 8);
                StringBuilder sBuilder = new StringBuilder();

                String line = null;
                while ((line = bReader.readLine()) != null) {
                    sBuilder.append(line + "\n");
                }

                inputStream.close();
                result = sBuilder.toString();

            } catch (Exception e) {
                Log.e("StriBuilding & Buffe", "Error con " + e.toString());
            }
            return null;
        } // protected Void doInBackground(String... params)

        protected void onPostExecute(Void v) {
            int i = 0;
            //parse JSON data
            try {
                JSONObject job = new JSONObject(result);
                JSONArray jArray = job.getJSONArray("predictions");
                for (i = 0; i < jArray.length(); i++) {

                    JSONObject jObject = jArray.getJSONObject(i);

                    String name = jObject.getString("description");
                    Log.d("name", name);

                } // End Loop
            } catch (JSONException e) {
                Log.e("JSONException", "Error: " + e.toString());
            } // catch (JSONException e)
        } // protected void onPostExecute(Void v)

    }*/
}
