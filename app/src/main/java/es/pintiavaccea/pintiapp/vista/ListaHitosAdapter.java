package es.pintiavaccea.pintiapp.vista;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;
import es.pintiavaccea.pintiapp.modelo.Imagen;
import es.pintiavaccea.pintiapp.utility.DataSource;
import es.pintiavaccea.pintiapp.utility.StorageManager;

/**
 * Created by Miguel on 02/05/2016.
 * <p/>
 * Adaptador de la lista de hitos. Provee acceso a los datos de los hitos y es responsable
 * de crear una vista para cada uno de ellos.
 */
public class ListaHitosAdapter extends RecyclerView.Adapter<ListaHitosAdapter.ViewHolder> {

    private List<Hito> mDataset;
    private Context context;
    private int lastPosition = -1;

    public ListaHitosAdapter(List<Hito> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @Override
    public ListaHitosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lista_hitos, parent, false);
        // set the view's size, margins, paddings and layout parameters

        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mDataset.get(position));
        setAnimation(holder.container, position);
    }

    /**
     * Configura la animación que se crea al aparecer cada item del adaptador
     *
     * @param viewToAnimate la vista a animar
     * @param position      la posición de la vista en el adaptador
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    /**
     * Describe un la vista de un hito del adaptador y sus metadatos.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout container;
        public ImageView foto;
        public TextView numero;
        public TextView titulo;
        public TextView subtitulo;
        public Hito hito;
        private final Context context;

        public ViewHolder(View v) {
            super(v);

            container = (RelativeLayout) v.findViewById(R.id.container);
            foto = (ImageView) v.findViewById(R.id.hito_foto);
            numero = (TextView) v.findViewById(R.id.hito_numero);
            titulo = (TextView) v.findViewById(R.id.hito_titulo);
            subtitulo = (TextView) v.findViewById(R.id.hito_subtitulo);
            context = v.getContext();

            v.setClickable(true);
            v.setOnClickListener(this);

        }

        /**
         * Enlaza el hito correspondiente a la vista del item.
         *
         * @param hito el hito que corresponde a la vista
         */
        public void bind(Hito hito) {
            this.hito = hito;
            if(hito.isItinerario()) numero.setText(String.valueOf(hito.getNumeroHito()));
            else numero.setText("");
            titulo.setText(hito.getTitulo());
            subtitulo.setText(hito.getSubtitulo());

            DataSource dataSource = new DataSource(context);
            Imagen portada = dataSource.getImagen(hito.getIdImagenPortada());

            if (portada != null) {
                Bitmap bitmapPortada = null;
                try {
                    bitmapPortada = StorageManager.loadImageFromStorage(portada.getNombre(), context);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Drawable error = new BitmapDrawable(context.getResources(), bitmapPortada);

                Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/thumbnail/" +
                        hito.getIdImagenPortada())
                        .error(error).into(foto);
            } else {
                Picasso.with(context).load("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/thumbnail/" +
                        hito.getIdImagenPortada()).into(foto);
                StorageManager.saveImage("http://virtual.lab.inf.uva.es:20212/pintiaserver/pintiaserver/getPortada/", hito.getId(), context);
            }

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
