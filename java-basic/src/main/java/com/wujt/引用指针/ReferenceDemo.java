package com.wujt.引用指针;

/**
 * 强引用： 每一个变量的引用对于对象来说都会影响可达性分析；
 * 只有当对象的强引用为0时才会在下次GC时回收
 * @author wujt
 */
public class ReferenceDemo {
    public static void main(String[] args) {
        /**
         * java 中对对象的引用默认是4个字节（指针压缩）；
         *
         */

        MyObject myObject = new MyObject("test", new Object());
        /**
         * java 中所有的对象都是一个强引用；当将引用改变不会改变引用地址中的对象
         */
        Object o = myObject.object;
        myObject.object = null;
        System.out.println(o);
    }

    private static final class MyObject {
        private String test;
        private Object object;

        public MyObject(String test, Object object) {
            this.test = test;
            this.object = object;
        }
    }
}
