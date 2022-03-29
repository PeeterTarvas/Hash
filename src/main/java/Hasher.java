
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Hasher {
    private String name;
    private String prevHash;
    private List<String> nonss;
    private List<String> alphanumerics;
    private List<String> alphanumericsZeros;
    private Integer bestZeroes = 0;
    private StringBuilder sb;

    public Hasher() {
        this.prevHash = "b94330354c8c6bf787f5d75c0d2f16bcfb663e4433692be7165283d4fd954183aa812ecbc5334c47273892d6dbaf1c5479312bd9c34c5af3e8dd461134de32cloudflow";
        this.nonss = new ArrayList<>(List.of("10000000000000000000000000000000".split("")));
        this.alphanumerics = List.of("123456789abcdefghijklmnopqrstuvwxyz".split(""));
        this.alphanumericsZeros = List.of("0123456789abcdefghijklmnopqrstuvwxyz".split(""));
    }


    private static String getRandomHexString() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < 32) {
            sb.append(Integer.toHexString(r.nextInt()));
        }

        return sb.toString().substring(0, 32);
    }

    private static String getSHA(String str) throws NoSuchAlgorithmException {
        String s1 = str;
        MessageDigest msg = MessageDigest.getInstance("SHA-512");
        byte[] hash = msg.digest(s1.getBytes(StandardCharsets.UTF_8));
        // convert bytes to hexadecimal
        StringBuilder s = new StringBuilder();
        for (byte b : hash) {
            s.append(Integer.toString((b & 0xff) + 0x100, 16).substring(1));
        }
        return s.toString();
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        char zero = "0".charAt(0);
        int highest = 0;
        while (true) {
            String nonce = getRandomHexString();
            StringBuilder abc = new StringBuilder().append("b94330354c8c6bf787f5d75c0d2f16bcfb663e4433692be7165283d4fd954183aa812ecbc5334c47273892d6dbaf1c5479312bd9c34c5af3e8dd461134de32cloudflow").append(nonce);
            String result = getSHA(abc.toString());
            int c = 0;
            while (result.charAt(c) == zero) {
                c += 1;
            }
            if (c >= highest) {
                highest = c;
                System.out.println("there are " + c + " zeros! The result is: " + result + " and nonce is: " + nonce);
            }

        }
    }
}
