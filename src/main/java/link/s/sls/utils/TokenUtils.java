package link.s.sls.utils;

import org.apache.commons.lang3.RandomUtils;

public class TokenUtils {

	private static final char[] numbers = "0123456789".toCharArray();

	private static final char[] alphabets = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM".toCharArray();

	private static final char[] chars = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM0123456789".toCharArray();

	/**
	 * 生成指定长度,全部由0-9数字构成的随机字符串
	 * @param length
	 * @return
	 */
	public static String generateNumberToken(int length){
		return generateToken(length,numbers);

	}

	/**
	 * 生产指定长度,全部由大小写字母构成的随机字符串
	 * @param length
	 * @return
	 */
	public static String generateAlphabetToken(int length){
		return generateToken(length,alphabets);

	}

	/**
	 * 生产指定长度,由大小写字母和数字构成的随机字符串
	 * @param length
	 * @return
	 */
	public static String generateToken(int length){
		return generateToken(length,chars);
	}

	/**
	 * 生成指定长度,由指定字符构成的随机字符串
	 * @param length
	 * @param chars
	 * @return
	 */
	public static String generateToken(int length, char[] chars){
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			int idx = RandomUtils.nextInt()%chars.length;
			sb.append(chars[idx]);
		}
		return sb.toString();
	}

}
