package link.s.sls.utils;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public class MD5Utils {

    protected static char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

    protected static MessageDigest messagedigest = null;
    static {
        try {
            messagedigest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsaex) {
            System.err.println(String.format("%s%s", MD5Utils.class.getName(), "初始化失败，MessageDigest不支持MD5Utils。"));
            nsaex.printStackTrace();
        }
    }

    public static String getMD5(final File file) throws IOException {
        try (FileInputStream in = new FileInputStream(file);
             FileChannel ch = in.getChannel();) {
            MappedByteBuffer byteBuffer = ch.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            messagedigest.update(byteBuffer);
            String bufferToHex = bufferToHex(messagedigest.digest());
            return bufferToHex;
        }
    }

    public static String getMD5(final String s) {

        if (StringUtils.isBlank(s)) {
            return null;
        }
        return getMD5(s.getBytes());
    }

    public static String getMD5(final byte[] bytes) {

        if (ArrayUtils.isEmpty(bytes)) {
            return null;
        }
        messagedigest.update(bytes);
        return bufferToHex(messagedigest.digest());
    }

    private static String bufferToHex(final byte bytes[]) {

        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(final byte bytes[], final int m, final int n) {

        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(final byte bt, final StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
