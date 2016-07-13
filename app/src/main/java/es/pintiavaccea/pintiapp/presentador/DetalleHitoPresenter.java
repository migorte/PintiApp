package es.pintiavaccea.pintiapp.presentador;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.modelo.Video;
import es.pintiavaccea.pintiapp.utility.JsonHitoParser;
import es.pintiavaccea.pintiapp.utility.JsonImagenParser;
import es.pintiavaccea.pintiapp.utility.JsonVideoParser;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.DetalleHitoView;

/**
 * Created by Miguel on 30/06/2016.
 */
public class DetalleHitoPresenter {

    private DetalleHitoView detalleHitoView;
    private Hito hito;

    public DetalleHitoPresenter(DetalleHitoView detalleHitoView){
        this.detalleHitoView = detalleHitoView;

    }

    public void setHito(Intent intent){
        Bundle extras = intent.getExtras();
        this.hito = extras.getParcelable("hito");
    }

    public void loadMap(){
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
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
                                detalleHitoView.navigateToMap((ArrayList<Hito>) hitos);
                            } else {
//                                        if (!dataSource.getAllHitos().isEmpty()) {
//                                            Toast.makeText(DetalleHitoActivity.this, "No hay hitos disponibles",
//                                                    Toast.LENGTH_LONG).show();
//                                            List<Hito> hitosDB = dataSource.getAllHitos();
//                                            Collections.sort(hitosDB, new Comparator<Hito>() {
//                                                @Override
//                                                public int compare(Hito lhs, Hito rhs) {
//                                                    return lhs.getNumeroHito() - rhs.getNumeroHito();
//                                                }
//                                            });
//                                        } else {
//                                            Toast.makeText(DetalleHitoActivity.this, "No hay hitos disponibles",
//                                                    Toast.LENGTH_LONG).show();
//                                        }
                            }
                        } catch (IOException | JSONException e) {
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

    public void loadVideo(){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getVideoHito/"+ hito.getId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonVideoParser parser = new JsonVideoParser();
                        try {
                            Video video = parser.leerVideo(response);


                            String vidAddress = "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/video/"+video.getId();
                            Uri vidUri = Uri.parse(vidAddress);
                            detalleHitoView.prepareVideoView(vidUri);

                        } catch (IOException | JSONException e) {
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

    public void loadImageGallery(){
        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getImagenesHito/" + hito.getId(),
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JsonImagenParser parser = new JsonImagenParser();
                        try {
                            List<Imagen> imagenes = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject object = response.getJSONObject(i);
                                imagenes.add(parser.leerImagen(object));
                            }
                            if (!imagenes.isEmpty()) {
                                detalleHitoView.showImageGallery(imagenes);
                            }
                        } catch (IOException | JSONException e) {
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

    public void loadTitle(){
        ((Activity) detalleHitoView.getViewContext()).setTitle(hito.getNumeroHito() + ". " + hito.getTitulo());
    }

    public void loadSubtitulo(TextView subtitulo){
        subtitulo.setText(hito.getSubtitulo());
    }

    public void loadTexto(TextView texto){
        texto.setText(hito.getTexto());
    }

    public void loadPortada(ImageView portada){
        Picasso.with(detalleHitoView.getViewContext()).setIndicatorsEnabled(true);
        Picasso.with(detalleHitoView.getViewContext()).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                + hito.getIdImagenPortada()).error(R.drawable.img201205191603108139).into(portada);
    }
}
