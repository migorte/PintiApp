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

/**
 * Created by Miguel on 06/06/2016.
 */
public class JsonHitoParser {

    public List<Hito> readJsonStream(InputStream in) throws IOException {
        // Nueva instancia JsonReader
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            // Leer Array
            return leerHitos(reader);
        } finally {
            reader.close();
        }
    }

    public List leerHitos(JsonReader reader) throws IOException {
        // Lista temporal
        List hitos = new ArrayList();

        reader.beginArray();
        while (reader.hasNext()) {
            // Leer objeto
            hitos.add(leerHito(reader));
        }
        reader.endArray();
        return hitos;
    }

    public Hito leerHito(JsonReader reader) throws IOException {
        int id = 0;
        int numeroHito = 0;
        String titulo = null;
        String subtitulo = null;
        double latitud = 0.0;
        double longitud = 0.0;
        String texto = null;
        int idImagenPortada = 0;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "numeroHito":
                    numeroHito = reader.nextInt();
                    break;
                case "titulo":
                    titulo = reader.nextString();
                    break;
                case "subtitulo":
                    subtitulo = reader.nextString();
                    break;
                case "latitud":
                    latitud = reader.nextDouble();
                    break;
                case "longitud":
                    longitud = reader.nextDouble();
                    break;
                case "texto":
                    texto = reader.nextString();
                    break;
                case "idImagenPortada":
                    idImagenPortada = reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Hito(id, numeroHito, titulo, subtitulo, latitud, longitud, true, texto, idImagenPortada);
    }

    public Hito leerHito(JSONObject object) throws IOException, JSONException {
        int id;
        int numeroHito;
        String titulo;
        String subtitulo;
        double latitud;
        double longitud;
        String texto;
        int idImagenPortada;

        id = (Integer) object.get("id");
        numeroHito = (Integer) object.get("numeroHito");
        titulo = (String) object.get("titulo");
        subtitulo = (String) object.get("subtitulo");
        latitud = (double) object.get("latitud");
        longitud = (double) object.get("longitud");
        texto = (String) object.get("texto");
        idImagenPortada = (Integer) object.get("idImagenPortada");

        return new Hito(id, numeroHito, titulo, subtitulo, latitud, longitud, true, texto, idImagenPortada);
    }
}
