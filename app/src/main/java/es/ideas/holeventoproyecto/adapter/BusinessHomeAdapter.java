package es.ideas.holeventoproyecto.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;
import es.ideas.holeventoproyecto.utils.BetterActivityResult;

public class BusinessHomeAdapter extends RecyclerView.Adapter<BusinessHomeAdapter.ViewHolder> {
    private Context cxt;
    private List<Evento> listaEventos;
    private View.OnClickListener vo;
    private EventoSelecccionado listener;
    private FirebaseUser user;

    private DatabaseReference database;



    public BusinessHomeAdapter(Context cxt, List<Evento> listaEventos,
                               EventoSelecccionado listener) {
        this.cxt = cxt;
        this.listaEventos = listaEventos;
        this.listener = listener;
    }


    @NonNull
    @Override
    public BusinessHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                             int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent,
                false);
        return new ViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull BusinessHomeAdapter.ViewHolder holder, int position) {

        database = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        String id =  user.getUid();
        String u =  database.child("UsuarioBusiness").child(id).child("nombreUsuario").getKey().toString();
        Log.i("DATOS", "Id usuario act -> "+id +" NOMBRE USUARIO -> "+ u);

        holder.tvNombreEmpresa.setText(u);

    }

    @Override
    public int getItemCount() {
        return listaEventos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        EventoSelecccionado listener;
        TextView tvNombreEmpresa;
        TextView tvContenido;
        TextView tvPlazasTotales;
        ImageView ivFoto;
        ImageButton btnEliminar;
        Button bntApuntarse;



        // Inicialización de cada elemento del RecyclerView
        public ViewHolder(View itemView, EventoSelecccionado listener) {
            super(itemView);
            tvNombreEmpresa = (TextView)itemView.findViewById(R.id.tvNombreEmpresa);
            tvContenido = (TextView)itemView.findViewById(R.id.tvContenido);
            tvPlazasTotales = (TextView)itemView.findViewById(R.id.tvPlazasTotales);
            ivFoto = (ImageView) itemView.findViewById(R.id.ivFoto);
            btnEliminar = (ImageButton) itemView.findViewById(R.id.btnEliminar);
            bntApuntarse = (Button) itemView.findViewById(R.id.bntApuntarse);
            this.listener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

        }
    }

    public interface EventoSelecccionado {
        void clickEvento(int clickedItem);
    }
}
