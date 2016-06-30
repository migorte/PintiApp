package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by Miguel on 30/06/2016.
 */
public interface ListaHitosView {

    void setmAdapter(RecyclerView.Adapter adapter);
    Context getViewContext();
    void showError(String msg);
}
