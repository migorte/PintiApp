package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Miguel on 15/07/2016.
 *
 * Interfaz de la vista de la actividad principal
 */
public interface MainView {
    /**
     * Devuelve el contexto de la actividad
     * @return el contexto de la actividad
     */
    Context getViewContext();

    /**
     * Configura un dialog con el horario de visitas
     * @param visitas el horario de visitas al yacimiento
     */
    void setVisitas(String visitas);

    /**
     * Configura el adaptador de la lista de hitos
     * @param adapter el adaptador de la lista de hitos
     */
    void setmAdapter(RecyclerView.Adapter adapter);

    /**
     * Muestra un Toast con el mensaje indicado.
     * @param msg el mensaje a mostrar
     */
    void showError(String msg);

    /**
     * Devuelve la última localización conocida del usuario
     * @return la última locaclización conocida del usuario
     */
    Location getLastLocation();

    /**
     * Muestra la información del hito más cercano al usuario.
     * @param intent el intent para comenzar la siguiente actividad
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
