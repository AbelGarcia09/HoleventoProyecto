package es.ideas.holeventoproyecto.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.auth.RegisterBusinessActivity;
import es.ideas.holeventoproyecto.fragments.business.NuevoEvento;
import es.ideas.holeventoproyecto.fragments.business.Profile;
import es.ideas.holeventoproyecto.modelo.Evento;
import es.ideas.holeventoproyecto.modelo.Provincia;
import es.ideas.holeventoproyecto.utils.BetterActivityResult;

public class BusinessHomeAdapter extends FirebaseRecyclerAdapter<Evento, BusinessHomeAdapter.eventoViewholder> {

    private Context cxt;
    private String username;
    private DatabaseReference database;

    public BusinessHomeAdapter(@NonNull FirebaseRecyclerOptions<Evento> options, Context cxt, String username){
        super(options);
        this.cxt = cxt;
        this.username = username;
    }

    @Override
    protected void onBindViewHolder(@NonNull BusinessHomeAdapter.eventoViewholder holder,
                                    int position, @NonNull Evento model) {

        Uri img = Uri.parse(model.getImagen());
        Log.i("DATOS", "MODEL IMG -> "+ img);

        holder.nombreEmpresa.setText(username);
        holder.contenido.setText(model.getContenido());
        holder.plazasTotales.setText(model.getPlazasTotales()+"");
        holder.fechaEvento.setText(model.getFechaEvento());
        Glide.with(cxt).load(img).into(holder.imagen);

        holder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database = FirebaseDatabase.getInstance().getReference();
                AlertDialog.Builder b = new AlertDialog.Builder(v.getContext());
                b.setMessage("¿Desea eliminar el registro?")
                        .setPositiveButton("SI", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                database.child("Eventos").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            for (DataSnapshot datos : snapshot.getChildren()) {
                                                if(datos.getKey().toString().equals(model.getIdEvento()+"")) {
                                                    datos.getRef().removeValue();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.d("FALLO", error.getMessage());
                                    }
                                });

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
