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

    public List<Imagen> readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerImagenes(reader);
        } finally {
            reader.close();
        }
    }

    public List leerImagenes(JsonReader reader) throws IOException {
        // Lista temporal
        List imagenes = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            imagenes.add(leerImagen(reader));
        }
        reader.endArray();
        return imagenes;
    }

    public Imagen leerImagen(JsonReader reader) throws IOException{
        int id = 0;
        String nombre = null;

        reader.beginObject();
        while (reader.hasNext()){
            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "name":
                    nombre = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Imagen(id, nombre);
    }

    public Imagen leerImagen(JSONObject object) throws IOException, JSONException {
        int id;
        String name = "";

        id = (Integer) object.get("id");
        if(!object.isNull("newFilename")) name = (String) object.get("newFilename");

        return new Imagen(id, name);
    }

}