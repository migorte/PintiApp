package es.pintiavaccea.pintiapp;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Miguel on 14/06/2016.
 */
public class VolleyRequestQueue {

    // Atributos
    private static VolleyRequestQueue singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyRequestQueue(Context context) {
        VolleyRequestQueue.context = context;
        requestQueue = getRequestQueue();
    }

    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleyRequestQueue(context);
        }
        return singleton;
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}
