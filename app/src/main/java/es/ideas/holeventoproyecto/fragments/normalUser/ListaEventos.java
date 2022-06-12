package es.ideas.holeventoproyecto.fragments.normalUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.Query;

import es.ideas.holeventoproyecto.R;

public class ListaEventos extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private FirebaseUser user;
    private Query mbase;

    public ListaEventos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_lista_eventos, container, false);

        return viewRoot;
    }
}