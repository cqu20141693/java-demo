package com.wujt.字符串;

/**
 * @author gow
 * @date 2021/9/7
 */
public class 字符串匹配 {

    public static void main(String[] args) {
        字符串匹配 stringMatch = new 字符串匹配();
        int useBM1 = stringMatch.useBM("mississippi", "issi");
        int useBM2 = stringMatch.useBM("abcbdacdcdcac", "acdcdca");

        int useKMP1 = stringMatch.useKMP("mississippi", "issi");
        int useKMP2 = stringMatch.useKMP("abcbdacdcdcac", "acdcdca");
        int useKMP3 = stringMatch.useKMP("bcbcbcbcbea", "bcbcbcbea");
        System.out.println(useKMP1 == useBM1);
        System.out.println(useBM2 == useKMP2);
    }

    /**
     * 最长公共前后缀
     * 1. 计算最长公共前后缀next 数组：next[]
     * 2. 进行匹配, 如果出现前缀不匹配 j：
     *   2.1 从模式串0开始匹配（next[j-1]+1),
     *
     * 时间复杂度：O(n+m)O(n+m)，其中 nn 是字符串 \textit{haystack}haystack 的长度，mm 是字符串 \textit{needle}needle 的长度。我们至多需要遍历两字符串一次。
     *
     * 空间复杂度：O(m)O(m)，其中 mm 是字符串 \textit{needle}needle 的长度。我们只需要保存字符串 \textit{needle}needle 的前缀函数。
     *
     *
     * @param haystack
     * @param needle
     * @return
     */
    private int useKMP(String haystack, String needle) {

        // 两种特殊情况
        if (needle.length() == 0) {
            return 0;
        }
        if (haystack.length() == 0) {
            return -1;
        }

        char[] hay = haystack.toCharArray();
        char[] need = needle.toCharArray();
        int hayLength = haystack.length();
        int needLength = needle.length();


        return kmp(hay, hayLength, need, needLength);
    }

    private int kmp(char[] hay, int hayLength, char[] need, int needLength) {

        // 获取next数组
        int[] next = getNext(need, needLength);

        int j = 0;
        for (int i = 0; i < hayLength; i++) {

            // 发现不匹配字符，根据next 数组移动指针，移动到最大公共前后缀的前缀的后一位
            // 匹配时，继续下一个位置匹配
            while (j > 0 && hay[i] != need[j]) {
                j = next[j - 1] + 1;
                // 超出长度时，直接返回不存在
                if (needLength - j + 1 > hayLength) {
                    return -1;
                }
            }
            // 如果相同就将指针后移一位，比较下一个字符
            if (hay[i] == need[j]) {
                j++;
            }
            // 遍历完模式串匹配成功
            if (j == needLength) {
                return i - needLength + 1;
            }

        }

        return -1;
    }

    /**
     * 获取模式串的next 数组
     *
     * @param need
     * @param needLength
     * @return
     */
    private int[] getNext(char[] need, int needLength) {
        // 定义 next数组
        int[] next = new int[needLength];
        // init
        next[0] = -1;
        int k = -1;
        for (int i = 1; i < needLength; i++) {
            //
            while (k != -1 && need[k + 1] != need[i]) {
                k = next[k];
            }
            //
            if (need[k + 1] == need[i]) {
                k++;
            }
            next[i] = k;
        }

        return next;

    }

    private int useBF(String haystack, String needle) {
        if (haystack == null || needle == null || needle.length() == 0) {
            return 0;
        }
        int hayLength = haystack.length();
        int needleLength = needle.length();
        if (hayLength < needleLength) {
            return -1;
        }
        // BF 匹配，每次移动一位下标
        for (int i = 0; i < hayLength - needleLength + 1; i++) {
            for (int j = 0; j < needleLength; j++) {
                if (haystack.charAt(i + j) != needle.charAt(j)) {
                    break;
                }
                if (j == needleLength - 1) {
                    return i;
                }
            }
        }
        return -1;
    }

    /**
     * 1.如果模式串含有好后缀，无论是中间还是头部可以按照规则进行移动。如果好后缀在模式串中出现多次，则以最右侧的好后缀为基准。
     * 2.如果模式串头部含有好后缀子串则可以按照规则进行移动，中间部分含有好后缀子串则不可以。
     * 3.如果在模式串尾部就出现不匹配的情况，即不存在好后缀时，则根据坏字符进行移动
     * 4. 如果好后缀和坏字符都没有匹配，移动模式串长度。
     *
     * @param haystack
     * @param needle
     * @return
     */
    private int useBM(String haystack, String needle) {
        char[] hay = haystack.toCharArray();
        char[] need = needle.toCharArray();
        int hayLength = haystack.length();
        int needLength = needle.length();

        return bm(hay, hayLength, need, needLength);
    }

    private int bm(char[] hay, int hayLength, char[] need, int needLength) {

        // 创建一个数组保存最右边字符的下标
        int[] bc = new int[256];

        badChar(need, needLength, bc);

        // 用来保存各种长度好后缀的最右位置的数组
        int[] suffix_index = new int[needLength];
        // 判断是否是头部，如果是头部则true
        boolean[] isPrefix = new boolean[needLength];
        goodSuffix(need, needLength, suffix_index, isPrefix);

        // 第一个匹配字符
        int i = 0;
        // 结束条件
        while (i < hayLength - needLength) {
            int j;
            for (j = needLength - 1; j >= 0; j--) {
                // 字符匹配
                if (hay[i + j] != need[j]) {
                    break;
                }
            }
            // 模式串匹配成功
            if (j < 0) {
                return i;
            }

            // 匹配失败
            // 求出坏字符规则下移送的位数：坏字符的下标-最右边的下标（bc）
            // 可能值为-1或index
            // 当坏字符不存在模式串时，直接移动j+1
            int x = j - bc[hay[i + j]];
            // 求出好后缀情况下移动位数：如果不包含好后缀，利用坏字符
            int y = 0;
            boolean notBadChar = needLength - 1 - j > 0;
            if (y < needLength - 1 && notBadChar) {
                y = move(j, needLength, suffix_index, isPrefix);
            }
            // 移动,下一轮匹配
            i = i + Math.max(x, y);
        }
        return -1;
    }

    /**
     * 计算好后缀位移
     *
     * @param j            : 坏字符下标
     * @param needLength
     * @param suffix_index
     * @param isPrefix
     * @return
     */
    private int move(int j, int needLength, int[] suffix_index, boolean[] isPrefix) {
        // 好后缀长度
        int k = needLength - 1 - j;
        // 如果含有长度为k的好后缀，返回位移数。
        if (suffix_index[k] != -1) {
            return j - suffix_index[k] + 1;
        }

        // 子后缀
        for (int r = j + 2; r <= needLength - 1; r++) {
            // 如果是头部
            if (isPrefix[needLength - r]) {
                return r;
            }
        }
        // 如果没有发现好后缀匹配的串，或者头部为好后缀字串，则移动needLength(匹配串长度)
        return needLength;
    }

    /**
     * 用于求好后缀情况下的移动位数
     *
     * @param need
     * @param needLength
     * @param suffix
     * @param prefix     : 好后缀位数对应的表示
     */
    private void goodSuffix(char[] need, int needLength, int[] suffix, boolean[] prefix) {
        // init
        for (int i = 0; i < needLength; i++) {
            suffix[i] = -1;
            prefix[i] = false;
        }

        for (int i = 0; i < needLength - 1; i++) {
            int j = i;
            int k = 0;
            // 将后缀与前缀进行比较
            while (j >= 0 && need[j] == need[needLength - 1 - k]) {
                j--;
                k++;
                suffix[k] = j + 1;
            }
            // 此时表示匹配成功
            if (j == -1) {
                prefix[k] = true;
            }
        }
    }

    /**
     * 初始化坏字符情况下移动位数
     * 保存每个字符在模式串的位置，用于坏字符情况下获取坏字符在模式串的位置。
     *
     * @param need
     * @param needLength
     * @param bc
     */
    private void badChar(char[] need, int needLength, int[] bc) {
        // init
        for (int i = 0; i < 256; i++) {
            bc[i] = -1;
        }
        // 如果模式串中出现相同的字符(比如a)，则后面字符的下标覆盖前面的
        for (int i = 0; i < needLength; i++) {
            int ascii = need[i];
            // 设置字符出现下标（最右边为准）
            bc[ascii] = i;
        }
    }
}
