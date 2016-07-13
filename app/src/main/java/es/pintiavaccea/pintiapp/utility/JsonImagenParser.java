package es.pintiavaccea.pintiapp.utility;

import android.util.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.modelo.Hito;
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
