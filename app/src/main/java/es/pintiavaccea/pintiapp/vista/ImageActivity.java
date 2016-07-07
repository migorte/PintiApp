package es.pintiavaccea.pintiapp.vista;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import uk.co.senab.photoview.PhotoViewAttacher;

public class ImageActivity extends AppCompatActivity {

    private Imagen imagen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                imagen = null;
            } else imagen = extras.getParcelable("imagen");
        } else {
            imagen = savedInstanceState.getParcelable("imagen");
        }

        ImageView imagenView = (ImageView) findViewById(R.id.imagen);
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imagenView);
        Picasso.with(this).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                + imagen.getId()).error(R.drawable.img201205191603108139).into(imagenView, new Callback() {
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
