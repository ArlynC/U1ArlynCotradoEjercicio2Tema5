package com.example.u1arlyncotradoejercicio2tema5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapClickListener,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
        GoogleMap mapa;
        List<LatLng> latLngList=new ArrayList<>();
        List<Marker> markerList=new ArrayList<>();
        int id = 0;
        TileOverlay mOverlay;
        private Location location;
        private GoogleApiClient googleApiClient;
        private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
        private LocationRequest locationRequest;
        private static final long UPDATE_INTERVAL = 5000, FASTEST_INTERVAL = 5000; // = 5 seconds
        TextView txtdistancia;
        Marker punto2;
@Override
protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD|
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_main);
// Obtenemos el mapa de forma asíncrona (notificará cuando esté listo)
        SupportMapFragment mapFragment=(SupportMapFragment)
        getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);
        txtdistancia=findViewById(R.id.distancia);
        //startForegroundService(new Intent(MainActivity.this, Servicio.class));

        googleApiClient = new GoogleApiClient.Builder(this).
                addApi(LocationServices.API).
                addConnectionCallbacks(this).
                addOnConnectionFailedListener(this).build();

        }
        @Override
        protected void onStart() {
                super.onStart();
                if (googleApiClient != null) {
                        googleApiClient.connect();
                }
        }

        @Override
        protected void onResume() {
                super.onResume();
                if (!checkPlayServices()) {
                        txtdistancia.setText("You need to install Google Play Services to use the App properly");
                }
        }
        private boolean checkPlayServices() {
                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
                if (resultCode != ConnectionResult.SUCCESS) {
                        if (apiAvailability.isUserResolvableError(resultCode)) {
                                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                        } else {
                                finish();
                        }
                        return false;
                }
                return true;
        }
        @Override
        protected void onPause() {
                super.onPause();
                if (googleApiClient != null && googleApiClient.isConnected()) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
                        googleApiClient.disconnect();
                }
        }
        @Override
        public void onConnected(@Nullable Bundle bundle) {
                location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location != null) {
                        //txtdistancia.setText("Latitude : " + location.getLatitude() + "\nLongitude : " + location.getLongitude());
                       punto2=mapa.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())));

                }
               // startLocationUpdates();
        }
        @Override
        public void onConnectionSuspended(int i) {

        }

        @Override
        public void onMapReady(GoogleMap googleMap){
        mapa=googleMap;
        mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(-18.007633,-70.239271),14));

                if (ContextCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) ==
                        PackageManager.PERMISSION_GRANTED) {
                        mapa.setMyLocationEnabled(true);
                        mapa.getUiSettings().setZoomControlsEnabled(false);
                        mapa.getUiSettings().setCompassEnabled(true);
                }
                mapa.setOnMapClickListener(this);
        mapa.setOnCameraIdleListener(new GoogleMap.OnCameraIdleListener() {
                @Override
                public void onCameraIdle() {
                        CameraPosition position = mapa.getCameraPosition();
                        float zoom = position.zoom;
                        for(Marker marker:markerList)
                                marker.setVisible(zoom>=12);

                }
        });
        mapa.setOnInfoWindowLongClickListener(new GoogleMap.OnInfoWindowLongClickListener() {
                @Override
                public void onInfoWindowLongClick(Marker marker) {
                        for (Marker mimarker : markerList)
                                if (mimarker.equals(marker)) {//si el marcador es identico
                                        markerList.remove(mimarker);//elimina de la lista
                                        mOverlay.remove();
                                        break;
                                }
                        marker.remove();//elimina del mapa

                }
        });


        }
        @Override
        public void onMapClick(LatLng latLng) {
                Marker Mymarker = mapa.addMarker(new MarkerOptions()
                        .position(latLng)
                        .title("Punto "+String.valueOf(id))
                );
                markerList.add(Mymarker);
                txtdistancia.setText("El Punto "+id+" a "+Util.formatDistanceBetween(Mymarker.getPosition(),punto2.getPosition()));
                if(!markerList.isEmpty()) {
                        HeatmapTileProvider mProvider;
                        ArrayList<LatLng> lista = new ArrayList<>();
                        for (Marker marker : markerList)
                                lista.add(marker.getPosition());
                        mProvider = new HeatmapTileProvider.Builder()
                                .data(lista)
                                .build();
                        mOverlay = mapa.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                }
                id++;


        }

@Override
public void onInfoWindowClick(Marker marker){

        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        public void onLocationChanged(Location location) {

        }
}
