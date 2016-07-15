package es.pintiavaccea.pintiapp.utility;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Miguel on 14/06/2016.
 *
 * Cola de solicitudes del framework Volley para Android.
 */
@SuppressWarnings("WeakerAccess")
public class VolleyRequestQueue {

    // Atributos
    private static VolleyRequestQueue singleton;
    private RequestQueue requestQueue;
    private static Context context;

    private VolleyRequestQueue(Context context) {
        VolleyRequestQueue.context = context;
        requestQueue = getRequestQueue();
    }

    /**
     * Singleton de la clase
     * @param context el contexto de la actividad
     * @return la instancia única
     */
    public static synchronized VolleyRequestQueue getInstance(Context context) {
        if (singleton == null) {
            singleton = new VolleyRequestQueue(context);
        }
        return singleton;
    }

    /**
     * Devuelve la cola de solicitudes.
     * @return la cola de solicitudes.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    /**
     * Añade una solicitud a la cola
     * @param req la solicitud a añadir
     */
    public  void addToRequestQueue(Request req) {
        getRequestQueue().add(req);
    }

}
