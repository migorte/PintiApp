package es.pintiavaccea.pintiapp.presentador;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.JsonImagenParser;
import es.pintiavaccea.pintiapp.utility.PreloadNotification;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.DetalleHitoActivity;
import es.pintiavaccea.pintiapp.vista.ListaHitosAdapter;
import es.pintiavaccea.pintiapp.vista.MainView;

/**
 * Created by Miguel on 15/07/2016.
 * <p>
 * Presentador de la vista principal. Principalmente envía los datos de las visitas.
 */
@SuppressWarnings("CanBeFinal")
public class MainPresenter {
    private MainView mainView;
    public static String URL = "http://per.infor.uva.es:8080/pintiaserver/pintiaserver";
    private int requestPending;

    public MainPresenter(MainView mainView) {
        this.mainView = mainView;
    }

    private void setRequestPending(int value) {
        requestPending = value;
    }

    private synchronized void decRequestPending() {
        requestPending--;
    }

    private synchronized boolean isRequestPendingEmpty() {
        return requestPending == 0;
    }

    /**
     * Descarga el horario de las visitas y se lo pasa a la actividad principal.
     */
    public void getVisitas() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + "/getVisita",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String visitas = response.getString("visitas");
                            mainView.setVisitas(visitas);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mainView.setVisitas("Error en la conexión");
                    }
                }
        );
        VolleyRequestQueue.getInstance(mainView.getViewContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Carga la lista de hitos completa y se la pasa al adaptador de la lista de hitos.
     */
    public void getListaHitos() {
        final DataSource dataSource = new DataSource(mainView.getViewContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL + "/getHitosItinerario",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        RecyclerView.Adapter mAdapter;

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
                                mAdapter = new ListaHitosAdapter(hitos, mainView.getViewContext());
                                mainView.setmAdapter(mAdapter);
                                dataSource.clearHitos();
                                dataSource.saveListaHitos(hitos);
                            } else {
                                if (!dataSource.getAllHitos().isEmpty()) {
                                    mainView.showError("No hay hitos disponibles");
                                    List<Hito> hitosDB = dataSource.getAllHitos();
                                    Collections.sort(hitosDB, new Comparator<Hito>() {
                                        @Override
                                        public int compare(Hito lhs, Hito rhs) {
                                            return lhs.getNumeroHito() - rhs.getNumeroHito();
                                        }
                                    });
                                    mAdapter = new ListaHitosAdapter(hitosDB, mainView.getViewContext());
                                    mainView.setmAdapter(mAdapter);
                                } else {
                                    mainView.showError("No hay hitos disponibles");
                                }
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mainView.showError("No se ha podido establecer conexión con el servidor, se cargarán los hitos offline");
                        List<Hito> hitosDB = dataSource.getAllHitos();
                        Collections.sort(hitosDB, new Comparator<Hito>() {
                            @Override
                            public int compare(Hito lhs, Hito rhs) {
                                return lhs.getNumeroHito() - rhs.getNumeroHito();
                            }
                        });
                        RecyclerView.Adapter mAdapter = new ListaHitosAdapter(hitosDB, mainView.getViewContext());
                        mainView.setmAdapter(mAdapter);
                    }
                }
        );
        VolleyRequestQueue.getInstance(mainView.getViewContext()).addToRequestQueue(jsonArrayRequest);
    }

    public void getCloserHito() {
        final Location mLastLocation = mainView.getLastLocation();
        if (mLastLocation != null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    URL + "/getHitoCercano?latitud=" + mLastLocation.getLatitude()
                            + "&longitud=" + mLastLocation.getLongitude(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JsonHitoParser parser = new JsonHitoParser();
                            try {
                                Hito hito = parser.leerHito(response);
                                Intent intent = new Intent(mainView.getViewContext(), DetalleHitoActivity.class);
                                intent.putExtra("hito", hito);
                                mainView.openHito(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            DataSource datasource = new DataSource(mainView.getViewContext());
                            Hito hito = datasource.getHitoCercano(mLastLocation.getLatitude(),
                                    mLastLocation.getLongitude());
                            if (hito != null) {
                                Intent intent = new Intent(mainView.getViewContext(), DetalleHitoActivity.class);
                                intent.putExtra("hito", hito);
                                mainView.openHito(intent);
                            } else {
                                mainView.stopFabAnimation();
                                mainView.showInternetError();
                            }
                        }
                    }
            );
            VolleyRequestQueue.getInstance(mainView.getViewContext()).addToRequestQueue(jsonObjectRequest);
        } else {
            mainView.showLocationError();
            mainView.stopFabAnimation();
        }
    }

    public void precargarDatos(Activity activity) {
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Descargando datos...");
        PreloadNotification.buildNotification(activity);
        PreloadNotification.startNotification();
        final DataSource dataSource = new DataSource(mainView.getViewContext());
        List<Hito> hitos = dataSource.getAllHitos();
        setRequestPending(hitos.size());
        for (final Hito hito : hitos) {
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    URL + "/getImagenesHito/" + hito.getId(),
                    null,
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JsonImagenParser parser = new JsonImagenParser();
                            try {
                                dataSource.clearImagenes(hito.getId());
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject object = response.getJSONObject(i);
                                    Imagen imagen = parser.leerImagen(object);
                                    imagen.setHito(hito.getId());
                                    dataSource.saveImagen(imagen);
                                    Picasso.with(mainView.getViewContext()).
                                            load(URL + "/picture/" + imagen.getId()).fetch();
                                }
                                decRequestPending();
                                if (isRequestPendingEmpty()){
                                    PreloadNotification.finishNotification();
                                    progressDialog.dismiss();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            decRequestPending();
                            if (isRequestPendingEmpty()){
                                PreloadNotification.finishNotification();
                                progressDialog.dismiss();
                            }
                        }
                    }
            );
            VolleyRequestQueue.getInstance(mainView.getViewContext()).addToRequestQueue(jsonObjectRequest);
        }
        progressDialog.show();
    }
}
