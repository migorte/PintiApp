package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.location.Location;

import es.pintiavaccea.pintiapp.modelo.Hito;

/**
 * Created by Miguel on 29/06/2016.
 */
public interface GeoView {

    Context getViewContext();
    Location getLastLocation();
    void openHito(Hito hito);
    void showInternetError();
    void showLocationError();
}
