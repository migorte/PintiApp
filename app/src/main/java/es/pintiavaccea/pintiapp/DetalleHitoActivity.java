package es.pintiavaccea.pintiapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetalleHitoActivity extends AppCompatActivity {

    private Hito hito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_hito);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState==null){
            Bundle extras = getIntent().getExtras();
            if(extras == null){
                hito = null;
            } else hito = extras.getParcelable("hito");
        } else {
            hito = savedInstanceState.getParcelable("hito");
        }

//        assignHito(savedInstanceState);
        this.setTitle(hito.getTitulo());
        setContent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putParcelable("hito", hito);
    }

    private void setContent(){
        TextView subtitulo = (TextView) findViewById(R.id.subtitulo);
        subtitulo.setText(hito.getSubtitulo());
        TextView fecha = (TextView) findViewById(R.id.fecha);
        fecha.setText(hito.getFecha());
        TextView texto = (TextView) findViewById(R.id.texto);
        texto.setText(hito.getTexto());
    }

    /**
     * Obtiene el hito del intent
     *
     * @param savedInstanceState savedInstanceState
     */

//    private void assignHito(Bundle savedInstanceState) {
//        if (savedInstanceState == null) {
//            Bundle extras = getIntent().getExtras();
//            if (extras == null) {
//                hito = null;
//            } else {
//                hito = (Hito) extras.getSerializable("hito");
//            }
//        } else {
//            hito = (Hito) savedInstanceState.getSerializable("hito");
//        }
//    }
}
