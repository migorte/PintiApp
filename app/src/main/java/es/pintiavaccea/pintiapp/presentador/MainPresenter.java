package es.pintiavaccea.pintiapp.presentador;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.MainView;

/**
 * Created by Miguel on 15/07/2016.
 */
public class MainPresenter {
    private MainView mainView;

    public MainPresenter(MainView mainView){
        this.mainView = mainView;
    }

    /**
     * Carga la lista de hitos completa y se la pasa al adaptador de la lista de hitos.
     */
    public void getVisitas(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getVisita",
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
}
