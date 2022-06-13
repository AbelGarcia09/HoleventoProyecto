package es.ideas.holeventoproyecto.utils;

import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class Utils {

    public static String generateEncode(String input) {
        HashFunction hf = Hashing.sha256();
        Hasher hasher = hf.newHasher();

        HashCode hc = hasher.putString(input, StandardCharsets.UTF_8).hash();

        return hc.toString();
    }

}
