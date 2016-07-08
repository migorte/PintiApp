package es.pintiavaccea.pintiapp.utility;

import android.content.Context;

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
import java.util.List;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Miguel on 07/07/2016.
 */
public class ImageLoader {
    public static void loadImage(String url, final Context context){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JsonImagenParser parser = new JsonImagenParser();
                        try {
                            Imagen imagen = parser.leerImagen(response);
                            Picasso.with(context)
                            .load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/" + imagen.getId())
                                    .into(StorageWriter.getTarget(imagen.getNombre(), context));
                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        VolleyRequestQueue.getInstance(context).addToRequestQueue(jsonObjectRequest);
    }
}
