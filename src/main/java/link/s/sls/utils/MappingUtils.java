package link.s.sls.utils;

/**
 * @author sweeter
 * @date 2021/6/20
 */
public class MappingUtils {

    private final static String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    private final static int scale = 62;

    /**
     * 10进制转换为62进制
     * @param num
     * @return
     */
    public static String encode(long num) {
        StringBuilder sb = new StringBuilder();
        int remainder;
        while (num > scale - 1) {
            remainder = Long.valueOf(num % scale).intValue();
            sb.append(chars.charAt(remainder));
            //除以进制数，获取下一个末尾数
            num = num / scale;
        }
        sb.append(chars.charAt(Long.valueOf(num).intValue()));
        return sb.reverse().toString();
    }

    /**
     * 62进制转为10进制
     * @param str
     * @return
     */
    public static long decode(String str) {
        long value = 0;
        char tempChar;
        int tempCharValue;
        for (int i = 0; i < str.length(); i++) {
            //获取字符
            tempChar = str.charAt(i);
            //单字符值
            tempCharValue = chars.indexOf(tempChar);
            //单字符值在进制规则下表示的值
            value += (long) (tempCharValue * Math.pow(scale, str.length() - i - 1));
        }
        return value;
    }
}
