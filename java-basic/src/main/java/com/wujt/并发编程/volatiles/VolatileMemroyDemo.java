package com.wujt.并发编程.volatiles;

/**
 * @author wujt
 */
public class VolatileMemroyDemo {

    private Integer count = -1;
    private volatile Boolean flag = false;

    public static void main(String[] args) {
        VolatileMemroyDemo volatileMemroyDemo = new VolatileMemroyDemo();
        for(int i=0;i<5;i++) {
            // IntStream.range(0, 5).forEach(i -> {

            int finalI = i;
            new Thread(() -> {
                volatileMemroyDemo.update(finalI, true);
            }).start();
            // });
            //  IntStream.range(0, 5).forEach(i -> {
            new Thread(() -> {
                volatileMemroyDemo.update(finalI, true);
            }).start();
            new Thread(volatileMemroyDemo::count).start();
            new Thread(() -> {
                volatileMemroyDemo.update(finalI, true);
            }).start();

            new Thread(volatileMemroyDemo::count).start();
            new Thread(() -> {
                volatileMemroyDemo.update(finalI, true);
            }).start();
            new Thread(volatileMemroyDemo::count).start();
        }
        //   });

    }

    public void update(Integer i, Boolean f) {
        System.out.println(String.format("flag=%s,count=%d", flag, count));
        count = i;
        flag = f;
    }

    public void count() {
        if (flag) {
            count += count;
        }
    }

    @Override
    public String toString() {
        return "VolatileMemroyDemo{" +
                "count=" + count +
                ", flag=" + flag +
                '}';
    }
}
