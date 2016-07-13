package es.pintiavaccea.pintiapp.presentador;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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
import es.pintiavaccea.pintiapp.utility.StorageManager;
import es.pintiavaccea.pintiapp.utility.VolleyRequestQueue;
import es.pintiavaccea.pintiapp.vista.DetalleHitoView;

/**
 * Created by Miguel on 30/06/2016.
 */
public class DetalleHitoPresenter {

    private final DetalleHitoView detalleHitoView;
    private Hito hito;

    public DetalleHitoPresenter(DetalleHitoView detalleHitoView) {
        this.detalleHitoView = detalleHitoView;
    }

    public void setHito(Intent intent, Bundle saveInstanceState) {
        Bundle extras = intent.getExtras();
        this.hito = extras.getParcelable("hito");
        if(hito == null) saveInstanceState.getParcelable("hito");
    }

    public void onSaveInstanceState(Bundle outState){
        outState.putParcelable("hito", hito);
    }

    public void loadMap() {
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

    public void loadVideo() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getVideoHito/" + hito.getId(),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonVideoParser parser = new JsonVideoParser();
                        try {
                            Video video = parser.leerVideo(response);


                            String vidAddress = "http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/video/" + video.getId();
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

    public void loadImageGallery() {
        final DataSource dataSource = new DataSource(detalleHitoView.getViewContext());
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

    public void loadTitle() {
        ((Activity) detalleHitoView.getViewContext()).setTitle(hito.getNumeroHito() + ". " + hito.getTitulo());
    }

    public void loadSubtitulo(TextView subtitulo) {
        subtitulo.setText(hito.getSubtitulo());
    }

    public void loadTexto(TextView texto) {
        texto.setText(hito.getTexto());
    }

    public void loadPortada(ImageView portada) {
        DataSource dataSource = new DataSource(detalleHitoView.getViewContext());
        Imagen imagendb = dataSource.getImagen(hito.getIdImagenPortada());
        Bitmap bitmapPortada = BitmapFactory.decodeResource(detalleHitoView.getViewContext().getResources(),
                R.drawable.img201205191603108139);

        if (imagendb != null) {
            try {
                bitmapPortada = StorageManager.loadImageFromStorage(imagendb.getNombre(), detalleHitoView.getViewContext());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Drawable error = new BitmapDrawable(detalleHitoView.getViewContext().getResources(), bitmapPortada);
        Picasso.with(detalleHitoView.getViewContext()).setIndicatorsEnabled(true);
        Picasso.with(detalleHitoView.getViewContext()).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/thumbnail/"
                + hito.getIdImagenPortada()).error(error).into(portada);
    }
}
