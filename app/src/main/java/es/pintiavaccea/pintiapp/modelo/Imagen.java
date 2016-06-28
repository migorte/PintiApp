package es.pintiavaccea.pintiapp.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Usuario on 09/06/2016.
 */
public class Imagen implements Parcelable {

    private int id;
    private String nombre;
    private Hito hito;

    public Imagen (int id, String nombre){
        this.setId(id);
        this.setNombre(nombre);
    }

    public Imagen (int id, String nombre, Hito hito){
        this.setId(id);
        this.setNombre(nombre);
        this.setHito(hito);
    }

    protected Imagen(Parcel in) {
        id = in.readInt();
        nombre = in.readString();
        hito = in.readParcelable(Hito.class.getClassLoader());
    }

    public static final Creator<Imagen> CREATOR = new Creator<Imagen>() {
        @Override
        public Imagen createFromParcel(Parcel in) {
            return new Imagen(in);
        }

        @Override
        public Imagen[] newArray(int size) {
            return new Imagen[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Hito getHito() {
        return hito;
    }

    public void setHito(Hito hito) {
        this.hito = hito;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nombre);
        dest.writeParcelable(hito, flags);
    }
}
