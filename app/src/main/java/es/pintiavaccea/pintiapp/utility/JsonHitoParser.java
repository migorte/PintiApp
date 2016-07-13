package es.pintiavaccea.pintiapp.utility;

import org.json.JSONException;
import org.json.JSONObject;

import es.pintiavaccea.pintiapp.modelo.Hito;

/**
 * Created by Miguel on 06/06/2016.
 */
public class JsonHitoParser {

    public Hito leerHito(JSONObject object) throws JSONException {
        int id;
        int numeroHito;
        String titulo;
        String subtitulo = "";
        double latitud = 0;
        double longitud = 0;
        String texto = "";
        int idImagenPortada;

        id = (Integer) object.get("id");
        numeroHito = (Integer) object.get("numeroHito");
        titulo = (String) object.get("titulo");
        if(!object.isNull("subtitulo")) subtitulo = (String) object.get("subtitulo");
        if(!object.isNull("latitud")) latitud = (double) object.get("latitud");
        if(!object.isNull("longitud")) longitud = (double) object.get("longitud");
        if(!object.isNull("texto")) texto = (String) object.get("texto");
        idImagenPortada = (Integer) object.get("idImagenPortada");

        return new Hito(id, numeroHito, titulo, subtitulo, latitud, longitud, true, texto, idImagenPortada);
    }
}
