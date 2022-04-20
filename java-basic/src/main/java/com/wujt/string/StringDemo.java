package com.wujt.string;

import java.util.HashSet;

/**
 * API:
 * length()
 * toCharArray()
 * charAt()
 * subString(1),subString(0,1)
 * new String(char[])
 * split("."),split(".",-1)
 *
 * @author wujt
 */
public class StringDemo {
    public static void main(String[] args) {
        String server = "sso.dev.svc.cluster.local";
        String referer = "dev.svc.cluster.local";
        int indexOf = server.indexOf(referer);
        int index = referer.indexOf(server);
        StringDemo demo = new StringDemo();
        String validIPAddress = demo.validIPAddress("2001:0db8:85a3:0:0:8A2E:0370:7334:");

        System.out.println(index);
        System.out.println(".... test indexOf start ....");
        testIndexOf();
        System.out.println(".... test indexOf end ....");

        System.out.println(".... test split start ....");
        testSplit();
        System.out.println(".... test split end ....");


        String topic="/productId/deviceId/read";

        int indexOf1 = topic.lastIndexOf("/", 1);
        String substring = topic.substring(indexOf1);

        HashSet<String> strings = new HashSet<>();
        System.out.println( String.join(",",strings));
        strings.add("poo");
        System.out.println(String.join(",",strings));
        strings.add("pjj");
        System.out.println(String.join(",",strings));

        String req_url="http://wila.com/storage";
        String url="http://wila.com/storage/ga-ea/common/202204/6461326556694a21a496e36aeb45a72f.whl";
        int indexOf2 = url.indexOf("/ga-ea/");
        System.out.println(indexOf2);
        String sub = url.substring(req_url.length()+1);
        System.out.println(sub);
        String bucket = sub.substring(0, sub.indexOf("/"));
        String object = sub.substring(sub.indexOf("/")+1);
        String prefix="hell:";
        String s = prefix + bucket;
        String s1 = s.substring(prefix.length());
        System.out.println(bucket+":"+object);
    }

    private static void testSplit() {
        String s1 = "test";
        String s2 = "test:1";
        String s3 = "test:1:";
        String[] split = s1.split(":");
        if (split.length == 1) {
            System.out.println("split not fount length =1");
        }
        String[] split1 = s2.split(":", -1);
        if (split1.length == 1) {
            System.out.println("split limit -1 not fount length =1");
        }
        String[] split2 = s3.split(":");
        String[] split3 = s3.split(":", -1);
        if(split2.length!=split3.length){
            System.out.println("split -1 length"+split3.length);
        }

    }

    private static void testIndexOf() {

        String key = "hello";
        String name = "hello:hello";
        String value = "hello:hello:hello";

        int i = key.indexOf(":");
        if (i == -1) {
            System.out.println("indexOf not fount retrun -1");
        }
        int index = name.indexOf(":");
        if (index != -1) {
            System.out.println("indexOf fount " + index);
        }
        int i1 = value.indexOf(":");
        if (i1 == index) {
            System.out.println("indexOf only fount first symbol");
        } else {
            System.out.println("indexOf return last symbol index");
        }
    }

    public String validIPAddress(String ip) {
        String[] split = ip.split("\\.");
        if (split.length == 4) {
            return is4(split);
        } else {
            String[] split1 = ip.split(":");
            if (split1.length == 8) {
                return is6(split1);
            }
        }
        return "Neither";
    }

    private String is6(String[] split) {
        String chars = "0123456789abcdefABCDEF";
        for (String s : split) {
            if (s.length() > 0 && s.length() < 5) {
                for (int i = 0; i < s.length(); i++) {
                    // 如果找不到
                    if (chars.indexOf(s.charAt(i)) == -1) {
                        return "Neither";
                    }
                }
            } else {
                return "Neither";
            }
        }
        return "IPv6";
    }

    private String is4(String[] split) {
        for (String s : split) {

            int i = Integer.parseInt(s);
            if (i <= 255) {
                // parse 01
                if (s.charAt(0) == '0' && s.length() != 1) {
                    return "Neither";
                }
            } else {
                return "Neither";
            }
        }
        return "IPv4";
    }
}
