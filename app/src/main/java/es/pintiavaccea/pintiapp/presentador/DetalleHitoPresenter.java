package es.pintiavaccea.pintiapp.presentador;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.modelo.Video;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.JsonImagenParser;
import es.pintiavaccea.pintiapp.utility.JsonVideoParser;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.DetalleHitoView;

/**
 * Created by Miguel on 30/06/2016.
 *
 * Presentador de la actividad DetalleHitoActivity. Se encarga de proporcionar los modelos de datos
 * a la vista.
 */
public class DetalleHitoPresenter {

    private final DetalleHitoView detalleHitoView;
    private Hito hito;
    public static String URL = "http://per.infor.uva.es:8080/pintiaserver/pintiaserver";

    public DetalleHitoPresenter(DetalleHitoView detalleHitoView) {
        this.detalleHitoView = detalleHitoView;
    }

    /**
     * Configura el hito del que se muestran los detalles en la vista
     * @param intent el intent con el que se llega a la vista
     * @param saveInstanceState el bundle donde se guarda el hito cuando la actividad se para
     */
    public void setHito(Intent intent, Bundle saveInstanceState) {
        Bundle extras = intent.getExtras();
        this.hito = extras.getParcelable("hito");
        if(hito == null) saveInstanceState.getParcelable("hito");
    }

    /**
     * Guarda la instancia del hito
     * @param outState bundle donde se guarda la instancia
     */
    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("hito", hito);
    }

    /**
     * Carga toda la lista de hitos y se los pasa a la vista para navegar a la actividad donde se
     * muestra el recorrido en el mapa
     */
    public void loadMap() {
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL + "/getHitosItinerario",
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
                                detalleHitoView.navigateToMap((ArrayList<Hito>) hitos, hito);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        detalleHitoView.showError(error.getLocalizedMessage());
                    }
                }
        );
        VolleyRequestQueue.getInstance(detalleHitoView.getViewContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Carga la uri del vídeo del hito para pasarsela a la vista.
     */
    public void loadVideo() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + "/getVideoHito/" + hito.getId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonVideoParser parser = new JsonVideoParser();
                        try {
                            Video video = parser.leerVideo(response);

                            String vidAddress = URL + "/video/" + video.getId();
                            Uri vidUri = Uri.parse(vidAddress);
                            detalleHitoView.prepareVideoView(vidUri);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        detalleHitoView.hideVideoLayout();
//                        Snackbar.make(this, "Latitud: "  + "\t Longitud: " + mLastLocation.getLongitude(), Snackbar.LENGTH_LONG)
//                                .setAction("Action", null).show();
                    }
                }
        );
        VolleyRequestQueue.getInstance(detalleHitoView.getViewContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Carga la lista de imágenes del hito para pasárselas a la vista y las guarda en la base
     * de datos
     */
    public void loadImageGallery() {
        final DataSource dataSource = new DataSource(detalleHitoView.getViewContext());
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
                            List<Imagen> imagenes = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);

                                Imagen imagen = parser.leerImagen(object);
                                imagen.setHito(hito.getId());
                                imagenes.add(imagen);
                                dataSource.saveImagen(imagen);
                            }
                            if (!imagenes.isEmpty()) {
                                detalleHitoView.showImageGallery(imagenes);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        detalleHitoView.showError(error.getLocalizedMessage());
                        List<Imagen> imagenes = dataSource.getImagenesHito(hito.getId());
                        detalleHitoView.showImageGallery(imagenes);
                    }
                }
        );
        VolleyRequestQueue.getInstance(detalleHitoView.getViewContext()).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Pasa a la vista el título del hito
     */
    public void loadTitle() {
        detalleHitoView.setTitle(hito.getNumeroHito() + ". " + hito.getTitulo());
    }

    /**
     * Pasa a la vista el subtítulo del hito
     */
    public void loadSubtitulo(TextView subtitulo) {
        subtitulo.setText(hito.getSubtitulo());
    }

    /**
     * Pasa a la vista el texto del hito
     */
    public void loadTexto(TextView texto) {
        texto.setText(hito.getTexto());
    }

    /**
     * Carga la portada del hito en la vista.
     * @param portada la ImageView donde se cargará la imagen.
     */
    public void loadPortada(final ImageView portada) {

        Picasso.with(detalleHitoView.getViewContext())
                .load(URL + "/picture/" + hito.getIdImagenPortada())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(portada, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(detalleHitoView.getViewContext())
                                .load(URL + "/picture/" + hito.getIdImagenPortada())
                                .error(R.drawable.logo_cevfw_opt)
                                .into(portada, new Callback() {
                                    @Override
                                    public void onSuccess() {

                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso","Could not fetch image");
                                    }
                                });
                    }
                });

    }
}
