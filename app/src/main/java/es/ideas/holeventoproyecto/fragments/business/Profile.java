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
    private BusinessHomeAdapter hAdaptador;
    private Toast mToast;

    public Profile() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewRoot = inflater.inflate(R.layout.fragment_profile, container, false);

        rv = (RecyclerView) viewRoot.findViewById(R.id.rvBusinessProf);

        DatabaseReference database =
                FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();
        Log.i("DATOS", "ID User AaAAAAAA -> "+id);

        /*
        database.child("Eventos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String id =  user.getUid();
                List<Evento> listaEventos = new ArrayList<>();

                if (snapshot.exists()){

                    for (DataSnapshot datos : snapshot.getChildren()) {
                        long cont = Long.parseLong(datos.getKey());
                        if (datos.child(String.valueOf(cont)).child("idUsuario").getValue().equals(id)){

                            String idUsuario = datos.child("Eventos").child(String.valueOf(cont)).child("idUsuario").getValue().toString();
                            String idProvincia = datos.child("Eventos").child(String.valueOf(cont)).child("idProvincia").getValue().toString();
                            String direccion = datos.child("Eventos").child(String.valueOf(cont)).child("direccion").getValue().toString();
                            String contenido = datos.child("Eventos").child(String.valueOf(cont)).child("contenido").getValue().toString();
                            String fechaEvento = datos.child("Eventos").child(String.valueOf(cont)).child("fechaEvento").getValue().toString();
                            String imagen = datos.child("Eventos").child(String.valueOf(cont)).child("imagen").getValue().toString();
                            int plazasTotales =(int) datos.child("Eventos").child(String.valueOf(cont)).child("plazasTotales").getValue();
                            String fechaPublicacion = datos.child("Eventos").child(String.valueOf(cont)).child("fechaPublicacion").getValue().toString();
                            listaEventos.add(new Evento(cont, idUsuario, idProvincia, direccion, contenido, fechaEvento, imagen, plazasTotales, fechaPublicacion));
                        }
                    }


                    rv.setLayoutManager(new LinearLayoutManager(getContext()));
                    hAdaptador = new BusinessHomeAdapter(getContext(), listaEventos, this::clickEvento);
                    rv.setAdapter(hAdaptador);

                }
            }

            public void clickEvento(int clickedItem) {
                if (mToast!=null) mToast.cancel();
                String msg = "Lista #";
                mToast = Toast.makeText(getContext(), msg + (clickedItem+1), Toast.LENGTH_LONG);
                mToast.show();
                Log.d("TAG", "listenerClick: cliked."+clickedItem);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

         */

        return viewRoot;
    }


}