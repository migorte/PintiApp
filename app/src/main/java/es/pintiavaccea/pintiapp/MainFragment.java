package es.pintiavaccea.pintiapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by Miguel on 09/06/2016.
 */
public class MainFragment extends Fragment implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, ResultCallback<LocationSettingsResult> {

    private static final int REQUEST_CODE_RECOVER_PLAY_SERVICES = 200;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private LocationRequest mLocationRequest;
    protected LocationSettingsRequest mLocationSettingsRequest;
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup myFragmentView = (ViewGroup) inflater.inflate(
                R.layout.fragment_main, container, false);

        fab = (FloatingActionButton) myFragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLastLocation != null) {

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                            Request.Method.GET,
                            "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getHitoCercano?latitud=" + mLastLocation.getLatitude()
                                    + "&longitud=" + mLastLocation.getLongitude(),
                            null,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    JsonHitoParser parser = new JsonHitoParser();
                                    try {
                                        Hito hito = parser.leerHito(response);
                                        final Intent intent;
                                        intent = new Intent(getContext(), DetalleHitoActivity.class);
                                        intent.putExtra("hito", hito);
                                        startActivity(intent);
                                    } catch (IOException | JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Snackbar.make(myFragmentView, "Latitud: " + mLastLocation.getLatitude() + "\t Longitud: " + mLastLocation.getLongitude(), Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                }
                            }
                    );
                    VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(jsonObjectRequest);

                } else {
                    Toast.makeText(myFragmentView.getContext(), "No se pudo obtener su localización",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageView imagen = (ImageView) myFragmentView.findViewById(R.id.foto_portada);
        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso.with(getActivity()).load("http://i.imgur.com/DvpvklR.png").fit().centerInside().error(R.drawable.img201205191602434594).into(imagen);


        return myFragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        initializeLocation();

//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            requestGPSPermission();
//
//        } else {
//            if (!isLocationEnabled()) {
//                checkLocationSettings();
//            } else {
//                initializeLocation();
//            }
//        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
//        if (mLastLocation != null) {
//            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
//        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {

    }


    /**
     * Construye un GoogleApiClient. Utiliza addApi() para solicitar la LocationServices API.
     */
    private synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Devuelve una instancia de GoogleApiClient
     *
     * @return una instancia de GoogleApiClient
     */
    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    /**
     * Devuelve la localizacion del dispositivo
     *
     * @return la localizacion del dispositivo
     */
    public Location getmLastLocation() {
        return mLastLocation;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getmGoogleApiClient() != null) {
            getmGoogleApiClient().connect();
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (getmGoogleApiClient() != null) {
            getmGoogleApiClient().disconnect();
        }
    }

    /**
     * Suspende las actualizaciones de la ubicacion
     */
    protected void stopLocationUpdates() {
        if (mGoogleApiClient != null) {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        }
    }

    public void initializeLocation() {
//        if (checkGooglePlayServices()) {
        buildGoogleApiClient();

        //prepare connection request
//            createLocationRequest();
//            buildLocationSettingsRequest();
//            checkLocationSettings();
//        }
    }
//
//    public boolean checkGooglePlayServices() {
//        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
//        int checkGooglePlayServices = googleAPI.isGooglePlayServicesAvailable(getActivity());
//        if (checkGooglePlayServices != ConnectionResult.SUCCESS) {
//              /*
//               * google play services is missing or update is required
//               *  return code could be
//               * SUCCESS,
//               * SERVICE_MISSING, SERVICE_VERSION_UPDATE_REQUIRED,
//               * SERVICE_DISABLED, SERVICE_INVALID.
//               */
//            if (googleAPI.isUserResolvableError(checkGooglePlayServices)) {
//                googleAPI.getErrorDialog(getActivity(), checkGooglePlayServices,
//                        REQUEST_CODE_RECOVER_PLAY_SERVICES).show();
//            }
//            return false;
//        }
//        return true;
//    }
//
//    protected void buildLocationSettingsRequest() {
//        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
//        builder.addLocationRequest(mLocationRequest);
//        mLocationSettingsRequest = builder.build();
//    }
//
//
//    /**
//     * Comprueba si existe conexion a internet
//     *
//     * @param context contexto de la actividad
//     * @return true si existe conexion a internet
//     */
//    public static boolean checkNetwork(Context context) {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//    }
//
//
//    /**
//     * Actualiza la ubicacion en el caso de que se produzcan cambios en la misma
//     */
//    protected void startLocationUpdates() {
//        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // Permisos de localizacion no concedidos
//            requestGPSPermission();
//
//        } else {
//            LocationServices.FusedLocationApi.requestLocationUpdates(
//                    mGoogleApiClient, mLocationRequest, this);
//        }
//    }
//
//    /**
//     * Crea una peticion de actualizacion de la ubicacion con un intervalo de tiempo de especifico
//     * en ms
//     */
//    protected void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(20000);
//        mLocationRequest.setFastestInterval(5000);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//    }
//
//
//    /**
//     * Solicita los permisos de localizacion
//     */
//    public void requestGPSPermission() {
//
//        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                Manifest.permission.ACCESS_FINE_LOCATION)) {
//            Snackbar.make(mLayout, R.string.permission_gps_rationale,
//                    Snackbar.LENGTH_INDEFINITE)
//                    .setAction(R.string.ok, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            ActivityCompat.requestPermissions(MainActivity.this,
//                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                                    REQUEST_GPS);
//                        }
//                    })
//                    .show();
//        } else {
//
//            // Los permisos de localizacion no se han concedido
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    REQUEST_GPS);
//        }
//    }
//
//
//    protected void checkLocationSettings() {
//        PendingResult<LocationSettingsResult> result =
//                LocationServices.SettingsApi.checkLocationSettings(
//                        mGoogleApiClient,
//                        mLocationSettingsRequest
//                );
//
//        result.setResultCallback(this);
//    }
//
//    public boolean isLocationEnabled() {
//        LocationManager locationManager = null;
//        boolean gps_enabled = false, network_enabled = false;
//
//        if (locationManager == null)
//            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        try {
//            gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        } catch (Exception ex) {
//            //do nothing...
//        }
//
//        try {
//            network_enabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//        } catch (Exception ex) {
//            //do nothing...
//        }
//
//        return gps_enabled || network_enabled;
//
//    }
//
//
//
//    @Override
//    public void onConnected(@Nullable Bundle bundle) {
//        startLocationUpdates();
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        Log.i("TAG", "Connection suspended");
//    }
//
//    @Override
//    public void onLocationChanged(Location location) {
//        mLastLocation = location;
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//        Log.i("TAG", "Connection failed: ConnectionResult.getErrorCode() = "
//                + connectionResult.getErrorCode());
//    }
//
//    @Override
//    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
//        final Status status = locationSettingsResult.getStatus();
//        switch (status.getStatusCode()) {
//            case LocationSettingsStatusCodes.SUCCESS:
//
//                // No muestra el dialogo
//                break;
//
//            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
//                //  La localizacion no esta activada. Se muestra el dialogo
//
//                try {
//                    // Show the dialog by calling startResolutionForResult(), and check the result
//                    // in onActivityResult().
//
//                    status.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
//
//                } catch (IntentSender.SendIntentException e) {
//
//                    //unable to execute request
//                }
//                break;
//
//            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
//                // Location settings are inadequate, and cannot be fixed here. Dialog not created
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//        switch (requestCode) {
//
//            case REQUEST_CHECK_SETTINGS:
//                switch (resultCode) {
//                    case RESULT_OK:
//                        startLocationUpdates();
//                        break;
//                    case RESULT_CANCELED:
//                        break;
//                }
//                break;
//
//            case REQUEST_CODE_RECOVER_PLAY_SERVICES:
//
//                switch (resultCode) {
//                    case RESULT_OK:
//                        // Make sure the app is not already connected or attempting to connect
//                        if (!getmGoogleApiClient().isConnecting() &&
//                                !getmGoogleApiClient().isConnected()) {
//                            getmGoogleApiClient().connect();
//                        }
//                        break;
//                    case RESULT_CANCELED:
//                        Toast.makeText(this, R.string.location_google_apis,
//                                Toast.LENGTH_SHORT).show();
//                        finish();
//                        break;
//                }
//                break;
//        }
//    }
//
//
}
