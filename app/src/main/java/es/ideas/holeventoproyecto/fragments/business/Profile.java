package es.ideas.holeventoproyecto.fragments.business;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.adapter.BusinessHomeAdapter;
import es.ideas.holeventoproyecto.modelo.Evento;

public class Profile extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private BusinessHomeAdapter adapter;
    private Toast mToast;
    private FirebaseUser user;
    public Profile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("Eventos");
        Query eventos = eventosRef.orderBy("fechaEvento" ,Query.Direction.ASCENDING).whereEqualTo("idUsuario", id);


        rv = (RecyclerView) viewRoot.findViewById(R.id.rvBusinessProf);

        rv.setLayoutManager(new LinearLayoutManager(viewRoot.getContext()));

        FirestoreRecyclerOptions<Evento> options
                = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(eventos, Evento.class)
                .build();

        adapter = new BusinessHomeAdapter(options, viewRoot.getContext());
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        adapter.notifyDataSetChanged();
    }
}