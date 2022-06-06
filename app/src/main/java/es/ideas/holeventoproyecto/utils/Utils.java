package es.ideas.holeventoproyecto.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.nio.charset.StandardCharsets;

public class Utils {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static String generateEncode(String input) {
        HashFunction hf = Hashing.sha256();
        Hasher hasher = hf.newHasher();

        HashCode hc = hasher.putString(input, StandardCharsets.UTF_8).hash();

        return hc.toString();
    }

    public String obtenerUid() {
        return user.getUid();
    }

}
