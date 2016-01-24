package xyz.brozzz.rapidpark;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
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
        map.setInfoWindowAdapter(new MapInfoWindowAdapter());

       // iitm.showInfoWindow();
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
    class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MapInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.map_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return myContentsView;

        }

        @Override
        public View getInfoContents(Marker marker) {

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.tittle));
            tvTitle.setText(marker.getTitle());
            TextView tvSnippet = ((TextView)myContentsView.findViewById(R.id.charge));

            return myContentsView;
        }
    }
}
