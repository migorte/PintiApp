package es.pintiavaccea.pintiapp.presentador;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Location;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.JsonImagenParser;
import es.pintiavaccea.pintiapp.utility.StorageManager;
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
        final Location mLastLocation = geoView.getLastLocation();
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
                            DataSource datasource = new DataSource(geoView.getViewContext());
                            Hito hito = datasource.getHitoCercano(mLastLocation.getLatitude(),
                                    mLastLocation.getLongitude());
                            if(hito != null){
                                Intent intent = new Intent(geoView.getViewContext(), DetalleHitoActivity.class);
                                intent.putExtra("hito", hito);
                                geoView.openHito(intent);
                            } else {
                                geoView.stopFabAnimation();
                                geoView.showInternetError();
                            }
                        }
                    }
            );
            VolleyRequestQueue.getInstance(geoView.getViewContext()).addToRequestQueue(jsonObjectRequest);
        } else {
            geoView.showLocationError();
            geoView.stopFabAnimation();
        }
    }

    public void precargarDatos(Activity activity){
        final ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Descargando datos...");
        final DataSource dataSource = new DataSource(geoView.getViewContext());
        List<Hito> hitos = dataSource.getAllHitos();
        for(final Hito hito : hitos){
            JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                    Request.Method.GET,
                    "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getImagenesHito/" + hito.getId(),
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
                                    Picasso.with(geoView.getViewContext()).
                                            load("http://virtual.lab.inf.uva.es:20212/pintiaserver/" +
                                                    "pintiaserver/picture/"
                                            + imagen.getId()).into(StorageManager
                                            .getTarget(imagen.getNombre(), geoView.getViewContext()));
                                }
                                progressDialog.dismiss();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.dismiss();
                        }
                    }
            );
            VolleyRequestQueue.getInstance(geoView.getViewContext()).addToRequestQueue(jsonObjectRequest);
            progressDialog.show();
        }
    }
}
