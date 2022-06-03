package es.ideas.holeventoproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.ktx.Firebase;

public class LoginActivity extends AppCompatActivity {

    private EditText loginEmail;
    private EditText loginPass;
    private Button btnIniciarSesion;
    private Button btnRegistrarse;
    private TextView tvReestablecer;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.login_act);

        auth = FirebaseAuth.getInstance();

        loginEmail = findViewById(R.id.loginEmail);
        loginPass = findViewById(R.id.loginPass);
        btnIniciarSesion = findViewById(R.id.btnRegistro);
        btnRegistrarse = findViewById(R.id.btnRegistrarse);
        tvReestablecer = findViewById(R.id.tvOlvidadoPass);

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String email = loginEmail.getText().toString();
                String pass = loginPass.getText().toString();

                if(compruebaVacio(email, pass)) {
                    loguear(email, pass);
                } else {
                    Toast.makeText(LoginActivity.this, "Rellena los campos", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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

    private void loguear(String email, String pass) {

        auth.signInWithEmailAndPassword(email, pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Credenciales incorrectas", Toast.LENGTH_LONG).show();
                        }
                    }
        });
    }

    private boolean compruebaVacio(String email, String pass) {
        if (email.isEmpty() && pass.isEmpty()) {
            Toast.makeText(this, "Rellena los campos", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (email.isEmpty()) {
            loginEmail.setError("Campo vacío");
            return false;
        }
        if (pass.isEmpty()) {
            loginPass.setError("Campo vacío");
            return false;
        }
        return true;
    }
}