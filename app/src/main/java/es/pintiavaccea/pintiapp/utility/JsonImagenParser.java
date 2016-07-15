package es.pintiavaccea.pintiapp.utility;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Miguel on 09/06/2016.
 *
 * Util para parsear un json y lo convierte en una imagen
 */
public class JsonImagenParser {

    /**
     * Parsea un JSONObject y lo convierte a Imagen
     * @param object el JSONObject a convertir
     * @return la Imagen convertida
     * @throws JSONException
     */
    public Imagen leerImagen(JSONObject object) throws JSONException {
        int id;
        String name = "";

        id = (Integer) object.get("id");
        if(!object.isNull("newFilename")) name = (String) object.get("newFilename");

        return new Imagen(id, name);
    }

}
