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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import es.ideas.holeventoproyecto.R;
import es.ideas.holeventoproyecto.modelo.UsuarioNormal;

public class RegisterUserActivity extends AppCompatActivity {

    private EditText registerEmail, registerUsuario, registerPass, registerPassR,
            existeUser;
    private Button btnRegistro;

    private FirebaseAuth auth;
    private FirebaseFirestore database;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.register_normal_user_act);

        // Cargar librerías necesarias.
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        iniciarVista();

        btnRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registerEmail.getText().toString();
                String pass = registerPass.getText().toString();

                existeNombreUsuario(email, pass);

            }
        });
    }

    private void iniciarVista() {
        registerEmail = findViewById(R.id.registerUserEmail);
        registerUsuario = findViewById(R.id.registerUserUsuario);
        registerPass = findViewById(R.id.registerUserPass);
        registerPassR = findViewById(R.id.registerUserPassR);
        btnRegistro = findViewById(R.id.btnUserRegistro);
        existeUser = findViewById(R.id.existeUserNormal);
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

        Log.i("DATOS", "NmUserEXIST: " + existeUser.getText().toString());
        if (existeUser.getText().toString().equals("false")) {
            auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                insertaUsuario();
                                agregarNombreUsuario();
                                Toast.makeText(RegisterUserActivity.this, R.string.registrado,
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
                                            R.string.existe_correo,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } catch (Exception e) {
                                    Log.d("FALLO", "onComplete: " + e.getMessage());
                                    Toast.makeText(
                                            RegisterUserActivity.this,
                                            R.string.err_inesperado,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                }
                            }
                        }
                    });
        }

    }

    private void insertaUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(registerUsuario.getText().toString())
                .build();
        user.updateProfile(profileUpdates);

        String email = registerEmail.getText().toString();
        String idUsuario = uid;
        String nombreUsuario = registerUsuario.getText().toString();
        String password = registerPass.getText().toString();

        UsuarioNormal usuario = new UsuarioNormal(email, idUsuario, nombreUsuario, password);

        database.collection("UsuarioNormal").document(usuario.getIdUsuario()).set(usuario);
    }

    private void agregarNombreUsuario() {
        database = FirebaseFirestore.getInstance();
        database.collection("NombresUsuario")
                .document("lista").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isComplete()) {
                            List<String> usernames = (List<String>) task.getResult().get("Nombres");
                            String nUser = registerUsuario.getText().toString();
                            if (usernames != null) {
                                //No existe nombre usuario
                                existeUser.setText("false");
                                usernames.add(nUser);
                                database.collection("NombresUsuario")
                                        .document("lista").update("Nombres", usernames);

                            } else {
                                //No existe nombre usuario ni la lista
                                usernames = new ArrayList<>();
                                usernames.add(nUser);
                                database.collection("NombresUsuario")
                                        .document("lista").update("Nombres", usernames);
                            }
                        } else {
                            Log.e("Firebase", "Se ha producido un error al realizar el " +
                                    "get", task.getException());
                        }
                    }
                });
    }

    private void existeNombreUsuario(String email, String pass) {

        database = FirebaseFirestore.getInstance();
        database.collection("NombresUsuario")
                .document("lista").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isComplete()) {
                            List<String> usernames = (List<String>) task.getResult().get("Nombres");
                            String nUser = registerUsuario.getText().toString();

                            if (usernames != null) {
                                if (usernames.contains(nUser)) {
                                    //Existe Nombre Usuario
                                    existeUser.setText("true");
                                    Toast.makeText(
                                            RegisterUserActivity.this,
                                            R.string.exite_nombre_user,
                                            Toast.LENGTH_SHORT
                                    ).show();
                                } else {
                                    //No existe nombre usuario ni la lista
                                    existeUser.setText("false");
                                    if (awesomeValidation.validate()) {
                                        registrar(email, pass);
                                    }
                                }
                            } else {
                                //No existe nombre usuario ni la lista
                                existeUser.setText("false");
                                if (awesomeValidation.validate()) {
                                    registrar(email, pass);
                                }
                            }
                        } else {
                            Log.e("Firebase", "Se ha producido un error al realizar el " +
                                    "get", task.getException());
                        }
                    }
                });

        try {
            Thread.sleep(150);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}