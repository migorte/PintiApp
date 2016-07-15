package es.pintiavaccea.pintiapp.vista;

import android.content.Context;

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
}
