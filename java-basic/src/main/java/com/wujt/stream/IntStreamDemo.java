package com.wujt.stream;

import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.stream.IntStream;

/**
 * @author wujt
 */
public class IntStreamDemo {
    public static void main(String[] args) {

        IntStream.range(0, 10).forEach(System.out::println);
        ArrayList<Integer> usingRtpPorts = new ArrayList<>();
        usingRtpPorts.add(10000);
        int from = 10000;
        int to = 10001;
        PrimitiveIterator.OfInt iterator = IntStream.range(from, to).iterator();
        while (iterator.hasNext()){
            System.out.print(iterator.nextInt()+" ");
        }
        System.out.println();
        test(usingRtpPorts, from, to);
        usingRtpPorts.add(10001);
        test(usingRtpPorts, from, to);
        test(new ArrayList<>(), from, to);
    }

    private static void test(ArrayList<Integer> usingRtpPorts, int from, int to) {
        OptionalInt first = IntStream.range(from, to + 1).filter(x -> !usingRtpPorts.contains(x)).findFirst();
        if (first.isPresent()) {
            System.out.println("port=" + first.getAsInt());
        } else {
            System.out.println("port not found");
        }
    }
}
