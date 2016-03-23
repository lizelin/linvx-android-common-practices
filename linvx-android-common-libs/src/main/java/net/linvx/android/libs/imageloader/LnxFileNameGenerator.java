package net.linvx.android.libs.imageloader;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by lizelin on 16/3/13.
 */
public class LnxFileNameGenerator {
    private LnxFileNameGenerator() {

    }

    private static final String HASH_ALGORITHM = "MD5";
    private static final int RADIX = 10 + 26; // 10 digits + 26 letters

    public static String generate(String imageUri) {
        byte[] md5 = getMD5(imageUri.getBytes());
        if (md5 == null)
            return String.valueOf(imageUri.hashCode());
        BigInteger bi = new BigInteger(md5).abs();
        String format = (imageUri.toLowerCase().endsWith("png"))?".png":"jpg";
        return bi.toString(RADIX) + format;
    }

    private static byte[] getMD5(byte[] data) {
        byte[] hash = null;
        try {
            MessageDigest digest = MessageDigest.getInstance(HASH_ALGORITHM);
            digest.update(data);
            hash = digest.digest();
        } catch (NoSuchAlgorithmException e) {
            hash = null;
        }
        return hash;
    }
}
