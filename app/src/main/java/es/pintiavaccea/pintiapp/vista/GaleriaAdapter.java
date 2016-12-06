package es.pintiavaccea.pintiapp.vista;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;

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
        private static final String URL = "http://per.infor.uva.es:8080/pintiaserver/pintiaserver";

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
        public void bind(final Imagen imagen) {
            this.imagen = imagen;

            Picasso.with(context)
                    .load(URL + "/thumbnail/" + imagen.getId())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(context)
                                    .load(URL + "/thumbnail/" + imagen.getId())
                                    .error(R.drawable.logo_cevfw_opt)
                                    .into(imageView, new Callback() {
                                        @Override
                                        public void onSuccess() {

                                        }

                                        @Override
                                        public void onError() {
                                            Log.v("Picasso","Could not fetch image");
                                        }
                                    });
                        }
                    });
        }
    }
}
