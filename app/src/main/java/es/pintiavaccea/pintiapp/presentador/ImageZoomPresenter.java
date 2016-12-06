package es.pintiavaccea.pintiapp.presentador;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.vista.ImageZoomView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Miguel on 13/07/2016.
 * <p/>
 * Presentador de la actividad ImageZoomActivity.
 */
@SuppressWarnings("CanBeFinal")
public class ImageZoomPresenter {

    private ImageZoomView view;
    private Imagen imagen;
    private static String URL = "http://per.infor.uva.es:8080/pintiaserver/pintiaserver";

    public ImageZoomPresenter(ImageZoomView view) {
        this.view = view;
    }

    /**
     * Configura la imagen que se mostrará en la vista
     *
     * @param intent el intent con el que se llega a la vista
     */
    public void setImagen(Intent intent) {
        Bundle extras = intent.getExtras();
        this.imagen = extras.getParcelable("imagen");
    }

    /**
     * Carga la imagen en la vista.
     *
     * @param imageView la ImageView donde se cargará la imagen
     */
    public void loadImagen(final ImageView imageView) {
        final Context context = view.getViewContext();
        assert imagen != null;

        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        Picasso.with(context)
                .load(URL + "/picture/" + imagen.getId())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        attacher.update();
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(context)
                                .load(URL + "/picture/" + imagen.getId())
                                .error(R.drawable.logo_cevfw_opt)
                                .into(imageView, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                        attacher.update();
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });
    }
}
