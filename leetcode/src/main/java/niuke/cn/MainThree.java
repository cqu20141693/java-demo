package niuke.cn;

/**
 * @author gow
 * @date 2022/1/15
 */
public class MainThree {
    public static void main(String[] args) {
        MainThree main = new MainThree();
        int maxSubSeq = main.findShortCut("", 2);
        System.out.println(maxSubSeq);
    }

    public int findShortCut(String cmdStr, int level) {
        int result = 0;

        // aeiouAEIOU
        // 头尾匹配上面字符的字符串为元音字符串
        // 字符串内部的非上面字符个数为瑕疵度
        // 需要寻找level 瑕疵度的元音子字符串
        //todo 可以先遍历一遍。清理开始和结尾处非元音字符，再进行遍历


        if (level == 0) {
            int start = -1, counter = 0;
            for (int i = 0; i < cmdStr.toCharArray().length; i++) {
                if (contains(cmdStr.charAt(i)) && i - 1 == start) {
                    // 连续元音
                    counter++;
                } else {
                    counter = 0;
                }

                if (counter > result) {
                    // 设置result
                    result = counter;
                }
            }
        } else {
            // 遍历字符串
            // 以元音字符串为开始，直到瑕疵度为level，满足进行下一个元音字母遍历
            // 如果遍历到了结束都没有达到level,直接结束循环
            char[] chars = cmdStr.toCharArray();
            int nextIndex = 0, counter = 0, startIndex = -1;
            boolean first = true, second = true;
            for (int i = 0; i < chars.length; i++) {
                if (first) {
                    if (contains(chars[i])) {
                        startIndex = i;
                        first = false;
                    }
                } else {
                    if (contains(chars[i]) && counter <= level) {
                        if (second) {
                            nextIndex = i;
                            second = false;
                        }
                        if (i == chars.length - 1 && counter == level) { // 字符串结束时
                            // 结束遍历，更新result
                            result = chars.length - startIndex;
                        }
                    } else {
                        counter += 1;
                        if (counter > level) {
                            result = i - startIndex;
                            // 如果遍历到结尾-1除，
                            if (i < chars.length - 2) {
                                //从第二元音字符处进行遍历
                                i = nextIndex;
                            }
                        }
                    }
                }

            }

        }

        return result;
    }

    public boolean contains(char ch) {
        if ('a' == ch || 'A' == ch || 'e' == ch || 'E' == ch || 'i' == ch || 'I' == ch
                || 'o' == ch || 'O' == ch || 'u' == ch || 'U' == ch) {
            return true;
        }
        return false;
    }
}
