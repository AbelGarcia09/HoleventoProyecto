package es.ideas.holeventoproyecto.fragments.normalUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ideas.holeventoproyecto.R;

public class BuscarEventos extends Fragment {

    public BuscarEventos() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buscar_eventos, container, false);
    }
}