package es.pintiavaccea.pintiapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Miguel on 28/04/2016.
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
        public static final String FECHA = "fecha";
        public static final String LATITUD = "latitud";
        public static final String LONGITUD = "latitud";
        public static final String TEXTO = "texto";
        public static final String ITINERARIO = "itinerario";
    }

    public static class ColumnImagen {
        public static final String ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String RUTA = "ruta";
        public static final String PORTADA = "portada";
        public static final String HITO = "hito";
    }

    public static class ColumnVideo {
        public static final String ID = BaseColumns._ID;
        public static final String NOMBRE = "nombre";
        public static final String RUTA = "ruta";
        public static final String HITO = "hito";
    }

    public static final String CREATE_HITO_SCRIPT = "create table " + HITO_TABLE + " ( " +
            ColumnHito.ID + " " + INT_TYPE + " primary key," +
            ColumnHito.NUMERO_HITO + " " + INT_TYPE + " not null," +
            ColumnHito.TITULO + " " + STRING_TYPE + " not null," +
            ColumnHito.SUBTITULO + " " + STRING_TYPE + " not null," +
            ColumnHito.FECHA + " " + STRING_TYPE + " not null," +
            ColumnHito.LATITUD + " " + REAL_TYPE + " not null," +
            ColumnHito.LONGITUD + " " + REAL_TYPE + " not null," +
            ColumnHito.TEXTO + " " + REAL_TYPE + " not null," +
            ColumnHito.ITINERARIO + " " + INT_TYPE + " not null)";

    public static final String CREATE_IMAGEN_SCRIPT = "create table " + IMAGEN_TABLE + " ( " +
            ColumnImagen.ID + " " + INT_TYPE + " primary key," +
            ColumnImagen.NOMBRE + " " + STRING_TYPE + " not null," +
            ColumnImagen.RUTA + " " + STRING_TYPE + " not null," +
            ColumnImagen.PORTADA + " " + STRING_TYPE + " not null," +
            ColumnImagen.HITO + " " + INT_TYPE + " not null)";

    public static final String CREATE_VIDEO_SCRIPT = "create table " + IMAGEN_TABLE + " ( " +
            ColumnImagen.ID + " " + INT_TYPE + " primary key," +
            ColumnImagen.NOMBRE + " " + STRING_TYPE + " not null," +
            ColumnImagen.RUTA + " " + STRING_TYPE + " not null," +
            ColumnImagen.HITO + " " + INT_TYPE + " not null)";


    public DataSource(Context context) {
        //Creando una instancia hacia la base de datos
        DBHelper openHelper = DBHelper.getInstance(context);
        database = openHelper.getWritableDatabase();
    }
}
