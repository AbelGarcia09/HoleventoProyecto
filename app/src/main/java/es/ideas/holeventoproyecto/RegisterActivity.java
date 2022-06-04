package es.ideas.holeventoproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import es.ideas.holeventoproyecto.modelo.Provincia;

public class RegisterActivity extends AppCompatActivity {

    private EditText registerEmail, registerUsuario, registerTelefono, registerPass, registerPassR;
    private Spinner registerProvincia;
    private Button btnRegistro;

    private FirebaseAuth auth;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.register_business_user_act);

        registerEmail = findViewById(R.id.registerEmail);
        registerUsuario = findViewById(R.id.registerUsuario);
        registerTelefono = findViewById(R.id.registerTelefono);
        registerPass = findViewById(R.id.registerPass);
        registerPassR = findViewById(R.id.registerPassR);
        btnRegistro = findViewById(R.id.btnRegistro);
        registerProvincia = findViewById(R.id.provincia);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        cargarProvincias();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = registerEmail.getText().toString();
                String usuario = registerUsuario.getText().toString();
                String telefono = registerTelefono.getText().toString();
                String pass = registerPass.getText().toString();
                String passR = registerPassR.getText().toString();

                if (compruebaVacio(email, usuario, telefono, pass, passR)) {
                    try {
                        registrar(email, pass);
                        Toast.makeText(RegisterActivity.this, "Usuario registrado.",
                                Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        finish();
                    } catch (Exception e) {e.toString();}
                }
            }
        });
    }

    private void registrar(String email, String pass) {
        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Log.d("FALLO", "onComplete: weak_password");
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Introduce una contraseña de al menos 6 carácteres",
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Log.d("FALLO", "onComplete: malformed_email");
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Correo electrónico invalido",
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Log.d("FALLO", "onComplete: exist_email");
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Ya existe una cuenta con este correo electrónico",
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (Exception e) {
                                Log.d("FALLO", "onComplete: " + e.getMessage());
                                Toast.makeText(
                                        RegisterActivity.this,
                                        "Ha ocurrido un error inesperado",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
                });
    }

    private boolean compruebaVacio(String email, String usuario, String telefono,
                                   String pass, String passR) {
        if (email.isEmpty() && usuario.isEmpty() && telefono.isEmpty() && pass.isEmpty() && passR.isEmpty()) {
            Toast.makeText(this, "Rellena los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty() || usuario.isEmpty() || telefono.isEmpty() || pass.isEmpty() || passR.isEmpty()) {
            if (email.isEmpty()) {
                registerEmail.setError("Campo vacío");
            }
            if (usuario.isEmpty()) {
                registerUsuario.setError("Campo vacío");
            }
            if (telefono.isEmpty()) {
                registerTelefono.setError("Campo vacío");
            }
            if (pass.isEmpty()) {
                registerPass.setError("Campo vacío");
            }
            if (passR.isEmpty()) {
                registerPassR.setError("Campo vacío");
            }
            if (!pass.isEmpty() && !passR.isEmpty()) {
                if (!pass.equals(passR)) {
                    registerPassR.setError("No coincide la contraseña");
                }
            }
            Toast.makeText(this, "Hay algunos campos vacíos", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void cargarProvincias() {
        List<Provincia> provincias = new ArrayList<>();
        database.child("Provincias").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datos : snapshot.getChildren()) {
                        String id = datos.getKey();
                        String nombre = datos.getValue().toString();
                        provincias.add(new Provincia(id, nombre));
                    }

                    ArrayAdapter<Provincia> arrayAdapter =
                            new ArrayAdapter<>(RegisterActivity.this,
                                    android.R.layout.simple_dropdown_item_1line, provincias);
                    registerProvincia.setAdapter(arrayAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("FALLO", error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}