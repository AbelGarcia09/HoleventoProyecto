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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;

public class NormalUserEventAdapter extends FirebaseRecyclerAdapter<Evento, NormalUserEventAdapter.eventoViewholder> {

    private Context cxt;
    private String username;
    private DatabaseReference database;

    public NormalUserEventAdapter(@NonNull FirebaseRecyclerOptions<Evento> options, Context cxt){
        super(options);
        this.cxt = cxt;
    }

    @Override
    protected void onBindViewHolder(@NonNull NormalUserEventAdapter.eventoViewholder holder,
                                    int position, @NonNull Evento model) {

        Uri img = Uri.parse(model.getImagen());
        Log.i("DATOS", "MODEL IMG -> "+ img);

        //holder.nombreEmpresa.setText(username);
        holder.contenido.setText(model.getContenido());
        holder.plazasTotales.setText(model.getPlazasTotales()+"");
        holder.fechaEvento.setText(model.getFechaEvento());
        holder.direccion.setText(model.getDireccion());
        Glide.with(cxt).load(img).into(holder.imagen);

        holder.btnApuntarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance().getReference();
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
