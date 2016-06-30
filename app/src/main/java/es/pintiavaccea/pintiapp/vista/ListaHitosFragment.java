package es.pintiavaccea.pintiapp.vista;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import es.pintiavaccea.pintiapp.presentador.ListaHitosPresenter;
import es.pintiavaccea.pintiapp.R;

public class ListaHitosFragment extends Fragment implements ListaHitosView {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ListaHitosPresenter listaHitosPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        listaHitosPresenter = new ListaHitosPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_lista_hitos, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setHasOptionsMenu(true);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        listaHitosPresenter.getListaHitos();

    }

    @Override
    public Context getViewContext(){
        return getActivity();
    }

    @Override
    public void setmAdapter(RecyclerView.Adapter adapter){
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void showError(String msg){
        Toast.makeText(getContext(), msg,
                Toast.LENGTH_LONG).show();
    }
}
