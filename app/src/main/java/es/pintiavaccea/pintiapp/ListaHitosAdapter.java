package es.pintiavaccea.pintiapp;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
                .inflate(R.layout.item_lista_hitos, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView foto;
        public TextView titulo;
        public TextView subtitulo;
        public Hito hito;
        private final Context context;

        public ViewHolder(View v) {
            super(v);

            foto = (ImageView) v.findViewById(R.id.hito_foto);
            titulo = (TextView) v.findViewById(R.id.hito_titulo);
            subtitulo = (TextView) v.findViewById(R.id.hito_subtitulo);
            context = v.getContext();

            v.setClickable(true);
            v.setOnClickListener(this);

        }

        public void bind(Hito hito){
            this.hito = hito;
            titulo.setText(hito.getTitulo());
            subtitulo.setText(hito.getSubtitulo());
            Picasso.with(context).setIndicatorsEnabled(true);
            Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/picture/4").error(R.drawable.img201205191603108139).into(foto);
        }

        @Override
        public void onClick(View view) {
            final Intent intent;
            intent = new Intent(context, DetalleHitoActivity.class);
            intent.putExtra("hito", hito);
            ActivityOptions options;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                options = ActivityOptions
                        .makeSceneTransitionAnimation((Activity) view.getContext(), foto, "portada_hito");
                view.getContext().startActivity(intent, options.toBundle());
            } else {
                view.getContext().startActivity(intent);
            }
        }
    }
}
