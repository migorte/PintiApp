package es.pintiavaccea.pintiapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class DetalleHitoActivity extends AppCompatActivity {

    private Hito hito;
    private ImageView portada;
    private VideoView mVideoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hito);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        if (savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                hito = null;
            } else hito = extras.getParcelable("hito");
        } else {
            hito = savedInstanceState.getParcelable("hito");
        }

        this.setTitle(hito.getTitulo());
        setContent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("hito", hito);
    }

    private void setContent(){
        TextView subtitulo = (TextView) findViewById(R.id.subtitulo);
        subtitulo.setText(hito.getSubtitulo());
        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(hito.getFecha());
        TextView texto = (TextView) findViewById(R.id.texto);
        texto.setText(hito.getTexto());
        portada = (ImageView) findViewById(R.id.imagen_toolbar);
        Picasso.with(this).setIndicatorsEnabled(true);
        Picasso.with(this).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/4")
                .error(R.drawable.img201205191603108139).into(portada);


        /********************
         * Reproductor Vitamio*
         *********************/
        mVideoView = (VideoView) findViewById(R.id.vitamio_videoView);
        String path = "rtmp://rrbalancer.broadcast.tneg.de:1935/pw/ruk/ruk";
        /*options = new HashMap<>();
        options.put("rtmp_playpath", "");
        options.put("rtmp_swfurl", "");
        options.put("rtmp_live", "1");
        options.put("rtmp_pageurl", "");*/
        mVideoView.setVideoPath(path);
        //mVideoView.setVideoURI(Uri.parse(path), options);
        mVideoView.setMediaController(new MediaController(this));
        mVideoView.requestFocus();

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setPlaybackSpeed(1.0f);
            }
        });


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
