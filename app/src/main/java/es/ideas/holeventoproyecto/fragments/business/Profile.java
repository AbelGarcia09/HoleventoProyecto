package es.ideas.holeventoproyecto.fragments.business;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.adapter.BusinessHomeAdapter;
import es.ideas.holeventoproyecto.modelo.Evento;

public class Profile extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private BusinessHomeAdapter adapter;
    private DatabaseReference mbase;
    private Toast mToast;

    public Profile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        mbase = FirebaseDatabase.getInstance().getReference().child("Eventos");
        rv = (RecyclerView) viewRoot.findViewById(R.id.rvBusinessProf);

        rv.setLayoutManager(new LinearLayoutManager(viewRoot.getContext()));

        FirebaseRecyclerOptions<Evento> options
                = new FirebaseRecyclerOptions.Builder<Evento>()
                .setQuery(mbase, Evento.class)
                .build();

        adapter = new BusinessHomeAdapter(options);
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