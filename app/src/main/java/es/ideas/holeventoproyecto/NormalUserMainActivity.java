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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import es.ideas.holeventoproyecto.auth.LoginActivity;
import es.ideas.holeventoproyecto.fragments.normalUser.BuscarEventos;
import es.ideas.holeventoproyecto.fragments.normalUser.ListaEventos;
import es.ideas.holeventoproyecto.fragments.normalUser.MainPage;

public class NormalUserMainActivity extends AppCompatActivity {

    private ImageButton btnLogout;
    private BottomNavigationView bv;
    private TextView tvUsername;
    private Fragment currentFragment;


    private FirebaseFirestore database = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.normal_user_main_activity);

        //carga los elementos de la vista
        iniciarVista();

        if (savedInstanceState == null) {
            currentFragment = new MainPage();
            cambiaFragment(currentFragment);
        } else {
            cambiaFragment(currentFragment);
        }


        //Se crea la BottomNavigationBar, y dependiendo de donde hagamos clic, carga el fragment
        //del mapa, o el fragment con el historial de recorridos.
        bv = (BottomNavigationView) findViewById(R.id.navBarUser);
        bv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.listEvent:
                        currentFragment = new ListaEventos();
                        break;
                    case R.id.mainPage:
                        currentFragment = new MainPage();
                        break;

                    case R.id.searchEvent:
                        currentFragment = new BuscarEventos();
                        break;

                }
                cambiaFragment(currentFragment);
                return true;
            }
        });


        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(NormalUserMainActivity.this, LoginActivity.class));
                finish();
            }
        });
        //----CLASS END----
    }

    private void iniciarVista() {
        btnLogout = (ImageButton) findViewById(R.id.btnLogoutUser);
        tvUsername = (TextView) findViewById(R.id.nombreUserNormal);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user.getUid();


        database.collection("UsuarioNormal").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    String username =task.getResult().get("nombreUsuario").toString();
                    tvUsername.setText(username);
                    Log.i("DATOS", "Nombre Usuario: " + username);
                }
            }
        });
    }

    private void cambiaFragment(Fragment f) {
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_Layout, f).commit();
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        currentFragment.onResume();
    }

}