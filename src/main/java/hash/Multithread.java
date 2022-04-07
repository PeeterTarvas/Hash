package hash;

import java.security.NoSuchAlgorithmException;

// Main Class
public class Multithread {


    public static void main(String[] args) throws NoSuchAlgorithmException {
        int n = 8; // Number of threads
        for (int i = 0; i < n; i++) {
            Hasher object = new Hasher();
            object.start();

        }
    }
}
