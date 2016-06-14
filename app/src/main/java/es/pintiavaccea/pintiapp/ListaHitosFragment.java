package es.pintiavaccea.pintiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

public class ListaHitosFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private DataSource dataSource;

    private CoordinatorLayout mLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_lista_hitos, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        mLayout = (CoordinatorLayout) getActivity().findViewById(R.id.main);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        dataSource = new DataSource(getContext());

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
                    getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()) {
                new JsonHitoTask().
                        execute(
                                new URL("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getHitosItinerario"));
            } else {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_LONG).show();
                if(!dataSource.getAllHitos().isEmpty()) {
                    List<Hito> hitosDB = dataSource.getAllHitos();
                    Collections.sort(hitosDB, new Comparator<Hito>() {
                        @Override
                        public int compare(Hito lhs, Hito rhs) {
                            return lhs.getNumeroHito() - rhs.getNumeroHito();
                        }
                    });
                    mAdapter = new ListaHitosAdapter(hitosDB, getContext());
                    mRecyclerView.setAdapter(mAdapter);
                }
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
        private Context context;
        private DataSource dataSource;

        public JsonHitoTask() {
            context = getContext();
            spinner = new ProgressDialog(context);
            dataSource = new DataSource(context);
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
                    hitos.add(new Hito(0, 0, "Error", null, 0.0, 0.0, false, null, 0));
                } else {

                    //Parsear el flujo con formato JSON
                    InputStream in = new BufferedInputStream(con.getInputStream());

                    JsonHitoParser parser = new JsonHitoParser();
//                    GsonHitoParser<Hito> parser = new GsonHitoParser<>(Hito.class);
                    hitos = parser.readJsonStream(in);
                }
            } catch (IOException e) {
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, "Ha sido imposible conectarse a internet",
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
                mAdapter = new ListaHitosAdapter(hitos, getContext());
                mRecyclerView.setAdapter(mAdapter);
                dataSource.clearHitos();
                dataSource.saveListaHitos(hitos);
            } else {
                if(!dataSource.getAllHitos().isEmpty()) {
                    Toast.makeText(context, "Ha ocurrido un error con el servidor",
                            Toast.LENGTH_LONG).show();
                    List<Hito> hitosDB = dataSource.getAllHitos();
                    Collections.sort(hitosDB, new Comparator<Hito>() {
                        @Override
                        public int compare(Hito lhs, Hito rhs) {
                            return lhs.getNumeroHito() - rhs.getNumeroHito();
                        }
                    });
                    mAdapter = new ListaHitosAdapter(hitosDB, getContext());
                    mRecyclerView.setAdapter(mAdapter);
                } else {
                    Toast.makeText(context, "Ha ocurrido un error con el servidor",
                            Toast.LENGTH_LONG).show();
                }
            }
            spinner.dismiss();
        }
    }

    /**
     * Created by Miguel on 07/06/2016.
     */
//    public class JsonImagenTask extends AsyncTask<URL, Void, List<Hito>> {
//
//        private ProgressDialog spinner;
//        private Context context;
//        private DataSource dataSource;
//
//        public JsonImagenTask() {
//            context = getContext();
//            spinner = new ProgressDialog(context);
//            dataSource = new DataSource(context);
//        }
//
//        @Override
//        protected void onPreExecute() {
//            // show progress spinner
//            spinner.setMessage("Descargando lista de hitos");
//            spinner.show();
//
//            //do something
//        }
//
//        @Override
//        protected List<Hito> doInBackground(URL... params) {
//            List<Hito> hitos = null;
//            HttpURLConnection con = null;
//
//            try {
//
//                //Establecer conexión con el servidor
//                con = (HttpURLConnection) params[0].openConnection();
//                con.setConnectTimeout(15000);
//                con.setReadTimeout(10000);
//
//                //Obtener el estado del recurso
//                int statusCode = con.getResponseCode();
//
//                if (statusCode != 200) {
//                    hitos = new ArrayList<>();
//                    hitos.add(new Hito(0, 0, "Error", null, 0.0, 0.0, false, null));
//                } else {
//
//                    //Parsear el flujo con formato JSON
//                    InputStream in = new BufferedInputStream(con.getInputStream());
//
//                    JsonHitoParser parser = new JsonHitoParser();
////                    GsonHitoParser<Hito> parser = new GsonHitoParser<>(Hito.class);
//                    hitos = parser.readJsonStream(in);
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(context, "Ha sido imposible conectarse a internet",
//                                Toast.LENGTH_LONG).show();
//                    }
//                });
//            } finally {
//                con.disconnect();
//            }
//            return hitos;
//        }
//
//        @Override
//        protected void onPostExecute(List<Hito> hitos) {
//            //Asignar los objetos de Json parseados al adaptador
//            if (hitos != null) {
//                Collections.sort(hitos, new Comparator<Hito>() {
//                    @Override
//                    public int compare(Hito lhs, Hito rhs) {
//                        return lhs.getNumeroHito() - rhs.getNumeroHito();
//                    }
//                });
//                mAdapter = new ListaHitosAdapter(hitos, getContext());
//                mRecyclerView.setAdapter(mAdapter);
//                dataSource.clearHitos();
//                dataSource.saveListaHitos(hitos);
//            } else {
//                if(!dataSource.getAllHitos().isEmpty()) {
//                    Toast.makeText(context, "Ha ocurrido un error con el servidor",
//                            Toast.LENGTH_LONG).show();
//                    List<Hito> hitosDB = dataSource.getAllHitos();
//                    Collections.sort(hitosDB, new Comparator<Hito>() {
//                        @Override
//                        public int compare(Hito lhs, Hito rhs) {
//                            return lhs.getNumeroHito() - rhs.getNumeroHito();
//                        }
//                    });
//                    mAdapter = new ListaHitosAdapter(hitosDB, getContext());
//                    mRecyclerView.setAdapter(mAdapter);
//                } else {
//                    Toast.makeText(context, "Ha ocurrido un error con el servidor",
//                            Toast.LENGTH_LONG).show();
//                }
//            }
//            spinner.dismiss();
//        }
//    }
}
