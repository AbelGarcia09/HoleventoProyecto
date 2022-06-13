package es.ideas.holeventoproyecto.fragments.business;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.bumptech.glide.Glide;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Query;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import es.ideas.holeventoproyecto.BusinessMainActivity;
import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.auth.LoginActivity;
import es.ideas.holeventoproyecto.modelo.Evento;
import es.ideas.holeventoproyecto.modelo.Provincia;
import es.ideas.holeventoproyecto.utils.BetterActivityResult;

public class NuevoEvento extends Fragment {


    public NuevoEvento() {
    }

    private String urlFoto = "empty";
    private View viewRoot;
    private ImageView ivFotoEvento;
    private EditText etFechaEvento;
    private EditText etDireccion;
    private EditText etPlazasTotales;
    private EditText etDescripcion;
    private EditText etProvincia;
    private DatePickerDialog datePickerDialog;
    private Button btnNuevoEvento;
    FirebaseUser user;
    private final BetterActivityResult<Intent, ActivityResult> activityLauncher =
            BetterActivityResult.registerActivityForResult(this);

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
                if (camposRellenos()){
                    nuevoEvento();
                    Toast.makeText(view.getContext(), R.string.evento_creado_correctamemte, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(viewRoot.getContext(), BusinessMainActivity.class));
                    try {
                        NuevoEvento.this.finalize();
                    } catch (Throwable e) {
                        e.printStackTrace();
                    }
                }
                else{
                    Toast.makeText(view.getContext(), R.string.empty_all, Toast.LENGTH_SHORT).show();
                }

            }
        });

        ivFotoEvento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subirFoto();

            }
        });

        return viewRoot;
    }

    private boolean camposRellenos() {

        return !etFechaEvento.getText().toString().equals("") &&
                !etDireccion.getText().toString().equals("") &&
                !etPlazasTotales.getText().toString().equals("") &&
                !etDescripcion.getText().toString().equals("");
    }

    private void iniciarVista() {
        initDatePicker();
        user = FirebaseAuth.getInstance().getCurrentUser();
        ivFotoEvento = (ImageView) viewRoot.findViewById(R.id.ivFotoEvento);
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
        database.child("UsuarioBusiness").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datos : snapshot.getChildren()) {
                        if (datos.getKey().equals(idUsuario)) {
                            String provincia = datos.child("idProvincia").getValue().toString();
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

    private String obtenerFechaActual() {
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd-MM-yyyy");
        String fecha = formatoFecha.format(cal.getTime());

        return fecha;
    }

    private String obtenerFechaFormateada(String fechaEvento) {
        String[] fechaSeparada = fechaEvento.split("-");

        return fechaSeparada[1] + "-" + fechaSeparada[0] + "-" + fechaSeparada[2];
    }

    private String fechaToString(int day, int month, int year) {
        if (day < 10 && month < 10) {
            return "0" + day + "-0" + month + "-" + year;
        } else if (day < 10 && month >= 10) {
            return "0" + day + "-" + month + "-" + year;
        } else if (day >= 10 && month < 10) {
            return day + "-0" + month + "-" + year;
        } else {
            return day + "-" + month + "-" + year;
        }
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
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

        datePickerDialog = new DatePickerDialog(viewRoot.getContext(), dateSetListener, year,
                month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private void openDatePicker(View view) {
        datePickerDialog.show();
    }

    private void nuevoEvento() {

        database = FirebaseDatabase.getInstance().getReference();
        database.child("Eventos").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                long cont = 0;
                if (snapshot.exists()) {
                    for (DataSnapshot datos : snapshot.getChildren()) {
                        if (cont <= Long.parseLong(datos.getKey())) {
                            cont = Long.parseLong(datos.getKey()) + 1;
                        }
                    }
                }else { cont =1;}

                long idEvento = cont;
                String idUsuario = user.getUid();
                String idProvincia = etProvincia.getText().toString();
                String direccion = etDireccion.getText().toString();
                String contenido = etDescripcion.getText().toString();
                String fechaEvento = obtenerFechaFormateada(etFechaEvento.getText().toString());
                String imagen = urlFoto;
                int plazasTotales = Integer.parseInt(etPlazasTotales.getText().toString());
                String fechaPublicacion = obtenerFechaActual();

                Evento evento = new Evento(contenido, direccion, fechaEvento, fechaPublicacion,
                        idEvento, idProvincia, idUsuario, imagen, plazasTotales);

                database.child("Eventos").child(idUsuario).child(String.valueOf(cont)).setValue(evento);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void subirFoto() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Uri datos = result.getData().getData();
                StorageReference storage =
                        FirebaseStorage.getInstance().getReference().child(user.getUid());

                final StorageReference foto = storage.child(datos.getLastPathSegment().toString());

                foto.putFile(datos).addOnSuccessListener(taskSnapshot -> foto.getDownloadUrl().addOnSuccessListener(uri -> {
                    urlFoto = uri.toString();
                }));
                Glide.with(viewRoot.getContext()).load(datos.toString()).into(ivFotoEvento);
            }
        });
    }

}