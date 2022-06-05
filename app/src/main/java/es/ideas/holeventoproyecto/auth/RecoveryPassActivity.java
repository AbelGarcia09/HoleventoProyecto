package es.ideas.holeventoproyecto.auth;

import android.content.Intent;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import es.ideas.holeventoproyecto.R;

public class RecoveryPassActivity extends AppCompatActivity {

    private Button btnRecovery;
    private EditText email;
    private FirebaseAuth auth;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.rec_pass);

        auth = FirebaseAuth.getInstance();
        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        iniciarVista();

        btnRecovery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (awesomeValidation.validate()) {
                    auth.sendPasswordResetEmail(email.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    Toast.makeText(RecoveryPassActivity.this, "Comprueba tu " +
                                            "correo electrónico.", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(RecoveryPassActivity.this,
                                            LoginActivity.class));
                                    finish();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                public void onFailure(Exception e) {
                                    Toast.makeText(RecoveryPassActivity.this, "El correo no está " +
                                            "registrado.", Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });


    }

    private void iniciarVista() {
        btnRecovery = findViewById(R.id.btnReestablecer);
        email = findViewById(R.id.recoverEmail);
        comprobarCampos();
    }

    private void comprobarCampos() {
        awesomeValidation.addValidation(RecoveryPassActivity.this, R.id.recoverEmail,
                RegexTemplate.NOT_EMPTY, R.string.empty);
        awesomeValidation.addValidation(RecoveryPassActivity.this, R.id.recoverEmail,
                Patterns.EMAIL_ADDRESS, R.string.invalid_mail);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(RecoveryPassActivity.this, LoginActivity.class));
        finish();
    }
}