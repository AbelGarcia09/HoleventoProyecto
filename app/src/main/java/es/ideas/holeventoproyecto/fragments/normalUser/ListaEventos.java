package es.ideas.holeventoproyecto.fragments.normalUser;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.adapter.NormalUserEventAdapter;
import es.ideas.holeventoproyecto.modelo.Evento;

public class ListaEventos extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private NormalUserEventAdapter adapter;
    private FirebaseUser user;
    private String nmButton;

    public ListaEventos() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_lista_eventos, container, false);


        nmButton = getStringByIdName(viewRoot.getContext(), "btnDesapuntarse");

        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("Eventos");

        Query eventos = eventosRef.orderBy("fechaEvento" ,Query.Direction.ASCENDING).whereArrayContains("Adhesiones", id);


        rv = (RecyclerView) viewRoot.findViewById(R.id.rvListaEventosUser);

        rv.setLayoutManager(new LinearLayoutManager(viewRoot.getContext()));

        FirestoreRecyclerOptions<Evento> options
                = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(eventos, Evento.class)
                .build();


        adapter = new NormalUserEventAdapter(options, viewRoot.getContext(), id, nmButton);
        rv.setAdapter(adapter);

        return viewRoot;
    }

    public static String getStringByIdName(Context context, String idName) {
        Resources res = context.getResources();
        return res.getString(res.getIdentifier(idName, "string", context.getPackageName()));
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