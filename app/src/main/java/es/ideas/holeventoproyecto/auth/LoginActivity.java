package es.ideas.holeventoproyecto.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import es.ideas.holeventoproyecto.BusinessMainActivity;
import es.ideas.holeventoproyecto.NormalUserMainActivity;
import es.ideas.holeventoproyecto.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private TextView tvReestablecer;
    private TextView tvRegistroEmpresa;

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseFirestore database;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_act);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        iniciarVista();

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPass.getText().toString();

                loguear(email, pass);

            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
                finish();
            }
        });

        tvRegistroEmpresa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterBusinessActivity.class));
                finish();
            }
        });

        tvReestablecer.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RecoveryPassActivity.class));
                finish();
            }
        });
    }

    private void iniciarVista() {
        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        btnIniciarSesion = findViewById(R.id.btnRegistro);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvReestablecer = findViewById(R.id.tvOlvidadoPass);
        tvRegistroEmpresa = findViewById(R.id.tvRegistroEmpresa);
        compruebaCampos();
    }

    private void compruebaCampos() {
        awesomeValidation.addValidation(LoginActivity.this, R.id.loginEmail,
                RegexTemplate.NOT_EMPTY, R.string.empty);
        awesomeValidation.addValidation(LoginActivity.this, R.id.loginPass,
                RegexTemplate.NOT_EMPTY, R.string.empty);
        awesomeValidation.addValidation(LoginActivity.this, R.id.loginEmail,
                Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
    }

    private void loguear(String email, String pass) {
        if (awesomeValidation.validate()) {
            auth.signInWithEmailAndPassword(email, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                compruebaTipoUsuario();

                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private void compruebaTipoUsuario() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        database.collection("UsuarioBusiness").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        startActivity(new Intent(LoginActivity.this,
                                BusinessMainActivity.class));
                    } else {
                        startActivity(new Intent(LoginActivity.this,
                                NormalUserMainActivity.class));
                    }
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "No se encuentra el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}