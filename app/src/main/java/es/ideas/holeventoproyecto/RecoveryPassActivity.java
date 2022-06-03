package es.ideas.holeventoproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class RecoveryPassActivity extends AppCompatActivity {

    private Button btnRecovery;
    private EditText email;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.rec_pass);

        btnRecovery = findViewById(R.id.btnReestablecer);
        email = findViewById(R.id.recoverEmail);
        auth = FirebaseAuth.getInstance();

        btnRecovery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                auth.sendPasswordResetEmail(email.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                Toast.makeText(RecoveryPassActivity.this, "Comprueba tu correo electrónico.", Toast.LENGTH_LONG).show();
                                startActivity(new Intent(RecoveryPassActivity.this, LoginActivity.class));
                                finish();
                            }

                        }).addOnFailureListener(new OnFailureListener() {
                            public void onFailure(Exception e) {
                                Toast.makeText(RecoveryPassActivity.this, "El correo no está registrado.", Toast.LENGTH_LONG).show();
                        }
            });
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RecoveryPassActivity.this, LoginActivity.class));
        finish();
    }
}