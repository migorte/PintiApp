package es.pintiavaccea.pintiapp;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Miguel on 02/05/2016.
 */
public class Hito implements Parcelable {

    private int id;
    private int numeroHito;
    private String titulo;
    private String subtitulo;
    private double latitud;
    private double longitud;
    private boolean itinerario;
    private String texto;
    private int idImagenPortada;

    public Hito(int id, int numeroHito, String titulo, String subtitulo, double latitud,
                double longitud, boolean itinerario, String texto, int idImagenPortada){
        this.id = id;
        this.numeroHito = numeroHito;
        this.titulo = titulo;
        this.subtitulo = subtitulo;
        this.latitud = latitud;
        this.longitud = longitud;
        this.itinerario = itinerario;
        this.texto = texto;
        this.idImagenPortada = idImagenPortada;
    }

    protected Hito(Parcel in) {
        setId(in.readInt());
        setNumeroHito(in.readInt());
        setTitulo(in.readString());
        setSubtitulo(in.readString());
        setLatitud(in.readDouble());
        setLongitud(in.readDouble());
        setItinerario(in.readByte() != 0);
        setTexto(in.readString());
        setIdImagenPortada(in.readInt());
    }

    public static final Creator<Hito> CREATOR = new Creator<Hito>() {
        @Override
        public Hito createFromParcel(Parcel in) {
            return new Hito(in);
        }

        @Override
        public Hito[] newArray(int size) {
            return new Hito[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeInt(getNumeroHito());
        dest.writeString(getTitulo());
        dest.writeString(getSubtitulo());
        dest.writeDouble(getLatitud());
        dest.writeDouble(getLongitud());
        dest.writeByte((byte) (isItinerario() ? 1 : 0));
        dest.writeString(getTexto());
        dest.writeInt(getIdImagenPortada());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNumeroHito() {
        return numeroHito;
    }

    public void setNumeroHito(int numeroHito) {
        this.numeroHito = numeroHito;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getSubtitulo() {
        return subtitulo;
    }

    public void setSubtitulo(String subtitulo) {
        this.subtitulo = subtitulo;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isItinerario() {
        return itinerario;
    }

    public void setItinerario(boolean itinerario) {
        this.itinerario = itinerario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getIdImagenPortada() {
        return idImagenPortada;
    }

    public void setIdImagenPortada(int idImagenPortada) {
        this.idImagenPortada = idImagenPortada;
    }
}
