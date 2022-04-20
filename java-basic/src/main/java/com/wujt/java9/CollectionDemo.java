package com.wujt.java9;

import java.util.*;

/**
 * 新增集合API
 * static <E> List<E> of(E e1, E e2, E e3);
 * static <E> Set<E>  of(E e1, E e2, E e3);
 * static <K,V> Map<K,V> of(K k1, V v1, K k2, V v2, K k3, V v3);
 * static <K,V> Map<K,V> ofEntries(Map.Entry<? extends K,? extends V>... entries)
 *
 * @author gow 2021/06/12
 */
public class CollectionDemo {
    public static void main(String[] args) {
        testOld();
        System.out.println("java 9 ");
        testJava9();
    }

    /**
     * java 9
     */
    private static void testJava9() {
        Set<String> set = Set.of("A", "B", "C");
        System.out.println(set);
        List<String> list = List.of("A", "B", "C");
        System.out.println(list);
        Map<String, String> map = Map.of("A", "Apple", "B", "Boy", "C", "Cat");
        System.out.println(map);

        Map<String, String> map1 = Map.ofEntries(
                new AbstractMap.SimpleEntry<>("A", "Apple"),
                new AbstractMap.SimpleEntry<>("B", "Boy"),
                new AbstractMap.SimpleEntry<>("C", "Cat"));
        System.out.println(map1);
    }

    /**
     * java 8 之前创建不可变集合
     */
    private static void testOld() {
        Set<String> set = new HashSet<>();
        set.add("A");
        set.add("B");
        set.add("C");
        set = Collections.unmodifiableSet(set);
        System.out.println(set);
        List<String> list = new ArrayList<>();

        list.add("A");
        list.add("B");
        list.add("C");
        list = Collections.unmodifiableList(list);
        System.out.println(list);
        Map<String, String> map = new HashMap<>();

        map.put("A", "Apple");
        map.put("B", "Boy");
        map.put("C", "Cat");
        map = Collections.unmodifiableMap(map);
        System.out.println(map);
    }
}
