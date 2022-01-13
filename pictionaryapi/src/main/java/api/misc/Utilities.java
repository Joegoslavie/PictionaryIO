package api.misc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utilities {

    private static final Logger log = LoggerFactory.getLogger(Utilities.class);

    private Utilities() {
        throw new IllegalStateException("Utilities class is not initializable! (loser)");
    }

    public static String hashPassword(String plaintext){
        String hashedPassword = null;
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(plaintext.getBytes(StandardCharsets.UTF_8));
            byte[] digest = md.digest();

            hashedPassword = String.format("%064x", new BigInteger(1, digest));

        }catch (NoSuchAlgorithmException e) {
            log.error("Algorithm not found!", e);
        }

        return hashedPassword;
    }

    public static boolean verifyPassword(String current, String stored){
        String pw = hashPassword(current);
        if(pw != null)
            return pw.equals(stored);
        else
            return false;
    }
}
