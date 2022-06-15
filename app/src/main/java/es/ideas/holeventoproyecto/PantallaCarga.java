package es.ideas.holeventoproyecto;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import es.ideas.holeventoproyecto.auth.LoginActivity;
import android.os.Handler;

public class PantallaCarga extends AppCompatActivity {

    private FirebaseFirestore database;
    private LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_carga);
        getSupportActionBar().hide();
        lottie = findViewById(R.id.lottie);

        //está pantalla nos aparecerá cuando iniciemos la app, es un afnimación y justo al terminar
        //se nos abrira la app.

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // User is signed in
                    compruebaTipoUsuario();
                } else {
                    startActivity(new Intent(PantallaCarga.this, LoginActivity.class));
                    finish();
                    // User is signed out
                    Log.d("LOGIN", "onAuthStateChanged:signed_out");
                }

            }

        }, 3000);

    }

    private void compruebaTipoUsuario() {
        database = FirebaseFirestore.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();

        database.collection("UsuarioBusiness").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists()){
                        startActivity(new Intent(PantallaCarga.this,
                                BusinessMainActivity.class));
                    } else {
                        startActivity(new Intent(PantallaCarga.this,
                                NormalUserMainActivity.class));
                    }
                    finish();
                }else{
                    Toast.makeText(PantallaCarga.this, "No se encuentra el usuario", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}