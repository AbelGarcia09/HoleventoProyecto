package es.ideas.holeventoproyecto.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;

public class NormalUserEventAdapter extends FirestoreRecyclerAdapter<Evento, NormalUserEventAdapter.eventoViewholder> {

    private Context cxt;
    private FirebaseFirestore database;
    private String idUsuario;

    public NormalUserEventAdapter(@NonNull FirestoreRecyclerOptions<Evento> options, Context cxt, String idUsurio){
        super(options);
        this.cxt = cxt;
        this.idUsuario = idUsurio;
    }

    @Override
    protected void onBindViewHolder(@NonNull NormalUserEventAdapter.eventoViewholder holder,
                                    int position, @NonNull Evento model) {

        Uri img = Uri.parse(model.getImagen());
        Log.i("DATOS", "MODEL IMG -> "+ img);

        holder.nombreEmpresa.setText(model.getNombreUsuario());
        holder.contenido.setText(model.getContenido());
        holder.plazasTotales.setText(model.getPlazasTotales()+"");
        holder.fechaEvento.setText(model.getFechaEvento());
        holder.direccion.setText(model.getDireccion());
        Glide.with(cxt).load(img).into(holder.imagen);

        holder.btnApuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseFirestore.getInstance();
                database.collection("Adhesiones")
                        .document(model.getIdEvento()+"").get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()){
                                    if(task.getResult().exists() && task.getResult().get("idUsuario").equals(idUsuario)){
                                        task.getResult().getReference().delete();
                                    }else {
                                        Map<String, Object> adhesion = new HashMap<>();
                                        adhesion.put("idUsuario", idUsuario);
                                        database.collection("Adhesiones").document(model.getIdEvento()+"").set(adhesion);
                                    }
                                }else{
                                    Log.e("Firebase","Se ha producido un error al realizar el get",task.getException());
                                }
                            }
                        });

            }
        });

    }

    @NonNull
    @Override
    public NormalUserEventAdapter.eventoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_user, parent, false);
        return new NormalUserEventAdapter.eventoViewholder(view);
    }

    class eventoViewholder
            extends RecyclerView.ViewHolder {
        TextView nombreEmpresa, contenido, plazasTotales, fechaEvento, direccion;
        ImageView imagen;
        Button btnApuntarse;



        public eventoViewholder(@NonNull View itemView)
        {
            super(itemView);

            nombreEmpresa= itemView.findViewById(R.id.tvNombreEmpresaUser);
            contenido = itemView.findViewById(R.id.tvContenidoUser);
            plazasTotales = itemView.findViewById(R.id.tvPTotalesUser);
            imagen = itemView.findViewById(R.id.ivFotoUser);
            direccion = itemView.findViewById(R.id.tvDireccionUser);
            btnApuntarse = itemView.findViewById(R.id.btnApuntarse);
            fechaEvento = itemView.findViewById(R.id.tvFechaEventoUser);

        }
    }
}
