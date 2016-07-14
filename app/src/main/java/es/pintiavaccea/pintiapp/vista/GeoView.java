package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

/**
 * Created by Miguel on 29/06/2016.
 *
 * Vista que obtiene la geolocalización del usuario y le muestra el hito más cercano.
 */
public interface GeoView {
    /**
     * Devuelve el contexto de la actividad.
     * @return el contexto de la actividad
     */
    Context getViewContext();

    /**
     * Devuelve la última localización conocida del usuario
     * @return la última locaclización conocida del usuario
     */
    Location getLastLocation();

    /**
     * Muestra la información del hito más cercano al usuario.
     * @param intent
     */
    void openHito(Intent intent);

    /**
     * Para la animación del Floating Action Button.
     */
    void stopFabAnimation();

    /**
     * Muestra un mensaje de error sobre la conexión a internet.
     */
    void showInternetError();

    /**
     * Muestra un mensaje de error sobre la geolocalización.
     */
    void showLocationError();
}
