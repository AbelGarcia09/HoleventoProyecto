package es.ideas.holeventoproyecto.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.UsuarioNormal;
import es.ideas.holeventoproyecto.utils.Utils;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText registerEmail, registerUsuario, registerPass, registerPassR;
    private Button btnRegistro;

    private FirebaseAuth auth;
    private DatabaseReference database;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.register_normal_user_act);

        // Cargar librerías necesarias.
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        iniciarVista();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmail.getText().toString();
                String pass = registerPass.getText().toString();

                if (awesomeValidation.validate()) {
                    registrar(email, pass);
                }
            }
        });
    }

    private void iniciarVista() {
        registerEmail = findViewById(R.id.registerUserEmail);
        registerUsuario = findViewById(R.id.registerUserUsuario);
        registerPass = findViewById(R.id.registerUserPass);
        registerPassR = findViewById(R.id.registerUserPassR);
        btnRegistro = findViewById(R.id.btnUserRegistro);
        comprobarCampos();
    }

    private void comprobarCampos() {
        // Comprueba que los campos no están vacíos.
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserEmail,
                RegexTemplate.NOT_EMPTY, R.string.empty);
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserUsuario,
                RegexTemplate.NOT_EMPTY, R.string.empty);
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserPass,
                RegexTemplate.NOT_EMPTY, R.string.empty);
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserPassR,
                RegexTemplate.NOT_EMPTY, R.string.empty);


        //Comprueba que los valores introducidos en los campos son valores válidos.
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserEmail,
                Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserPass,
                ".{6,}", R.string.invalid_password);
        awesomeValidation.addValidation(RegisterUserActivity.this, R.id.registerUserPassR,
                R.id.registerUserPass, R.string.contrasenya_no_coincide);
    }

    private void registrar(String email, String pass) {

        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            insertaUsuario();
                            Toast.makeText(RegisterUserActivity.this, "Usuario registrado.",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(RegisterUserActivity.this,
                                    LoginActivity.class));
                            finish();
                        } else {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Log.d("FALLO", "onComplete: exist_email");
                                Toast.makeText(
                                        RegisterUserActivity.this,
                                        "Ya existe una cuenta con este correo electrónico",
                                        Toast.LENGTH_SHORT
                                ).show();
                            } catch (Exception e) {
                                Log.d("FALLO", "onComplete: " + e.getMessage());
                                Toast.makeText(
                                        RegisterUserActivity.this,
                                        "Ha ocurrido un error inesperado",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        }
                    }
                });

    }

    private void insertaUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        String email = registerEmail.getText().toString();
        String idUsuario = uid;
        String nombreUsuario = registerUsuario.getText().toString();
        String password = registerPass.getText().toString();

        UsuarioNormal usuario = new UsuarioNormal(email, idUsuario, nombreUsuario, password);

        database.child("UsuarioNormal").child(usuario.getIdUsuario()).setValue(usuario);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}