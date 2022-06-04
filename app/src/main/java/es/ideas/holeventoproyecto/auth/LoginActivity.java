package es.ideas.holeventoproyecto.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import es.ideas.holeventoproyecto.MainActivity;
import es.ideas.holeventoproyecto.R;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private TextView tvReestablecer;
    private TextView tvRegistroEmpresa;

    private FirebaseAuth auth;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_act);

        auth = FirebaseAuth.getInstance();
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
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Credenciales incorrectas",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}