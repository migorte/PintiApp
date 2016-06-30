package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import es.pintiavaccea.pintiapp.modelo.Hito;

/**
 * Created by Miguel on 30/06/2016.
 */
public interface DetalleHitoView {

    Context getViewContext();

    void navigateToMap(ArrayList<Hito> hitos);

    void showError(String msg);

    void prepareVideoView(Uri vidUri);

    void hideVideoLayout();
}
