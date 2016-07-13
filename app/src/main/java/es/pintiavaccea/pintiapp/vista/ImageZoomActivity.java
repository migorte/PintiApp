package es.pintiavaccea.pintiapp.vista;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.StorageManager;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageZoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        Imagen imagen;
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                imagen = null;
            } else {
                imagen = extras.getParcelable("imagen");
            }
        } else {
            imagen = savedInstanceState.getParcelable("imagen");
        }

        DataSource dataSource = new DataSource(this);
        Imagen imagendb = dataSource.getImagen(imagen.getId());
        Bitmap imagenError = BitmapFactory.decodeResource(getResources(),
                R.drawable.img201205191603108139);

        if (imagendb != null) {
            try {
                imagenError = StorageManager.loadImageFromStorage(imagendb.getNombre(), this);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Drawable error = new BitmapDrawable(getResources(), imagenError);

        ImageView imagenView = (ImageView) findViewById(R.id.imagen);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imagenView);
        Picasso.with(this).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                + imagen.getId()).error(error).into(imagenView, new Callback() {
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
