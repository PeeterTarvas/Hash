
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Hasher {
    private String name;
    private String prevHash;
    private List<String> nonss;
    private List<String> alphanumerics;
    private Integer alphanumericIndex;
    private Integer nonssIndex;
    private Integer bestZeroes = 0;

    public Hasher() {
        this.name = "cloudflow";
        this.prevHash = "b94330354c8c6bf787f5d75c0d2f16bcfb663e4433692be7165283d4fd954183aa812ecbc5334c47273892d6dbaf1c5479312bd9c34c5af3e8dd461134de32cloudflow";
        this.nonss = new ArrayList<>(List.of("10000000000000000000000000000000".split("")));
        this.alphanumerics = List.of("123456789abcdefghijklmnopqrstuvwxyz".split(""));

        this.alphanumericIndex = 0;
        this.nonssIndex = 0;

    }

    public String genNonsse() {
        String alphanumeric = this.alphanumerics.get(alphanumericIndex);
        this.alphanumericIndex++;
        if (this.alphanumericIndex == 35) {
            this.alphanumericIndex%=35;
            this.nonssIndex++;

        }
        this.alphanumericIndex %= 36;
        this.nonss.set(nonssIndex, alphanumeric);
        return this.nonss.toString();
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

    public static void main(String[] args) {
        Hasher h = new Hasher();
        String hash = h.generateHash(String.valueOf(h.nonss));
        while (true) {
            System.out.println(h.genNonsse());
        }
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
            String hashtext = no.toString(16);

            // Add preceding 0s to make it 32 bit
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            // return the HashText
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
