package es.pintiavaccea.pintiapp.vista;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import es.pintiavaccea.pintiapp.R;
import es.pintiavaccea.pintiapp.modelo.Hito;

/**
 * Created by Miguel on 02/05/2016.
 * <p/>
 * Adaptador de la lista de hitos. Provee acceso a los datos de los hitos y es responsable
 * de crear una vista para cada uno de ellos.
 */
@SuppressWarnings("CanBeFinal")
public class ListaHitosAdapter extends RecyclerView.Adapter<ListaHitosAdapter.ViewHolder> {

    private List<Hito> mDataset;
    private Context context;
    private int lastPosition = -1;

    public ListaHitosAdapter(List<Hito> mDataset, Context context) {
        List<Hito> itinerario = new ArrayList<>();
        List<Hito> noItinerario = new ArrayList<>();
        for (Hito h : mDataset) {
            if (h.getNumeroHito() == 0)
                noItinerario.add(h);
            else itinerario.add(h);
        }
        itinerario.addAll(noItinerario);
        this.mDataset = itinerario;
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

        private RelativeLayout container;
        private ImageView foto;
        private TextView numero;
        private TextView titulo;
        private TextView subtitulo;
        private Hito hito;
        private static final String URL = "http://per.infor.uva.es:8080/pintiaserver/pintiaserver";
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
        public void bind(final Hito hito) {
            this.hito = hito;
            if (hito.isItinerario()) numero.setText(String.valueOf(hito.getNumeroHito()));
            else numero.setText("");
            titulo.setText(hito.getTitulo());
            subtitulo.setText(hito.getSubtitulo());

            Picasso.with(context)
                    .load(URL + "/thumbnail/" + hito.getIdImagenPortada())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(foto, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            //Try again online if cache failed
                            Picasso.with(context)
                                    .load(URL + "/thumbnail/" + hito.getIdImagenPortada())
                                    .error(R.drawable.logo_cevfw_opt)
                                    .into(foto, new Callback() {
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
