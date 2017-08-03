package id.co.dimas.android.mymaps;

import android.app.Dialog;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener {

    private GoogleMap mMap;
    private EditText etLocation;
    private Button btnGoLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()){
            Toast.makeText(this, "Good", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_maps);
            intiMap();
        }

        initView();
    }

    private void initView(){
        etLocation = (EditText) findViewById(R.id.et_position);
        btnGoLocation = (Button) findViewById(R.id.btn_go_location);

        btnGoLocation.setOnClickListener(this);
    }

    private void intiMap() {
        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.mapfragment);
        mapFragment.getMapAsync(this);
    }

    private boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailable = api.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS){
            return true;
        }else if (api.isUserResolvableError(isAvailable)){
            Dialog dialog = api.getErrorDialog(this,isAvailable,0);
            dialog.show();
        }else {
            Toast.makeText(this, "Can't connect to play services", Toast.LENGTH_SHORT).show();
        }
        return false;
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

        goToLocation(-6.319614, 106.749909,18);
        // Add a marker in Sydney and move the camera
    }

    private void goToLocation(double lat, double lng, int zoom) {
        LatLng nbs = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(nbs,zoom);
        mMap.addMarker(new MarkerOptions().position(nbs).title("Marker in NBS"));
        mMap.moveCamera(update);
    }

    private void goToLocation(double lat, double lng) {
        LatLng nbs = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(nbs);
        mMap.addMarker(new MarkerOptions().position(nbs).title("Marker in NBS"));
        mMap.moveCamera(update);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_go_location:
                String location = etLocation.getText().toString();
                Geocoder gc = new Geocoder(this);
                try {
                    List<Address> list = gc.getFromLocationName(location,1);
                    Address addres = list.get(0);
                    String locality = addres.getLocality();
                    Toast.makeText(getApplicationContext(),locality,Toast.LENGTH_SHORT).show();

                    double lat = addres.getLatitude();
                    double lng = addres.getLongitude();
                    goToLocation(lat,lng,18);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.gmap_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.mapTypeNone:
                mMap.setMapType(GoogleMap.MAP_TYPE_NONE);
                break;
            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}