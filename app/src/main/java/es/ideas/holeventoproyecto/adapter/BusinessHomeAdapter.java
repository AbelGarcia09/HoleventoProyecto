package es.ideas.holeventoproyecto.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;
import es.ideas.holeventoproyecto.utils.BetterActivityResult;

public class BusinessHomeAdapter extends FirebaseRecyclerAdapter<Evento, BusinessHomeAdapter.eventoViewholder> {

    private Context cxt;

    public BusinessHomeAdapter(@NonNull FirebaseRecyclerOptions<Evento> options, Context cxt){
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
        Glide.with(cxt).load(img).into(holder.imagen);

    }

    @NonNull
    @Override
    public BusinessHomeAdapter.eventoViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new BusinessHomeAdapter.eventoViewholder(view);
    }

    class eventoViewholder
            extends RecyclerView.ViewHolder {
        TextView nombreEmpresa, contenido, plazasTotales, fechaEvento;
        ImageView imagen;
        ImageButton btnEliminar;
        Button bntApuntarse;



        public eventoViewholder(@NonNull View itemView)
        {
            super(itemView);

            nombreEmpresa= itemView.findViewById(R.id.tvNombreEmpresa);
            contenido = itemView.findViewById(R.id.tvContenido);
            plazasTotales = itemView.findViewById(R.id.tvPTotales);
            imagen = itemView.findViewById(R.id.ivFoto);
            bntApuntarse = itemView.findViewById(R.id.bntApuntarse);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
            fechaEvento = itemView.findViewById(R.id.tvFechaEvento);

        }
    }
}
