package es.pintiavaccea.pintiapp.presentador;

import android.content.Intent;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.DetalleHitoActivity;
import es.pintiavaccea.pintiapp.vista.GeoView;

/**
 * Created by Miguel on 29/06/2016.
 *
 * Presentador del fragmento GeoFragment.
 */
public class GeoPresenter {
    private GeoView geoView;

    public GeoPresenter(GeoView geoView) {
        this.geoView = geoView;
    }

    /**
     * Obtiene el hito más cercano a la posición del usuario y se le pasa al intent que comenzará
     * la actividad DetalleHitoActivity
     */
    public void getCloserHito() {
        Location mLastLocation = geoView.getLastLocation();
        if (mLastLocation != null) {
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getHitoCercano?latitud=" + mLastLocation.getLatitude()
                            + "&longitud=" + mLastLocation.getLongitude(),
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JsonHitoParser parser = new JsonHitoParser();
                            try {
                                Hito hito = parser.leerHito(response);
                                Intent intent = new Intent(geoView.getViewContext(), DetalleHitoActivity.class);
                                intent.putExtra("hito", hito);
                                geoView.openHito(intent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            geoView.stopFabAnimation();
                            geoView.showInternetError();
                        }
                    }
            );
            VolleyRequestQueue.getInstance(geoView.getViewContext()).addToRequestQueue(jsonObjectRequest);
        } else {
            geoView.showLocationError();
            geoView.stopFabAnimation();
        }
    }
}
