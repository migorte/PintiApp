package es.pintiavaccea.pintiapp.utility;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Miguel on 07/07/2016.
 */
public class StorageManager {

    //target to save
    public static Target getTarget(final String url, final Context context){
        Target target = new Target(){

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
                        File directory = cw.getDir("images", Context.MODE_PRIVATE);
                        File file = new File(directory + "/" + url);
//                        File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + url);
//                        String path = Environment.getExternalStorageDirectory().getPath() + "/" + url;
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.flush();
                            ostream.close();
                        } catch (IOException e) {
                            Log.e("IOException", e.getLocalizedMessage());
                        }
                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        };
        return target;
    }

    public static void saveImage(String url, final Context context) {
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
                                    .into(getTarget(imagen.getNombre(), context));
                            DataSource dataSource = new DataSource(context);
                            dataSource.saveImagen(imagen);
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

    public static Bitmap loadImageFromStorage(String path, Context context) throws FileNotFoundException {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        File directory = cw.getDir("images", Context.MODE_PRIVATE);
        File f = new File(directory + "/" + path);
        return BitmapFactory.decodeStream(new FileInputStream(f));
    }

}
