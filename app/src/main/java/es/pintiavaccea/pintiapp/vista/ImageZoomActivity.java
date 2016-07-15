package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.presentador.ImageZoomPresenter;
/**
 * Created by Miguel on 20/06/2016.
 *
 * Vista que se abre cuando se pulsa en un item de la galería de imágenes. Permite hacer zoom en la
 * imagen.
 */
public class ImageZoomActivity extends AppCompatActivity implements ImageZoomView {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        ImageZoomPresenter presenter = new ImageZoomPresenter(this);
        presenter.setImagen(getIntent());
        ImageView imagenView = (ImageView) findViewById(R.id.imagen);
        presenter.loadImagen(imagenView);
    }

    @Override
    public Context getViewContext(){
        return this;
    }
}
