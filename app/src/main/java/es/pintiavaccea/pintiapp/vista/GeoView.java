package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

/**
 * Created by Miguel on 29/06/2016.
 */
public interface GeoView {

    Context getViewContext();
    Location getLastLocation();
    void openHito(Intent intent);
    void showInternetError();
    void showLocationError();
}
