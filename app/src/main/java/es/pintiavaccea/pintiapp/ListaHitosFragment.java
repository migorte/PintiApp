package es.pintiavaccea.pintiapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getHitosItinerario",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JsonHitoParser parser = new JsonHitoParser();
                        try {
                            List<Hito> hitos = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                hitos.add(parser.leerHito(object));
                            }
                            if (!hitos.isEmpty()) {
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
                                if (!dataSource.getAllHitos().isEmpty()) {
                                    Toast.makeText(getContext(), "No hay hitos disponibles",
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
                                    Toast.makeText(getContext(), "No hay hitos disponibles",
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        } catch (JSONException | IOException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Ha ocurrido un error con el servidor",
                                Toast.LENGTH_LONG).show();
                    }
                }
        );
        VolleyRequestQueue.getInstance(getContext()).addToRequestQueue(jsonArrayRequest);

    }
}
