package es.ideas.holeventoproyecto.fragments.normalUser;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import es.ideas.holeventoproyecto.adapter.NormalUserEventAdapter;
import es.ideas.holeventoproyecto.modelo.Evento;


public class MainPage extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private NormalUserEventAdapter adapter;
    private FirebaseUser user;

    public MainPage() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_normal_user_main_page, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("Eventos");
        Query eventos = eventosRef.orderBy("fechaEvento" ,Query.Direction.ASCENDING);


        rv = (RecyclerView) viewRoot.findViewById(R.id.rvMainUser);

        rv.setLayoutManager(new LinearLayoutManager(viewRoot.getContext()));

        FirestoreRecyclerOptions<Evento> options
                = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(eventos, Evento.class)
                .build();

        adapter = new NormalUserEventAdapter(options, viewRoot.getContext(), id);
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