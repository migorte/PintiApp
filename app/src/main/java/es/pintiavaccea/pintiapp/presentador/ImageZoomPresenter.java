package es.pintiavaccea.pintiapp.presentador;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.StorageManager;
import es.pintiavaccea.pintiapp.vista.ImageZoomView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by Miguel on 13/07/2016.
 */
public class ImageZoomPresenter {

    private ImageZoomView view;
    private Imagen imagen;

    public ImageZoomPresenter(ImageZoomView view){
        this.view = view;
    }

    public void setImagen(Intent intent) {
        Bundle extras = intent.getExtras();
        this.imagen = extras.getParcelable("imagen");
    }

    public void loadImagen(ImageView imageView){
        Context context = view.getViewContext();
        DataSource dataSource = new DataSource(context);
        assert imagen != null;
        Imagen imagendb = dataSource.getImagen(imagen.getId());
        Bitmap imagenError = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.img201205191603108139);

        if (imagendb != null) {
            try {
                imagenError = StorageManager.loadImageFromStorage(imagendb.getNombre(), context);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Drawable error = new BitmapDrawable(context.getResources(), imagenError);

        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                + imagen.getId()).error(error).into(imageView, new Callback() {
            @Override
            public void onSuccess() {
                attacher.update();
            }

            @Override
            public void onError() {

            }
        });
    }
}
