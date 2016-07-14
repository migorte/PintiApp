package es.pintiavaccea.pintiapp.vista;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;

/**
 * Muestra el recorrido del yacimiento con todos sus hitos.
 */
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private List<Hito> hitos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            hitos = new ArrayList<>();
        } else hitos = extras.getParcelableArrayList("hitos");
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
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);

        PolylineOptions rectOptions = new PolylineOptions().width(25).color(ContextCompat.getColor(this, R.color.colorAccent));

        for(Hito hito : hitos){
            LatLng position = new LatLng(hito.getLatitud(), hito.getLongitud());
            googleMap.addMarker(new MarkerOptions().position(position).title("Marca en " + hito.getTitulo()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
            rectOptions.add(position);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(position));
        }

        googleMap.addPolyline(rectOptions);

        // Add a marker in Sydney and move the camera


    }
}
