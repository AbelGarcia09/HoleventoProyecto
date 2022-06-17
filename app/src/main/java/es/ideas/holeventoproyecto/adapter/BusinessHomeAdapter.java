package es.ideas.holeventoproyecto.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;

public class BusinessHomeAdapter extends FirestoreRecyclerAdapter<Evento,
        BusinessHomeAdapter.eventoViewholder> {

    private Context cxt;
    private FirebaseFirestore database;

    public BusinessHomeAdapter(@NonNull FirestoreRecyclerOptions<Evento> options, Context cxt) {
        super(options);
        this.cxt = cxt;
    }

    @Override
    protected void onBindViewHolder(@NonNull BusinessHomeAdapter.eventoViewholder holder,
                                    int position, @NonNull Evento model) {

        Uri img = Uri.parse(model.getImagen());
        Log.i("DATOS", "MODEL IMG -> " + img);

        holder.nombreEmpresa.setText(model.getNombreUsuario());
        holder.contenido.setText(model.getContenido());
        holder.plazasTotales.setText(model.getPlazasTotales() + "");
        holder.fechaEvento.setText(model.getFechaEvento());
        holder.direccion.setText(model.getDireccion());
        Glide.with(cxt).load(img).into(holder.imagen);

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
                            }
                        });
                    }
                }
            });

        } catch (Exception e) {
        }

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminaEvento(model.getIdEvento(), v);
            }
        });

        holder.btnParticipantes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                muestraParticipantes(model.getIdEvento(), holder.cont.getText().toString());
            }
        });

    }

    @NonNull
    @Override
    public BusinessHomeAdapter.eventoViewholder onCreateViewHolder(@NonNull ViewGroup parent,
                                                                   int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_bussines, parent, false);
        return new BusinessHomeAdapter.eventoViewholder(view);
    }

    private void eliminaEvento(long idEvento, View v) {
        database = FirebaseFirestore.getInstance();
        AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
        b.setMessage(R.string.eliminar_registro)
                .setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        database.collection("Eventos").document(idEvento + "").delete();

                    }
                }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void muestraParticipantes(long idEvento, String cont) {
        int contador = Integer.parseInt(cont);
        if (contador > 0) {


            database = FirebaseFirestore.getInstance();
            database.collection("Eventos")
                    .document(idEvento + "").get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isComplete()) {
                                final List<String> users =
                                        (List<String>) task.getResult().get("Adhesiones");
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("UsuarioNormal").get()
                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    List<String> participantes =
                                                            new ArrayList<>();
                                                    for (QueryDocumentSnapshot datos :
                                                            task.getResult()) {
                                                        for (String u : users) {
                                                            if (datos.get("idUsuario").equals(u)) {
                                                                participantes.add(datos.get(
                                                                        "nombreUsuario").toString() + " - " + datos.get("email"));
                                                            }
                                                        }
                                                    }
                                                    AlertDialog.Builder builder =
                                                            new AlertDialog.Builder(cxt);
                                                    builder.setTitle(R.string.lista_asistentes);

                                                    ArrayAdapter<String> dataAdapter =
                                                            new ArrayAdapter<String>(cxt,
                                                                    android.R.layout.simple_dropdown_item_1line, participantes);
                                                    builder.setAdapter(dataAdapter,
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                }
                                                            });
                                                    AlertDialog dialog = builder.create();
                                                    dialog.show();

                                                }
                                            }
                                        });
                            }

                        }

                    });

        } else {
            Toast.makeText(cxt, R.string.empty_participantes, Toast.LENGTH_LONG).show();
        }
    }

    class eventoViewholder
            extends RecyclerView.ViewHolder {
        TextView nombreEmpresa, contenido, plazasTotales, fechaEvento, direccion, cont;
        ImageView imagen;
        ImageButton btnEliminar;
        Button btnParticipantes;


        public eventoViewholder(@NonNull View itemView) {
            super(itemView);

            nombreEmpresa = itemView.findViewById(R.id.tvNombreEmpresa);
            contenido = itemView.findViewById(R.id.tvContenido);
            plazasTotales = itemView.findViewById(R.id.tvPTotales);
            imagen = itemView.findViewById(R.id.ivFoto);
            direccion = itemView.findViewById(R.id.tvDireccion);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            fechaEvento = itemView.findViewById(R.id.tvFechaEvento);
            cont = itemView.findViewById(R.id.tvPocupadasBussines);
            btnParticipantes = itemView.findViewById(R.id.btnParticipantes);

        }
    }
}
