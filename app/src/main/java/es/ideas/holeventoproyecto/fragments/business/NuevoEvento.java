package es.ideas.holeventoproyecto.fragments.business;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.Evento;
import es.ideas.holeventoproyecto.modelo.UsuarioBusiness;

public class NuevoEvento extends Fragment {


    public NuevoEvento() {}

    private View viewRoot;
    private EditText etFechaEvento;
    private EditText etDireccion;
    private EditText etPlazasTotales;
    private EditText etDescripcion;
    private EditText etProvincia;
    private DatePickerDialog datePickerDialog;
    private Button btnNuevoEvento;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        viewRoot = inflater.inflate(R.layout.nuevo_evento_fragment, container, false);
        // Inflate the layout for this fragment

        iniciarVista();

        etFechaEvento.setFocusable(false);
        etFechaEvento.setHint(obtenerFechaActual());
        etFechaEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePicker(viewRoot);
            }
        });

        btnNuevoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevoEvento();
            }
        });

        return viewRoot;
    }

    private void iniciarVista(){
        initDatePicker();
        etFechaEvento = (EditText) viewRoot.findViewById(R.id.etFechaEvento);
        etDireccion = (EditText) viewRoot.findViewById(R.id.etDireccionEvento);
        etPlazasTotales = (EditText) viewRoot.findViewById(R.id.etPlazasTotales);
        etDescripcion = (EditText) viewRoot.findViewById(R.id.etDescripcion);
        etProvincia = (EditText) viewRoot.findViewById(R.id.etProvincia);
        btnNuevoEvento = (Button) viewRoot.findViewById(R.id.btnNuevoEvento);
        obtenProvincia();
    }

    private void obtenProvincia() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String idUsuario = user.getUid();
        database.child("UsuarioBusiness").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datos : snapshot.getChildren()) {
                        if (datos.getKey().equals(idUsuario)){
                            String provincia = datos.child("provincia").getValue().toString();
                            etProvincia.setText(provincia);
                            Log.i("DATOS", "dentro del for: " + provincia);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "No existe el usuario");
            }
        });
    }

    private String obtenerFechaActual(){
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatoFecha = new SimpleDateFormat("MM/dd/yyyy");
        String fecha = formatoFecha.format(cal.getTime());

        return fecha;
    }

    private String fechaToString(int day, int month, int year){
        if (day<10 && month<10){
            return "0"+day+"/0"+month+"/"+year;
        }
        else if (day<10 && month>=10){
            return "0"+day+"/"+month+"/"+year;
        }
        else if (day>=10 && month<10){
            return day+"/0"+month+"/"+year;
        }
        else{
            return day+"/"+month+"/"+year;
        }
    }

    private void initDatePicker(){
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month += 1;
                String date = fechaToString(day, month, year);
                etFechaEvento.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(viewRoot.getContext(), dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private void openDatePicker(View view){
        datePickerDialog.show();
    }

    private void nuevoEvento() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String idUsuario = user.getUid();
        String idProvincia = etProvincia.getText().toString();
        String direccion = etDireccion.getText().toString();
        String contenido = etDescripcion.getText().toString();
        String fechaEvento = etFechaEvento.getText().toString();
        String imagen = "";
        int plazasTotales = Integer.parseInt(etPlazasTotales.getText().toString());
        String fechaPublicacion = obtenerFechaActual();

        Evento evento = new Evento(idUsuario, idProvincia, direccion, contenido, fechaEvento, imagen, plazasTotales, fechaPublicacion);

        database.child("Eventos").child(evento.getIdEvento()+"").setValue(evento);
    }
}