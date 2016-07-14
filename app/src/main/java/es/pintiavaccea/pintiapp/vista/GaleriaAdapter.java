package es.pintiavaccea.pintiapp.vista;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.utility.StorageManager;

/**
 * Created by Miguel on 09/06/2016.
 *
 * Adaptador de la galería de imágenes. Provee acceso a los datos de los items y es responsable
 * de crear una vista para cada uno de ellos.
 */
public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder> {

    private final List<Imagen> mDataset;

    public GaleriaAdapter(List<Imagen> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public GaleriaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_galeria, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(GaleriaAdapter.ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Describe un la vista de un item del adaptador y sus metadatos.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final Context context;
        private Imagen imagen;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imagen_galeria);
            context = v.getContext();

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageZoomActivity.class);
                    intent.putExtra("imagen", imagen);
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        ActivityOptions options = ActivityOptions
                                .makeSceneTransitionAnimation((AppCompatActivity) context, imageView, "imagen_galeria");
                        context.startActivity(intent, options.toBundle());
                    } else {
                        context.startActivity(intent);
                    }
                }
            });
        }

        /**
         * Enlaza la imagen correspondiente al item del adaptador.
         * @param imagen la imagen correspondiente al item
         */
        public void bind(Imagen imagen) {
            this.imagen = imagen;

            Bitmap bitmapPortada = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.img201205191603108139);

            try {
                bitmapPortada = StorageManager.loadImageFromStorage(imagen.getNombre(), context);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            Drawable error = new BitmapDrawable(context.getResources(), bitmapPortada);

            Picasso.with(context).setIndicatorsEnabled(true);
            Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/thumbnail/"
                    + imagen.getId()).error(error).into(imageView);
            Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                    + imagen.getId()).into(StorageManager.getTarget(imagen.getNombre(), context));
        }
    }
}
