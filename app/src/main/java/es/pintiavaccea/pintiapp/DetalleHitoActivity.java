package es.pintiavaccea.pintiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import io.vov.vitamio.MediaPlayer;
import io.vov.vitamio.widget.MediaController;
import io.vov.vitamio.widget.VideoView;

public class DetalleHitoActivity extends AppCompatActivity {

    private Hito hito;
    private ImageView portada;
    private VideoView mVideoView;
    private GaleriaAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hito);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                hito = null;
            } else hito = extras.getParcelable("hito");
        } else {
            hito = savedInstanceState.getParcelable("hito");
        }

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

                /*
        Comprobar la disponibilidad de la Red
         */
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new JsonImagenTask().
                        execute(
                                new URL("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getImagenesHito/" + hito.getId()));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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
        Picasso.with(this).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/4")
                .error(R.drawable.img201205191603108139).into(portada);


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

    /**
     * Created by Miguel on 07/06/2016.
     */
    public class JsonImagenTask extends AsyncTask<URL, Void, List<Imagen>> {

        private ProgressDialog spinner;

        public JsonImagenTask() {
            Context context = DetalleHitoActivity.this;
//            spinner = new ProgressDialog(context);
        }

//        @Override
//        protected void onPreExecute() {
//            // show progress spinner
//            spinner.setMessage("Descargando lista de hitos");
//            spinner.show();
//
//            //do something
//        }

        @Override
        protected List<Imagen> doInBackground(URL... params) {
            List<Imagen> imagenes = null;
            HttpURLConnection con = null;

            try {

                //Establecer conexión con el servidor
                con = (HttpURLConnection) params[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                //Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if (statusCode != 200) {
                    imagenes = new ArrayList<>();
                    imagenes.add(new Imagen(0, "ERROR",
                            new Hito(0, 0, "Error", null, 0.0, 0.0, false, null)));
                } else {

                    //Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonImagenParser parser = new JsonImagenParser();
//                    GsonHitoParser<Hito> parser = new GsonHitoParser<>(Hito.class);
                    imagenes = parser.readJsonStream(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "Ha sido imposible conectarse a internet",
                                Toast.LENGTH_LONG).show();
                    }
                });
            } finally {
                con.disconnect();
            }
            return imagenes;
        }

        @Override
        protected void onPostExecute(List<Imagen> imagenes) {
            //Asignar los objetos de Json parseados al adaptador
            if (imagenes != null) {
//                Collections.sort(imagenes, new Comparator<Imagen>() {
//                    @Override
//                    public int compare(Hito lhs, Hito rhs) {
//                        return lhs.getNumeroHito() - rhs.getNumeroHito();
//                    }
//                });
                mAdapter = new GaleriaAdapter(imagenes);
                mRecyclerView.setAdapter(mAdapter);
                TextView cantidadImagenes = (TextView) findViewById(R.id.cantidad_imagenes);
                assert cantidadImagenes != null;
                cantidadImagenes.setText(String.format("%d imágenes", imagenes.size()));
            } else {
                Toast.makeText(getBaseContext(), "Ha ocurrido un error con el servidor",
                        Toast.LENGTH_LONG).show();
            }
//            spinner.dismiss();
        }
    }
}
