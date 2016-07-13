package es.pintiavaccea.pintiapp.utility;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Usuario on 09/06/2016.
 */
public class JsonImagenParser {

    public Imagen leerImagen(JSONObject object) throws JSONException {
        int id;
        String name = "";

        id = (Integer) object.get("id");
        if(!object.isNull("newFilename")) name = (String) object.get("newFilename");

        return new Imagen(id, name);
    }

}
