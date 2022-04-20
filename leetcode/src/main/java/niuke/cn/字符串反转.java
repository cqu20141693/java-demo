package niuke.cn;

/**
 * @author gow
 * @date 2022/1/12
 */
public class 字符串反转 {
    public String solve(String str) {
        // write code here
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length < 2) {
            return str;
        }
        StringBuilder builder = new StringBuilder();
        for (int i = length - 1; i >= 0; i--) {
            builder.append(str.charAt(i));
        }
        return builder.toString();
    }

    public String solve1(String str) {
        // write code here
        if (str == null) {
            return null;
        }
        int length = str.length();
        if (length < 2) {
            return str;
        }
        char[] array = str.toCharArray();

        for (int i = 0; i < length; i++) {
            array[length - 1 - i] = (str.charAt(i));
        }
        return new String(array);
    }
}
