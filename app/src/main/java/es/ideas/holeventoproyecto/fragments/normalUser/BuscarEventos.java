package es.ideas.holeventoproyecto.fragments.normalUser;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.adapter.NormalUserEventAdapter;
import es.ideas.holeventoproyecto.auth.RegisterBusinessActivity;
import es.ideas.holeventoproyecto.modelo.Evento;
import es.ideas.holeventoproyecto.modelo.Provincia;

public class BuscarEventos extends Fragment {

    private View viewRoot;
    private RecyclerView rv;
    private FirebaseUser user;
    private ImageButton btnBuscar;
    private Spinner spProvincias;
    private FirebaseFirestore database;
    private NormalUserEventAdapter adapter;
    private String nmButton;
    private String idProvincia = "0";
    private Query eventos;
    private EditText itemPosition;

    public BuscarEventos() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_buscar_eventos, container, false);

        rv = viewRoot.findViewById(R.id.rvBuscarProvinciasUser);
        spProvincias = viewRoot.findViewById(R.id.buscarProvincias);
        btnBuscar = viewRoot.findViewById(R.id.ntnBuscarProvincia);
        rv = (RecyclerView) viewRoot.findViewById(R.id.rvBuscarProvinciasUser);
        itemPosition = (EditText) viewRoot.findViewById(R.id.itemPosition);

        nmButton = getStringByIdName(viewRoot.getContext(), "btnApuntarse");


        cargarProvincias();

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemPosition.setText(spProvincias.getSelectedItemPosition()+"");
                idProvincia = itemPosition.getText().toString();
                cargarAdapter(idProvincia);
            }
        });

        idProvincia = itemPosition.getText().toString();


        return viewRoot;
    }

    private void cargarAdapter(String idProvincia) {
        user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eventosRef = db.collection("Eventos");

        eventos =
                eventosRef.orderBy("fechaEvento", Query.Direction.ASCENDING).whereEqualTo(
                        "idProvincia", idProvincia);


        rv.setLayoutManager(new LinearLayoutManager(viewRoot.getContext()));

        FirestoreRecyclerOptions<Evento> options
                = new FirestoreRecyclerOptions.Builder<Evento>()
                .setQuery(eventos, Evento.class)
                .build();


        adapter = new NormalUserEventAdapter(options, viewRoot.getContext(), id, nmButton);
        rv.setAdapter(adapter);
        adapter.startListening();
    }

    public static String getStringByIdName(Context context, String idName) {
        Resources res = context.getResources();
        return res.getString(res.getIdentifier(idName, "string", context.getPackageName()));
    }

    private void cargarProvincias() {
        database = FirebaseFirestore.getInstance();
        List<Provincia> provincias = new ArrayList<>();


        database.collection("Provincias").document("provincia").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot document = task.getResult();
                List<String> provincias = (List<String>) document.get("nombre");

                ArrayAdapter<String> arrayAdapter =
                        new ArrayAdapter<>(getActivity(),
                                android.R.layout.simple_dropdown_item_1line, provincias);
                spProvincias.setAdapter(arrayAdapter);
            }
        });

    }


    // Function to tell the app to stop getting
    // data from database on stopping of the activity
    @Override
    public  void onStop()
    {
        super.onStop();
        if (adapter != null){
            adapter.stopListening();
        }
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (adapter != null){
            adapter.notifyDataSetChanged();
        }
    }

}