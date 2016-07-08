package es.pintiavaccea.pintiapp.utility;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Miguel on 28/04/2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static DBHelper mInstance = null;

    public static DBHelper getInstance(Context context){
        if(mInstance == null){
            mInstance = new DBHelper(context);
        }
        return mInstance;
    }

    private DBHelper(Context context){
        super(context, "PintiApp", null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //Crear la base de datos
        db.execSQL(DataSource.CREATE_HITO_SCRIPT);
        db.execSQL(DataSource.CREATE_IMAGEN_SCRIPT);
        db.execSQL(DataSource.CREATE_VIDEO_SCRIPT);

//        db.execSQL(DataSource.INSERT_HITO_SCRIPT);
//        db.execSQL(DataSource.INSERT_HITO_SCRIPT1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
