package es.pintiavaccea.pintiapp.utility;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Miguel on 28/04/2016.
 *
 * Implementa la persistencia de la aplicación. Inserta los datos en una base de datos SQLite.
 */
public class DataSource {
    private static final String HITO_TABLE = "Hito";
    private static final String IMAGEN_TABLE = "Imagen";
    private static final String VIDEO_TABLE = "Video";

    private static final String STRING_TYPE = "text";
    private static final String INT_TYPE = "integer";
    private static final String REAL_TYPE = "real";

    private final SQLiteDatabase database;

    public static class ColumnHito {
        public static final String ID = BaseColumns._ID;
        public static final String NUMERO_HITO = "numeroHito";
        public static final String TITULO = "titulo";
        public static final String SUBTITULO = "subtitulo";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "longitud";
        public static final String TEXTO = "texto";
        public static final String ITINERARIO = "itinerario";
        public static final String IDIMAGENPORTADA = "idImagenPortada";
    }

    public static class ColumnImagen {
        public static final String ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String HITO = "hito";
    }

    public static class ColumnVideo {
        public static final String ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String HITO = "hito";
    }

    public static final String CREATE_HITO_SCRIPT = "create table " + HITO_TABLE + " ( " +
            ColumnHito.ID + " " + INT_TYPE + " primary key," +
            ColumnHito.NUMERO_HITO + " " + INT_TYPE + " not null," +
            ColumnHito.TITULO + " " + STRING_TYPE + " not null," +
            ColumnHito.SUBTITULO + " " + STRING_TYPE + " not null," +
            ColumnHito.LATITUD + " " + REAL_TYPE + " not null," +
            ColumnHito.LONGITUD + " " + REAL_TYPE + " not null," +
            ColumnHito.TEXTO + " " + REAL_TYPE + " not null," +
            ColumnHito.ITINERARIO + " " + INT_TYPE + " not null," +
            ColumnHito.IDIMAGENPORTADA + " " + INT_TYPE + " not null)";

    public static final String CREATE_IMAGEN_SCRIPT = "create table " + IMAGEN_TABLE + " ( " +
            ColumnImagen.ID + " " + INT_TYPE + " primary key," +
            ColumnImagen.NOMBRE + " " + STRING_TYPE + " not null," +
            ColumnImagen.HITO + " " + INT_TYPE + " not null)";

    public static final String CREATE_VIDEO_SCRIPT = "create table " + VIDEO_TABLE + " ( " +
            ColumnVideo.ID + " " + INT_TYPE + " primary key," +
            ColumnVideo.NOMBRE + " " + STRING_TYPE + " not null," +
            ColumnVideo.HITO + " " + INT_TYPE + " not null)";

    public DataSource(Context context) {
        //Creando una instancia hacia la base de datos
        DBHelper openHelper = DBHelper.getInstance(context);
        database = openHelper.getWritableDatabase();
    }

    /**
     * Borra todos los hitos
     */
    public void clearHitos(){
        database.delete(HITO_TABLE, null, null);
    }

    /**
     * Borra todas las imágenes de un determinado hito
     * @param hito el id del hito
     */
    public void clearImagenes(int hito){
        database.delete(IMAGEN_TABLE, ColumnImagen.HITO + "=" + hito, null);
    }

    /**
     * Devuelve todos los hitos guardados en la base de datos
     * @return todos los hitos guardados
     */
    public List<Hito> getAllHitos() {
        Cursor cursor = database.rawQuery("select * from " + HITO_TABLE, null);

        cursor.moveToFirst();

        List<Hito> listaHitos = new ArrayList<>();

        while (!cursor.isAfterLast()) {

            boolean itinerario = false;

            if (cursor.getInt(cursor.getColumnIndex(ColumnHito.ITINERARIO)) == 1) itinerario = true;

            Hito hito = new Hito(cursor.getInt(cursor.getColumnIndex(ColumnHito.ID)),
                    cursor.getInt(cursor.getColumnIndex(ColumnHito.NUMERO_HITO)),
                    cursor.getString(cursor.getColumnIndex(ColumnHito.TITULO)),
                    cursor.getString(cursor.getColumnIndex(ColumnHito.SUBTITULO)),
                    cursor.getDouble(cursor.getColumnIndex(ColumnHito.LATITUD)),
                    cursor.getDouble(cursor.getColumnIndex(ColumnHito.LONGITUD)),
                    itinerario,
                    cursor.getString(cursor.getColumnIndex(ColumnHito.TEXTO)),
                    cursor.getInt(cursor.getColumnIndex(ColumnHito.IDIMAGENPORTADA)));

            listaHitos.add(hito);
            cursor.moveToNext();
        }
        cursor.close();

        return listaHitos;
    }

    /**
     * Guarda una lista de hitos en la base de datos
     * @param hitos la lista de hitos
     */
    public void saveListaHitos(List<Hito> hitos){

        for(Hito hito : hitos){

            ContentValues values = new ContentValues();

            values.put(ColumnHito.ID, hito.getId());
            values.put(ColumnHito.NUMERO_HITO, hito.getNumeroHito());
            values.put(ColumnHito.TITULO, hito.getTitulo());
            values.put(ColumnHito.SUBTITULO, hito.getSubtitulo());
            values.put(ColumnHito.LATITUD, hito.getLatitud());
            values.put(ColumnHito.LONGITUD, hito.getLongitud());
            values.put(ColumnHito.TEXTO, hito.getTexto());
            values.put(ColumnHito.ITINERARIO, 1);
            values.put(ColumnHito.IDIMAGENPORTADA, hito.getIdImagenPortada());

            database.insert(HITO_TABLE, null, values);
        }
    }

    /**
     * Guarda una imagen en la base de datos
     * @param imagen la imagen a guardar
     */
    public void saveImagen(Imagen imagen){
        ContentValues values = new ContentValues();

        values.put(ColumnImagen.ID, imagen.getId());
        values.put(ColumnImagen.NOMBRE, imagen.getNombre());
        values.put(ColumnImagen.HITO, imagen.getHito());

        database.insert(IMAGEN_TABLE, null, values);
    }

    /**
     * Devuelve una imagen según su id
     * @param id el id de la imagen a devolver
     * @return la imagen
     */
    public Imagen getImagen(int id) {
        String[] args = new String[]{Integer.toString(id)};
        Cursor cursor = database.rawQuery("select * from " + IMAGEN_TABLE + " where " +
                ColumnImagen.ID + " = ?", args);

        cursor.moveToFirst();

        Imagen imagen = null;

        if (cursor.getCount()>0) {
            imagen = new Imagen(cursor.getInt(cursor.getColumnIndex(ColumnImagen.ID)),
                    cursor.getString(cursor.getColumnIndex(ColumnImagen.NOMBRE)));
        }
        cursor.close();

        return imagen;
    }

    /**
     * Devuelve todas las imagenes de un hito
     * @param idHito el id del hito
     * @return todas las imágenes del hito
     */
    public List<Imagen> getImagenesHito(int idHito) {
        String[] args = new String[]{Integer.toString(idHito)};
        Cursor cursor = database.rawQuery("select * from " + IMAGEN_TABLE + " where "
                + ColumnImagen.HITO + " =?", args);

        cursor.moveToFirst();

        List<Imagen> imagenes = new ArrayList<>();

        while (!cursor.isAfterLast()) {

            Imagen imagen = new Imagen(cursor.getInt(cursor.getColumnIndex(ColumnImagen.ID)),
                    cursor.getString(cursor.getColumnIndex(ColumnImagen.NOMBRE)));

            imagenes.add(imagen);
            cursor.moveToNext();
        }
        cursor.close();

        return imagenes;
    }

    /**
     * Borra una imagen según su id
     * @param id el id de la imagen a borrar
     */
    public void removeImagen(int id){
        database.delete(IMAGEN_TABLE, ColumnImagen.ID + "=" + id, null);
    }

    /**
     * Comprueba si existe una imagen en la base de datos
     * @param imagen la imagen a comprobar
     * @return true si la imagen existe
     */
    public boolean existImagen(Imagen imagen){
        String[] args = new String[]{Integer.toString(imagen.getId())};
        Cursor cursor = database.rawQuery("select * from " + IMAGEN_TABLE + " where "
                + ColumnImagen.ID + " =?", args);
        if(cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        return true;
    }

    /**
     * Devuelve el hito más cercano a una posición determinada.
     * @param latitud la latitud de la ubicación
     * @param longitud la longitud de la ubicación
     * @return el hito más cercano
     */
    public Hito getHitoCercano(double latitud, double longitud){
        Cursor cursor = database.rawQuery("select * from " + HITO_TABLE, null);

        cursor.moveToFirst();

        Hito resultado = null;

        Double distanciaMin = null;

        while (!cursor.isAfterLast()) {

            boolean itinerario = false;
            if (cursor.getInt(cursor.getColumnIndex(ColumnHito.ITINERARIO)) == 1) itinerario = true;

            Hito hito = new Hito(cursor.getInt(cursor.getColumnIndex(ColumnHito.ID)),
                    cursor.getInt(cursor.getColumnIndex(ColumnHito.NUMERO_HITO)),
                    cursor.getString(cursor.getColumnIndex(ColumnHito.TITULO)),
                    cursor.getString(cursor.getColumnIndex(ColumnHito.SUBTITULO)),
                    cursor.getDouble(cursor.getColumnIndex(ColumnHito.LATITUD)),
                    cursor.getDouble(cursor.getColumnIndex(ColumnHito.LONGITUD)),
                    itinerario,
                    cursor.getString(cursor.getColumnIndex(ColumnHito.TEXTO)),
                    cursor.getInt(cursor.getColumnIndex(ColumnHito.IDIMAGENPORTADA)));

            double distancia = Math.sqrt(Math.pow(latitud - hito.getLatitud(), 2) +
                    Math.pow(longitud - hito.getLongitud(), 2));

            if (distanciaMin == null || distanciaMin > distancia) {
                resultado = hito;
                distanciaMin = distancia;
            }

            cursor.moveToNext();
        }

        cursor.close();

        return resultado;
    }
}
