package es.pintiavaccea.pintiapp;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Miguel on 07/06/2016.
 */
public class GsonHitoParser<E> {

    public Class<E> type;

    public GsonHitoParser(Class<E> type) {
        this.type = type;
    }

    public List<E> readJsonStream(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        List<E> objetos = new ArrayList<E>();
        while (reader.hasNext()) {
            E objeto = gson.fromJson(reader, type);
            objetos.add(objeto);
        }

        reader.endArray();
        reader.close();
        return objetos;
    }

    public E readJson(InputStream in) throws IOException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));

        E objeto = gson.fromJson(reader, type);


        reader.endObject();
        reader.close();
        return objeto;
    }
}
