package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.presentador.DetalleHitoPresenter;
import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import android.widget.MediaController;
import android.widget.VideoView;

public class DetalleHitoActivity extends AppCompatActivity implements DetalleHitoView {

    private Hito hito;
    private ImageView portada;
    private GaleriaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private DetalleHitoPresenter detalleHitoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hito);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                hito = null;
            } else hito = extras.getParcelable("hito");
        } else {
            hito = savedInstanceState.getParcelable("hito");
        }

        detalleHitoPresenter = new DetalleHitoPresenter(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                detalleHitoPresenter.loadMap();
            }
        });

        detalleHitoPresenter.loadVideo(hito);

        assert hito != null;
        this.setTitle(hito.getNumeroHito() + ". " + hito.getTitulo());
        setContent();

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setAdapter(new GaleriaAdapter());

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(DetalleHitoActivity.this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        mRecyclerView.addItemDecoration(new GaleriaItemDecoration(spacingInPixels));

        detalleHitoPresenter.loadImageGallery(hito);
    }

    @Override
    public Context getViewContext(){
        return this;
    }

    @Override
    public void navigateToMap(ArrayList<Hito> hitos){
        Intent intent = new Intent(this, MapsActivity.class);
        intent.putParcelableArrayListExtra("hitos", hitos);
        this.startActivity(intent);
    }

    @Override
    public void showError(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void prepareVideoView(Uri vidUri){
        final VideoView vidView = (VideoView) findViewById(R.id.video_view);
        vidView.setVideoURI(vidUri);
        MediaController vidControl = new MediaController(this);
        vidControl.setAnchorView(vidView);
        vidView.setMediaController(vidControl);
        vidView.requestFocus();
        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vidView.start();
            }
        });
    }

    @Override
    public void hideVideoLayout(){
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.video_layout);
        layout.setVisibility(View.GONE);
    }

    @Override
    public void showImageGallery(List<Imagen> imagenes){
        mAdapter = new GaleriaAdapter(imagenes);
        mRecyclerView.setAdapter(mAdapter);
        TextView cantidadImagenes = (TextView) findViewById(R.id.cantidad_imagenes);
        assert cantidadImagenes != null;
        cantidadImagenes.setText(String.format("%d imágenes", imagenes.size()));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("hito", hito);
    }

    private void setContent() {
        TextView subtitulo = (TextView) findViewById(R.id.subtitulo);
        subtitulo.setText(hito.getSubtitulo());
        TextView texto = (TextView) findViewById(R.id.texto);
        texto.setText(hito.getTexto());
        portada = (ImageView) findViewById(R.id.imagen_toolbar);
        Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(this).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                + hito.getIdImagenPortada()).error(R.drawable.img201205191603108139).into(portada);

//        Picasso.with(this).setIndicatorsEnabled(true);
//        Picasso.with(this).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/4")
//                .error(R.drawable.img201205191603108139).into(portada);


        /********************
         * Reproductor Vitamio*
         *********************/
//        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
//        String path = "rtmp://rrbalancer.broadcast.tneg.de:1935/pw/ruk/ruk";
//        /*options = new HashMap<>();
//        options.put("rtmp_playpath", "");
//        options.put("rtmp_swfurl", "");
//        options.put("rtmp_live", "1");
//        options.put("rtmp_pageurl", "");*/
//        mVideoView.setVideoPath(path);
//        //mVideoView.setVideoURI(Uri.parse(path), options);
//        mVideoView.setMediaController(new MediaController(this));
//        mVideoView.requestFocus();
//
//        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//            @Override
//            public void onPrepared(MediaPlayer mediaPlayer) {
//                mediaPlayer.setPlaybackSpeed(1.0f);
//            }
//        });


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
}
