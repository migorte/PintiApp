package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Miguel on 30/06/2016.
 *
 * Vista de la lista de hitos.
 */
public interface ListaHitosView {

    /**
     * Configura el adaptador de la lista de hitos
     * @param adapter el adaptador de la lista de hitos
     */
    void setmAdapter(RecyclerView.Adapter adapter);

    /**
     * Devuelve el contexto de la actividad.
     * @return el contexto de la actividad
     */
    Context getViewContext();

    /**
     * Muestra un Toast con el mensaje indicado.
     * @param msg el mensaje a mostrar
     */
    void showError(String msg);
}
