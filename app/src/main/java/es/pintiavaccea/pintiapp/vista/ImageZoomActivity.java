package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
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
import es.pintiavaccea.pintiapp.presentador.ImageZoomPresenter;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.StorageManager;
import uk.co.senab.photoview.PhotoViewAttacher;

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
