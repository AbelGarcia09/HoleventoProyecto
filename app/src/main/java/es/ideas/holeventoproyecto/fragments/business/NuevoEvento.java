package es.ideas.holeventoproyecto.fragments.business;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import es.ideas.holeventoproyecto.R;

public class NuevoEvento extends Fragment {


    public NuevoEvento() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.nuevo_evento_fragment, container, false);
    }
}