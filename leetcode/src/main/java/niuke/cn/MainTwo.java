package niuke.cn;

/**
 * @author gow
 * @date 2022/1/15
 */
public class MainTwo {
    public static void main(String[] args) {
        MainTwo main = new MainTwo();
        String cryptStr = main.cryptStr('1', "password__a12345678_timeout_100");
        System.out.println(cryptStr);
    }

    public String cryptStr(char ch, String cmdStr) {
        // 命令字由单个字符或者“”字符串，
        // 需要寻找ch 出现的非“”的字符和“”中字符串
        // 替换其为******
        // 剔除多余的_

        String[] strings = cmdStr.split("_");
        StringBuilder builder = new StringBuilder();
        String replaceStr = "******";
        boolean exist = false;
        for (String cmd : strings) {
            if (cmd.equals("")) { // 空字符串
                break;
            }

            char[] chars = cmd.toCharArray();

            if (cmd.charAt(0) == '"') {  // 如果字符串startWith "
                boolean match = false;
                for (int j = 1; j < chars.length - 1; j++) {
                    if (chars[j] == ch) {
                        match = true;
                        exist = true;
                        break;
                    }
                }
                if (match) {
                    builder.append(replaceStr);
                } else {
                    builder.append(cmd);
                }
            } else {

                for (char aChar : chars) {
                    if (aChar == ch) { // 匹配
                        exist = true;
                        builder.append(replaceStr);
                    } else {
                        builder.append(aChar);
                    }
                }
            }
            builder.append("_");
        }
        if (exist) {
            String toString = builder.toString();
            return toString.substring(0, toString.length() - 1);
        }

        return "ERROR";
    }
}
