package es.pintiavaccea.pintiapp;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Miguel on 02/05/2016.
 */
public class ListaHitosAdapter extends RecyclerView.Adapter<ListaHitosAdapter.ViewHolder> {

    private List<Hito> mDataset;
    private Context context;

    public ListaHitosAdapter(List<Hito> mDataset) {
        this.mDataset = mDataset;
    }

    @Override
    public ListaHitosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hito_card, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.titulo.setText(mDataset.get(position).getTitulo());
        holder.subtitulo.setText(mDataset.get(position).getSubtitulo());
        holder.hito = mDataset.get(position);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView foto;
        public TextView titulo;
        public TextView subtitulo;
        public TextView fecha;
        public Hito hito;
        private final Context context;

        public ViewHolder(View v) {
            super(v);

            foto = (ImageView) v.findViewById(R.id.hito_foto);
            titulo = (TextView) v.findViewById(R.id.hito_titulo);
            subtitulo = (TextView) v.findViewById(R.id.hito_subtitulo);
//            fecha = (TextView) v.findViewById(R.id.hito_fecha);
            context = v.getContext();

            v.setClickable(true);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final Intent intent;
            intent = new Intent(context, DetalleHitoActivity.class);
            intent.putExtra("hito", hito);
            context.startActivity(intent);
        }
    }
}
