package es.ideas.holeventoproyecto.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;

public class NormalUserEventAdapter extends FirestoreRecyclerAdapter<Evento,
        NormalUserEventAdapter.eventoViewholder> {

    private Context cxt;
    private FirebaseFirestore database;
    private String idUsuario;
    private String nMButton;

    public NormalUserEventAdapter(@NonNull FirestoreRecyclerOptions<Evento> options, Context cxt,
                                  String idUsurio, String nMButton) {
        super(options);
        this.cxt = cxt;
        this.idUsuario = idUsurio;
        this.nMButton = nMButton;
    }

    @Override
    protected void onBindViewHolder(@NonNull NormalUserEventAdapter.eventoViewholder holder,
                                    int position, @NonNull Evento model) {

        Uri img = Uri.parse(model.getImagen());
        Log.i("DATOS", "MODEL IMG -> " + img);

        holder.nombreEmpresa.setText(model.getNombreUsuario());
        holder.contenido.setText(model.getContenido());
        holder.plazasTotales.setText(model.getPlazasTotales() + "");
        holder.fechaEvento.setText(model.getFechaEvento());
        holder.direccion.setText(model.getDireccion());
        Glide.with(cxt).load(img).into(holder.imagen);
        holder.btnApuntarse.setText(nMButton);

        holder.btnApuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseFirestore.getInstance();
                database.collection("Eventos")
                        .document(model.getIdEvento() + "").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isComplete()) {
                                    List<String> users = (List<String>) task.getResult().get("Adhesiones");
                                    int total = Integer.parseInt(task.getResult().get("plazasTotales").toString());
                                    int cont = Integer.parseInt(task.getResult().get("plazasTotalesCont").toString());
                                    if (users != null) {
                                        if (users.contains(idUsuario)) {
                                            users.remove(idUsuario);
                                            database = FirebaseFirestore.getInstance();
                                            database.collection("Eventos").document(model.getIdEvento() + "").update("Adhesiones", users);
                                            if ( total == cont && task.getResult().get("completo").equals("true")){
                                                database.collection("Eventos").document(model.getIdEvento() + "").update("completo", "false");
                                            }
                                            database.collection("Eventos").document(model.getIdEvento() + "").update("plazasTotalesCont", FieldValue.increment(-1));
                                        } else {
                                            if (task.getResult().get("completo").equals("false")) {
                                                users.add(idUsuario);
                                                database.collection("Eventos").document(model.getIdEvento() + "").update("Adhesiones", users);
                                                database.collection("Eventos").document(model.getIdEvento() + "").update("plazasTotalesCont", FieldValue.increment(1));
                                                if ( total == cont+1){
                                                    database.collection("Eventos").document(model.getIdEvento() + "").update("completo", "true");
                                                }
                                            }
                                            else{
                                                Toast.makeText(cxt, R.string.evento_completo, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else {
                                        if (task.getResult().get("completo").equals("false")) {
                                            users = new ArrayList<>();
                                            users.add(idUsuario);
                                            database.collection("Eventos").document(model.getIdEvento() + "").update("Adhesiones", users);
                                            database.collection("Eventos").document(model.getIdEvento() + "").update("plazasTotalesCont", FieldValue.increment(1));
                                            if ( total == cont+1){
                                                database.collection("Eventos").document(model.getIdEvento() + "").update("completo", "true");
                                            }
                                        }else{
                                            Toast.makeText(cxt, R.string.evento_completo, Toast.LENGTH_SHORT).show();
                                        }
                                    }





                                } else {
                                    Log.e("Firebase", "Se ha producido un error al realizar el " +
                                            "get", task.getException());
                                }
                            }
                        });

            }
        });


        try {
            database = FirebaseFirestore.getInstance();
            database.collection("Eventos").document(model.getIdEvento() + "").addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot value,
                                    @Nullable FirebaseFirestoreException error) {
                    if (value.exists()) {
                        database = FirebaseFirestore.getInstance();
                        database.collection("Eventos").document(model.getIdEvento() + "").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                holder.cont.setText(task.getResult().get("plazasTotalesCont").toString());
                                if (task.getResult().get("plazasTotales").equals(task.getResult().get("plazasTotalesCont"))) {
                                    holder.btnApuntarse.setBackgroundColor(Color.parseColor("#ff0000"));
                                }else{
                                    holder.btnApuntarse.setBackgroundColor(Color.parseColor("#489fb5"));
                                }

                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
        }

    }


    @NonNull
    @Override
    public NormalUserEventAdapter.eventoViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                      int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_user, parent, false);
        return new NormalUserEventAdapter.eventoViewholder(view);
    }

    class eventoViewholder
            extends RecyclerView.ViewHolder {
        TextView nombreEmpresa, contenido, plazasTotales, fechaEvento, direccion, cont;
        ImageView imagen;
        Button btnApuntarse;


        public eventoViewholder(@NonNull View itemView) {
            super(itemView);

            nombreEmpresa = itemView.findViewById(R.id.tvNombreEmpresaUser);
            contenido = itemView.findViewById(R.id.tvContenidoUser);
            plazasTotales = itemView.findViewById(R.id.tvPTotalesUser);
            imagen = itemView.findViewById(R.id.ivFotoUser);
            direccion = itemView.findViewById(R.id.tvDireccionUser);
            btnApuntarse = itemView.findViewById(R.id.btnApuntarse);
            fechaEvento = itemView.findViewById(R.id.tvFechaEventoUser);
            cont = itemView.findViewById(R.id.tvContador);

        }
    }
}
