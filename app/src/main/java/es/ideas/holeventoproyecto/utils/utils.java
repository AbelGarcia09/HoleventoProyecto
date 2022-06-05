package es.ideas.holeventoproyecto.utils;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import es.ideas.holeventoproyecto.BusinessMainActivity;
import es.ideas.holeventoproyecto.NormalUserMainActivity;
import es.ideas.holeventoproyecto.auth.LoginActivity;
import es.ideas.holeventoproyecto.modelo.Provincia;

public class utils {

    private  static final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static final DatabaseReference database = FirebaseDatabase.getInstance().getReference();;

    public static String generateEncode(String input) {
        HashFunction hf = Hashing.sha256();
        Hasher hasher = hf.newHasher();

        HashCode hc = hasher.putString(input, StandardCharsets.UTF_8).hash();

        return hc.toString();
    }

    public static String obtenerUid() {

        return user.getUid();
    }


    static String username = "";
    public static String obtenerNombreUsuario(){

        database.child("UsuarioBusiness").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot datos : snapshot.getChildren()) {
                        if (datos.getKey().equals(obtenerUid())){
                            Log.i("DATOS", datos.child("nombreUsuario").getValue().toString());
                            username = datos.child("nombreUsuario").getValue().toString();
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Error", "No existe el usuario");
            }
        });
        Log.i("DATOS",username);
        return username;
    }
}
