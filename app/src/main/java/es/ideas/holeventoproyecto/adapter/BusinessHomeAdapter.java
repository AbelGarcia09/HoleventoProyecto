package es.ideas.holeventoproyecto.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;

public class BusinessHomeAdapter extends FirestoreRecyclerAdapter<Evento, BusinessHomeAdapter.eventoViewholder> {

    private Context cxt;
    private FirebaseFirestore database;

    public BusinessHomeAdapter(@NonNull FirestoreRecyclerOptions<Evento> options, Context cxt){
        super(options);
        this.cxt = cxt;
    }

    @Override
    protected void onBindViewHolder(@NonNull BusinessHomeAdapter.eventoViewholder holder,
                                    int position, @NonNull Evento model) {

        Uri img = Uri.parse(model.getImagen());
        Log.i("DATOS", "MODEL IMG -> "+ img);

        holder.nombreEmpresa.setText(model.getNombreUsuario());
        holder.contenido.setText(model.getContenido());
        holder.plazasTotales.setText(model.getPlazasTotales()+"");
        holder.fechaEvento.setText(model.getFechaEvento());
        holder.direccion.setText(model.getDireccion());
        Glide.with(cxt).load(img).into(holder.imagen);

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseFirestore.getInstance();
                AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                b.setMessage("¿Desea eliminar el registro?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                database.collection("Eventos").document(model.getIdEvento()+"").delete();

                            }
                        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
            }
        });

    }

    @NonNull
    @Override
    public BusinessHomeAdapter.eventoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event_bussines, parent, false);
        return new BusinessHomeAdapter.eventoViewholder(view);
    }

    class eventoViewholder
            extends RecyclerView.ViewHolder {
        TextView nombreEmpresa, contenido, plazasTotales, fechaEvento, direccion;
        ImageView imagen;
        ImageButton btnEliminar;



        public eventoViewholder(@NonNull View itemView)
        {
            super(itemView);

            nombreEmpresa= itemView.findViewById(R.id.tvNombreEmpresa);
            contenido = itemView.findViewById(R.id.tvContenido);
            plazasTotales = itemView.findViewById(R.id.tvPTotales);
            imagen = itemView.findViewById(R.id.ivFoto);
            direccion = itemView.findViewById(R.id.tvDireccion);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            fechaEvento = itemView.findViewById(R.id.tvFechaEvento);

        }
    }
}
