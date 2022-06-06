package es.ideas.holeventoproyecto;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import es.ideas.holeventoproyecto.auth.LoginActivity;
import es.ideas.holeventoproyecto.fragments.business.NuevoEvento;
import es.ideas.holeventoproyecto.fragments.business.Profile;
import es.ideas.holeventoproyecto.utils.Utils;

public class BusinessMainActivity extends AppCompatActivity {

    private BottomNavigationView bv;
    private Fragment currentFragment;
    private TextView tvUsername;
    private ImageButton btnLogout;
    private Utils util;

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.business_main_activity);


        //carga los elementos de la vista
        iniciarVista();

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(BusinessMainActivity.this, LoginActivity.class));
                finish();
            }
        });

        //Si se rota el móvil, no nos cargará un nuevo fragment, por lo que
        //no perderemos los datos que tenemos en pantalla.
        if (savedInstanceState == null) {
            currentFragment = new Profile();
            cambiaFragment(currentFragment);
        } else {
            cambiaFragment(currentFragment);
        }


        //Se crea la BottomNavigationBar, y dependiendo de donde hagamos clic, carga el fragment
        //del mapa, o el fragment con el historial de recorridos.
        bv = (BottomNavigationView) findViewById(R.id.navBar);
        bv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        currentFragment = new Profile();
                        break;

                    case R.id.newEvent:
                        currentFragment = new NuevoEvento();
                        break;
                }
                cambiaFragment(currentFragment);
                return true;
            }
        });


    }

    private void iniciarVista() {
        tvUsername = (TextView) findViewById(R.id.nombreUserBusiness);
        btnLogout = (ImageButton) findViewById(R.id.btnLogout);
        util = new Utils();

        database.child("UsuarioBusiness").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datos : snapshot.getChildren()) {
                        if (datos.getKey().equals(util.obtenerUid())) {
                            String username = datos.child("nombreUsuario").getValue().toString();
                            tvUsername.setText(username);
                            Log.i("DATOS", "dentro del for: " + username);
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

    private void cambiaFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout, f).commit();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }


}