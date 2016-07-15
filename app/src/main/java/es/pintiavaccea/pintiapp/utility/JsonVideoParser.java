package es.pintiavaccea.pintiapp.utility;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.modelo.Video;

/**
 * Created by Miguel on 28/06/2016.
 *
 * Util para parsear un json y lo convierte a Video
 */
public class JsonVideoParser {
    /**
     * Parsea un JSONObject y lo convierte a Video
     * @param object el JSONObject a parsear
     * @return el Video sacado del json
     * @throws JSONException
     */
    public Video leerVideo(JSONObject object) throws JSONException {
        int id;
        String nombre;

        id = (Integer) object.get("id");
        nombre = (String) object.get("name");

        return new Video(id, nombre);
    }
}
