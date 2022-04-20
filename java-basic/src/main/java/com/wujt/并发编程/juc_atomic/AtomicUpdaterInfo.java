package com.wujt.并发编程.juc_atomic;

/**
 * @author wujt
 */
public  class AtomicUpdaterInfo {
     volatile  int age;
     volatile  long time;
     volatile  String name;
     volatile  Boolean work;

    public AtomicUpdaterInfo() {
    }

    public AtomicUpdaterInfo(int age, long time, String name, Boolean wrok) {
        this.age = age;
        this.time = time;
        this.name = name;
        this.work = wrok;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getWork() {
        return work;
    }

    public void setWork(Boolean work) {
        this.work = work;
    }

    @Override
    public String toString() {
        return "AtomicUpdaterInfo{" +
                "age=" + age +
                ", time=" + time +
                ", name='" + name + '\'' +
                ", work=" + work +
                '}';
    }
}
