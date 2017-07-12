package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.presentador.DetalleHitoPresenter;
import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by Miguel on 09/06/2016.
 *
 * Pantalla donde se muestra toda la información acerca de un hito, esto es, título, subtítulo, texto,
 * galería de imágenes y vídeo.
 */
public class DetalleHitoActivity extends AppCompatActivity implements DetalleHitoView {

    private RecyclerView mRecyclerView;

    private DetalleHitoPresenter detalleHitoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hito);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        detalleHitoPresenter = new DetalleHitoPresenter(this);

        detalleHitoPresenter.setHito(getIntent(), savedInstanceState);

        Button loc = (Button) findViewById(R.id.push_button);
        loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detalleHitoPresenter.loadMap();
            }
        });

        detalleHitoPresenter.loadVideo();

        detalleHitoPresenter.loadTitle();

        setContent();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(DetalleHitoActivity.this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new GaleriaItemDecoration(spacingInPixels));

        detalleHitoPresenter.loadImageGallery();
    }

    @Override
    public void setTitle(String titulo){
        TextView textView = (TextView) findViewById(R.id.hito_titulo);
        textView.setText(titulo);
    }

    @Override
    public Context getViewContext(){
        return this;
    }

    @Override
    public void navigateToMap(ArrayList<Hito> hitos, Hito hito){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putParcelableArrayListExtra("hitos", hitos);
        intent.putExtra("hito", hito);
        this.startActivity(intent);
    }

    @Override
    public void showError(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void prepareVideoView(Uri vidUri){
        final VideoView vidView = (VideoView) findViewById(R.id.video_view);
        assert vidView != null;
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.requestFocus();
    }

    @Override
    public void hideVideoLayout(){
        VideoView layout = (VideoView) findViewById(R.id.video_view);
        assert layout != null;
        layout.setVisibility(View.GONE);
    }

    @Override
    public void showImageGallery(List<Imagen> imagenes){
        GaleriaAdapter mAdapter = new GaleriaAdapter(imagenes);
        mRecyclerView.setAdapter(mAdapter);
        TextView cantidadImagenes = (TextView) findViewById(R.id.cantidad_imagenes);
        assert cantidadImagenes != null;
        cantidadImagenes.setText(String.format("%d imágenes", imagenes.size()));
    }

    /**
     * Pasa al presentador los campos del subtitulo, el texto y la portada para que se les
     * asigne los valores.
     */
    private void setContent() {
        TextView subtitulo = (TextView) findViewById(R.id.subtitulo);
        detalleHitoPresenter.loadSubtitulo(subtitulo);
        TextView texto = (TextView) findViewById(R.id.texto);
        detalleHitoPresenter.loadTexto(texto);
        ImageView portada = (ImageView) findViewById(R.id.imagen_toolbar);
        detalleHitoPresenter.loadPortada(portada);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        detalleHitoPresenter.onSaveInstanceState(outState);
    }
}
