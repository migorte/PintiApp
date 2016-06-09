package es.pintiavaccea.pintiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ListaHitosActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_hitos);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        DataSource dataSource = new DataSource(this);
//        List<Hito> myDataset = dataSource.getAllHitos();

        // specify an adapter (see also next example)
//        mAdapter = new ListaHitosAdapter(myDataset);
//        mRecyclerView.setAdapter(mAdapter);

        /*
        Comprobar la disponibilidad de la Red
         */
        try {
            ConnectivityManager connMgr = (ConnectivityManager)
                    getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new JsonHitoTask().
                        execute(
                                new URL("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getHitosItinerario"));
            } else {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG).show();
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Created by Miguel on 07/06/2016.
     */
    public class JsonHitoTask extends AsyncTask<URL, Void, List<Hito>> {

        private ProgressDialog spinner;

        public JsonHitoTask() {
            Context context = ListaHitosActivity.this;
            spinner = new ProgressDialog(context);
        }

        @Override
        protected void onPreExecute() {
            // show progress spinner
            spinner.setMessage("Descargando lista de hitos");
            spinner.show();

            //do something
        }

        @Override
        protected List<Hito> doInBackground(URL... params) {
            List<Hito> hitos = null;
            HttpURLConnection con = null;

            try {

                //Establecer conexión con el servidor
                con = (HttpURLConnection) params[0].openConnection();
                con.setConnectTimeout(15000);
                con.setReadTimeout(10000);

                //Obtener el estado del recurso
                int statusCode = con.getResponseCode();

                if (statusCode != 200) {
                    hitos = new ArrayList<>();
                    hitos.add(new Hito(0, 0, "Error", null, 0.0, 0.0, false, null));
                } else {

                    //Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonHitoParser parser = new JsonHitoParser();
//                    GsonHitoParser<Hito> parser = new GsonHitoParser<>(Hito.class);
                    hitos = parser.readJsonStream(in);
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
            return hitos;
        }

        @Override
        protected void onPostExecute(List<Hito> hitos) {
            //Asignar los objetos de Json parseados al adaptador
            if (hitos != null) {
                Collections.sort(hitos, new Comparator<Hito>() {
                    @Override
                    public int compare(Hito lhs, Hito rhs) {
                        return lhs.getNumeroHito() - rhs.getNumeroHito();
                    }
                });
                mAdapter = new ListaHitosAdapter(hitos);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                Toast.makeText(getBaseContext(), "Ha ocurrido un error con el servidor",
                        Toast.LENGTH_LONG).show();
            }
            spinner.dismiss();
        }
    }
}
