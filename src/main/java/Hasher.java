
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Hasher {
    private String name;
    private String prevHash;
    private List<String> nonss;
    private List<String> alphanumerics;
    private List<String> alphanumericsZeros;
    private Integer bestZeroes = 0;
    private StringBuilder sb;

    public Hasher() {
        this.name = "cloudflow";
        this.prevHash = "b94330354c8c6bf787f5d75c0d2f16bcfb663e4433692be7165283d4fd954183aa812ecbc5334c47273892d6dbaf1c5479312bd9c34c5af3e8dd461134de32cloudflow";
        this.nonss = new ArrayList<>(List.of("10000000000000000000000000000000".split("")));
        this.alphanumerics = List.of("123456789abcdefghijklmnopqrstuvwxyz".split(""));
        this.alphanumericsZeros = List.of("0123456789abcdefghijklmnopqrstuvwxyz".split(""));
        this.sb = new StringBuilder(32);
    }

    public void clearSb() {
        this.sb.delete(0, this.sb.length());
    }

    public String genNonsse() {
        // chose a Character random from this String
        // create StringBuffer size of AlphaNumericString

        for (int i = 0; i < 32; i++) {

            // generate a random number between
            // 0 to AlphaNumericString variable length
            if (i == 0) {
                int index
                    = (int)(this.alphanumerics.size()
                    * Math.random());

                 // add Character one by one in end of sb
                sb.append(this.alphanumerics.get(index));
            } else {
                int index
                        = (int)(this.alphanumericsZeros.size()
                        * Math.random());

                // add Character one by one in end of sb
                sb.append(this.alphanumericsZeros.get(index));
            }
        }
        return sb.toString();
    }

    public Integer getLeadingZeroesCount(String hash) {
        int count = 0;
        boolean isZeroes = true;
        while (isZeroes) {
            if (String.valueOf(hash.charAt(count)).equals("0")) {
                count++;
            } else {
                isZeroes = false;
            }
        }
        return count;
    }

    public String generateHash(String nonss) {
        String s = prevHash +
                nonss;
        return encryptThisString(s);
    }

    public void write(String input) {
        String path = "src/main/resources/output.txt";
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(path))) {
            writer.append(input);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("IOException:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        int maxZeroes = 0;
        while (true) {

            String nonsse = genNonsse();
            String hash = generateHash(nonsse);
            int zeroes = getLeadingZeroesCount(hash);
            if (zeroes > maxZeroes) {
                maxZeroes = zeroes;
                write(nonsse + ' ' + hash);
                System.out.println(nonsse + ' ' + hash);
            }
            clearSb();


        }

    }

    public static void main(String[] args) {
        Hasher h = new Hasher();
        h.run();

    }

    public static String encryptThisString(String input) {
        try {
            // getInstance() method is called with algorithm SHA-512
            MessageDigest md = MessageDigest.getInstance("SHA-512");

            // digest() method is called
            // to calculate message digest of the input string
            // returned as array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            StringBuilder hashtext = new StringBuilder(no.toString(16));


            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 126) {
                hashtext.insert(0, "0");
            }
            // return the HashText
            return hashtext.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
