package xyz.brozzz.rapidpark.Fragments;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import java.util.HashMap;

import in.aqel.quickparksdk.Objects.Parking;
import xyz.brozzz.rapidpark.Activity.BookingActivity;
import xyz.brozzz.rapidpark.R;


/**
 * Created by arun on 14-Jul-15.
 */

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener
        ,GoogleMap.OnMapClickListener {
    MapView mapView;
    GoogleMap map;
    HashMap parkings = new HashMap();
    RelativeLayout hiden;
    Gson gson;
    TextView BookingCharge;
    Parking activeParking;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_maps, container, false);
        hiden=(RelativeLayout) v.findViewById(R.id.hiden);
        BookingCharge=(TextView) v.findViewById(R.id.bookingcharge);
        AppCompatButton bookButton= (AppCompatButton) v.findViewById(R.id.bookbutton);
        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);
        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.setPadding(0, 70, 0, 0);
        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        MapsInitializer.initialize(this.getActivity());
        map.setMyLocationEnabled(true);
        map.setInfoWindowAdapter(new MapInfoWindowAdapter());
        map.setOnMarkerClickListener(this);
        map.setOnMapClickListener(this);
        // iitm.showInfoWindow();

        Location locationCt;
        LocationManager locationManagerCt = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        /*if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            locationCt = locationManagerCt.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            LatLng latLng = new LatLng(locationCt.getLatitude(),
                    locationCt.getLongitude());
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
            map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);
        }*/

        //map.getUiSettings().setMyLocationButtonEnabled(false);

        View locationButton = ((View) mapView.findViewById(1).getParent()).findViewById(2);
        // and next place it, for exemple, on bottom right (as Google Maps app)
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        rlp.setMargins(0, 0, 30, 30);
        //  new GetSuggestion().execute("crc");




         Parking[] parking = {new Parking("phoenix mall","asjhkfkkkj1",12.98927,80.2191565,200,100,150,50
                ,10,50,true,true,true),
                new Parking("oppsit phoenix mall","asjhkfkkkj",12.989637, 80.217870,100,98,40,35
                        ,5,15,true,true,true)};
         gson = new Gson();


        for(Parking par:parking){
            parkings.put(par.getId(), gson.toJson(par));
            Marker mark = map.addMarker(new MarkerOptions()
                    .position(new LatLng(par.getLat(), par.getLon()))
                    .snippet( gson.toJson(par))
                    .title(par.getName()));
           if(!par.isOpen()){
               mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
           }else if(par.getCurrentCars()==par.getTotalCars()&&par.getCurrentBikes()==par.getTotalBikes()){
               mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
           }else{
               mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
           }
        }



        map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(parking[0].getLat(), parking[0].getLon()), 14));
        map.animateCamera(CameraUpdateFactory.zoomTo(14), 2000, null);

        final LatLngBounds[] currentCameraBounds = new LatLngBounds[1];
        currentCameraBounds[0]=new LatLngBounds(new LatLng(0,0),new LatLng(0,0));

       map.setOnCameraChangeListener( new GoogleMap.OnCameraChangeListener() {
           private int CAMERA_MOVE_REACT_THRESHOLD_MS = 500;
           private long lastCallMs = Long.MIN_VALUE;

           @Override
           public void onCameraChange(CameraPosition cameraPosition) {
               LatLngBounds bounds = map.getProjection().getVisibleRegion().latLngBounds;
               // Check whether the camera changes report the same boundaries (?!), yes, it happens
               if (currentCameraBounds[0].northeast.latitude == bounds.northeast.latitude
                       && currentCameraBounds[0].northeast.longitude == bounds.northeast.longitude
                       && currentCameraBounds[0].southwest.latitude == bounds.southwest.latitude
                       && currentCameraBounds[0].southwest.longitude == bounds.southwest.longitude) {
                   return;
               }

               final long snap = System.currentTimeMillis();
               if (lastCallMs + CAMERA_MOVE_REACT_THRESHOLD_MS > snap) {
                   lastCallMs = snap;
                   return;
               }

               FetchData(bounds);

               lastCallMs = snap;
               currentCameraBounds[0] = bounds;
               Log.d("lat lan" ,(new LatLng(currentCameraBounds[0].northeast.latitude,currentCameraBounds[0].northeast.longitude)).toString());
           }
       });
        bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(getActivity(),BookingActivity.class);
                intent.putExtra("parking",gson.toJson(activeParking));
                startActivity(intent);
            }
        });

        return v;
    }

    private void FetchData(LatLngBounds bounds) {
        Parking[] parking = {new Parking("phoenix mall","asjhkfkkkj1",bounds.northeast.latitude,bounds.northeast.longitude,200,200,200,200
                ,10,50,true,true,true),
                new Parking("oppsit phoenix mall","asjhkfkkkj",bounds.southwest.latitude,bounds.southwest.longitude,100,98,40,35
                        ,5,15,true,false,false)};
        for(Parking par:parking){
            parkings.put(par.getId(), gson.toJson(par));
            Marker mark = map.addMarker(new MarkerOptions()
                    .position(new LatLng(par.getLat(), par.getLon()))
                    .snippet( gson.toJson(par))
                    .title(par.getName()));
            if(!par.isOpen()){
                mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }else if(par.getCurrentCars()==par.getTotalCars()&&par.getCurrentBikes()==par.getTotalBikes()){
                mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            }else{
                mark.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            }
        }
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

    @Override
    public boolean onMarkerClick(Marker marker) {
       Parking parking = gson.fromJson(marker.getSnippet(),Parking.class)  ;
        activeParking=parking;
        if(parking.isBooking()){
            slideUpDown(hiden);
            BookingCharge.setText("Bookinng charge ₹"+parking.getBookingCharge());

        }
        Log.d("clicked",marker.getSnippet());
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        HidePanel(hiden);
    }

    class MapInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View myContentsView;

        MapInfoWindowAdapter(){
            myContentsView = getActivity().getLayoutInflater().inflate(R.layout.map_info_contents, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            Log.d(" here i am",marker.getSnippet());
            Parking parking = gson.fromJson(marker.getSnippet(),Parking.class)  ;

            TextView tvTitle = ((TextView)myContentsView.findViewById(R.id.tittle));
            TextView charge = ((TextView)myContentsView.findViewById(R.id.charge));
            TextView car = ((TextView)myContentsView.findViewById(R.id.carcount));
            TextView bike = ((TextView)myContentsView.findViewById(R.id.bike_ount));

            charge.setText("₹"+parking.getParkingCharge()+" per hour");
            car.setText(parking.getCurrentCars()+"/"+parking.getTotalCars());
            bike.setText(parking.getCurrentBikes()+"/"+parking.getTotalBikes());
            tvTitle.setText(marker.getTitle());

            return myContentsView;

        }

        @Override
        public View getInfoContents(Marker marker) {
           return null;
        }
    }
    private void setUpMapIfNeeded(){

    }
    private void centerMapOnMyLocation() {

    }
    // Initialize map options. For example:
    // mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    public void slideUpDown(final View hiddenPanel) {
        if (!isPanelShown(hiddenPanel)) {

            // Show the panel
            Animation bottomUp = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.bottom_up);

            hiddenPanel.startAnimation(bottomUp);
            hiddenPanel.setVisibility(View.VISIBLE);
        }/*
        else {
            // Hide the Panel
            Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_out_to_bottom);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }*/
    }
    public void HidePanel(final View hiddenPanel) {
        if (isPanelShown(hiddenPanel)) {
            Animation bottomDown = AnimationUtils.loadAnimation(getActivity(),
                    R.anim.push_out_to_bottom);

            hiddenPanel.startAnimation(bottomDown);
            hiddenPanel.setVisibility(View.GONE);
        }
    }

    private boolean isPanelShown(final View hiddenPanel) {
        return hiddenPanel.getVisibility() == View.VISIBLE;
    }

}
