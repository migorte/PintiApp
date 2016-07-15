package es.pintiavaccea.pintiapp.modelo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Miguel on 28/06/2016.
 *
 * Modela un video que muestra información sobre un hito del yacimiento.
 */
@SuppressWarnings("WeakerAccess")
public class Video implements Parcelable {

    private int id;
    private String name;

    public Video (int id, String nombre){
        this.id = id;
        this.name = nombre;
    }

    protected Video(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel in) {
            return new Video(in);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(getId());
        dest.writeString(getName());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
