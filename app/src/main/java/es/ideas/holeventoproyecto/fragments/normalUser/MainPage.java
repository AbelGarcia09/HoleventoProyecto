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
import es.ideas.holeventoproyecto.adapter.NormalUserEventAdapter;
import es.ideas.holeventoproyecto.modelo.Evento;


public class MainPage extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private Query mbase;
    private NormalUserEventAdapter adapter;

    public MainPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_normal_user_main_page, container, false);


        mbase = FirebaseDatabase.getInstance().getReference().child("Eventos").orderByChild("fechaEvento");
        rv = (RecyclerView) viewRoot.findViewById(R.id.rvMainUser);

        rv.setLayoutManager(new LinearLayoutManager(viewRoot.getContext()));

        FirebaseRecyclerOptions<Evento> options
                = new FirebaseRecyclerOptions.Builder<Evento>()
                .setQuery(mbase, Evento.class)
                .build();

        adapter = new NormalUserEventAdapter(options, viewRoot.getContext());
        rv.setAdapter(adapter);

        return viewRoot;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        adapter.startListening();
    }

    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public  void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }
}