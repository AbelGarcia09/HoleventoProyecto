package es.ideas.holeventoproyecto.fragments.normalUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseUser;

import es.ideas.holeventoproyecto.R;

public class ListaEventos extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private FirebaseUser user;

    public ListaEventos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_lista_eventos, container, false);

        return viewRoot;
    }
}