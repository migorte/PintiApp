package es.pintiavaccea.pintiapp.presentador;

import android.content.Intent;
import android.location.Location;
import android.support.design.widget.Snackbar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.GeoView;

/**
 * Created by Miguel on 29/06/2016.
 */
public class GeoPresenter {
    private GeoView geoView;

    public GeoPresenter(GeoView geoView) {
        this.geoView = geoView;
    }

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
                                geoView.openHito(hito);
                            } catch (IOException | JSONException e) {
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
