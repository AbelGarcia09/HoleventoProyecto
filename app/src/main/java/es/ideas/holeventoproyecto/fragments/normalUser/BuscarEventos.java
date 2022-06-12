package es.ideas.holeventoproyecto.fragments.normalUser;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.adapter.BusinessHomeAdapter;
import es.ideas.holeventoproyecto.modelo.Evento;

public class BuscarEventos extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private FirebaseUser user;
    private Query mbase;

    public BuscarEventos() {}



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.fragment_buscar_eventos, container, false);

        return viewRoot;
    }
}