package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Imagen;

/**
 * Created by Usuario on 09/06/2016.
 */
public class GaleriaAdapter extends RecyclerView.Adapter<GaleriaAdapter.ViewHolder> {

    private List<Imagen> mDataset;

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

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;
        private Context context;

        public ViewHolder(View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imagen_galeria);
            context = v.getContext();
        }

        public void bind (Imagen imagen){
            Picasso.with(context).setIndicatorsEnabled(true);
            Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/"
                    + imagen.getId()).error(R.drawable.img201205191603108139).into(imageView);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
