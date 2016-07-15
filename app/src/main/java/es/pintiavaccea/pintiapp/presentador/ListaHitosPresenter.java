package es.pintiavaccea.pintiapp.presentador;

import android.support.v7.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.ListaHitosAdapter;
import es.pintiavaccea.pintiapp.vista.ListaHitosView;

/**
 * Created by Miguel on 30/06/2016.
 *
 * Presentador del fragmento ListaHitosFragment.
 */
@SuppressWarnings("CanBeFinal")
public class ListaHitosPresenter {

    private ListaHitosView listaHitosView;

    public ListaHitosPresenter(ListaHitosView listaHitosView){
        this.listaHitosView = listaHitosView;
    }

    /**
     * Carga la lista de hitos completa y se la pasa al adaptador de la lista de hitos.
     */
    public void getListaHitos(){
        final DataSource dataSource = new DataSource(listaHitosView.getViewContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getHitosItinerario",
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
                                mAdapter = new ListaHitosAdapter(hitos, listaHitosView.getViewContext());
                                listaHitosView.setmAdapter(mAdapter);
                                dataSource.clearHitos();
                                dataSource.saveListaHitos(hitos);
                            } else {
                                if (!dataSource.getAllHitos().isEmpty()) {
                                    listaHitosView.showError("No hay hitos disponibles");
                                    List<Hito> hitosDB = dataSource.getAllHitos();
                                    Collections.sort(hitosDB, new Comparator<Hito>() {
                                        @Override
                                        public int compare(Hito lhs, Hito rhs) {
                                            return lhs.getNumeroHito() - rhs.getNumeroHito();
                                        }
                                    });
                                    mAdapter = new ListaHitosAdapter(hitosDB, listaHitosView.getViewContext());
                                    listaHitosView.setmAdapter(mAdapter);
                                } else {
                                    listaHitosView.showError("No hay hitos disponibles");
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
                        listaHitosView.showError("No se ha podido establecer conexión con el servidor, se cargarán los hitos offline");
                        List<Hito> hitosDB = dataSource.getAllHitos();
                        Collections.sort(hitosDB, new Comparator<Hito>() {
                            @Override
                            public int compare(Hito lhs, Hito rhs) {
                                return lhs.getNumeroHito() - rhs.getNumeroHito();
                            }
                        });
                        RecyclerView.Adapter mAdapter = new ListaHitosAdapter(hitosDB, listaHitosView.getViewContext());
                        listaHitosView.setmAdapter(mAdapter);
                    }
                }
        );
        VolleyRequestQueue.getInstance(listaHitosView.getViewContext()).addToRequestQueue(jsonArrayRequest);
    }
}
