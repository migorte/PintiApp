package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Miguel on 30/06/2016.
 *
 * Vista donde se muestra la información y contenido multimedia de un hito.
 */
public interface DetalleHitoView {

    /**
     * Configura el título del hito.
     */
    void setTitle(String titulo);

    /**
     * Devuelve el contexto de la actividad.
     * @return el contexto de la actividad
     */
    Context getViewContext();

    /**
     * Comienza la actividad MapsActivity, donde se muestra el recorrido del yacimiento mostrando
     * los hitos. Se le pasa el ArrayList de los hitos del yacimiento.
     * @param hitos los hitos del yacimiento
     */
    void navigateToMap(ArrayList<Hito> hitos, Hito hito);

    /**
     * Muestra un Toast con el mensaje indicado.
     * @param msg el mensaje a mostrar.
     */
    void showError(String msg);

    /**
     * Muestra en el VideoView de la vista el video correspondiente a la URI proporcionada.
     * @param vidUri la URI del vídeo
     */
    void prepareVideoView(Uri vidUri);

    /**
     * Si el hito no tiene un vídeo, se esconde el layout que contiene el VideoView.
     */
    void hideVideoLayout();

    /**
     * Pasa al adaptador de la galería las imágenes que tiene que mostrar.
     * @param imagenes las imágenes que se van a mostrar en la galería
     */
    void showImageGallery(List<Imagen> imagenes);
}
