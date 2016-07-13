package es.pintiavaccea.pintiapp.utility;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.modelo.Video;

/**
 * Created by madri on 28/06/2016.
 */
public class JsonVideoParser {
    public Video leerVideo(JSONObject object) throws JSONException {
        int id;
        String nombre;

        id = (Integer) object.get("id");
        nombre = (String) object.get("name");

        return new Video(id, nombre);
    }
}
