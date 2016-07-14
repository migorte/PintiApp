package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.jorgecastilloprz.FABProgressCircle;
import com.squareup.picasso.Picasso;

import es.pintiavaccea.pintiapp.presentador.GeoPresenter;
import es.pintiavaccea.pintiapp.R;

/**
 * Created by Miguel on 09/06/2016.
 *
 * Fragmento que se muestra al iniciar la aplicación. Es la tab de la izquierda del tab layout.
 * Contiene la funcionalidad de obtener el hito más cercano a la geolocalización del usuario.
 */
public class GeoFragment extends Fragment implements GeoView{

    private FABProgressCircle fab;
    private GeoPresenter geoPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final ViewGroup myFragmentView = (ViewGroup) inflater.inflate(
                R.layout.fragment_geo, container, false);

        fab = (FABProgressCircle) myFragmentView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.show();
                geoPresenter.getCloserHito();
//                fab.beginFinalAnimation();
            }
        });

        ImageView imagen = (ImageView) myFragmentView.findViewById(R.id.foto_portada);
        Picasso.with(getActivity()).setIndicatorsEnabled(true);
        Picasso.with(getActivity()).load(R.drawable.img201205191603108139).fit().centerInside().into(imagen);


        return myFragmentView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        geoPresenter = new GeoPresenter(this);

    }

    @Override
    public Context getViewContext() {
        return this.getActivity();
    }

    @Override
    public Location getLastLocation() {
        return ((MainActivity) getActivity()).getmLastLocation();
    }

    @Override
    public void openHito(Intent intent){
        startActivity(intent);
        stopFabAnimation();
    }

    @Override
    public void stopFabAnimation() {
        fab.hide();
    }

    @Override
    public void showInternetError() {
        Toast.makeText(getActivity(), "No se tiene conexión a internet",
                Toast.LENGTH_SHORT).show();
        stopFabAnimation();
    }

    @Override
    public void showLocationError() {
        Toast.makeText(getActivity(), "No se pudo obtener su localización",
                Toast.LENGTH_SHORT).show();
        stopFabAnimation();
    }

//
//
//    /**
//     * Comprueba si existe conexion a internet
//     *
//     * @param context contexto de la actividad
//     * @return true si existe conexion a internet
//     */
//    public static boolean checkNetwork(Context context) {
//        ConnectivityManager connMgr = (ConnectivityManager)
//                context.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
//        return (networkInfo != null && networkInfo.isConnected());
//    }
}
