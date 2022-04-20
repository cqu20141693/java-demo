/**
 * @author gow 2021/06/12
 */module my.module.comuser {
    // 利用export 导出包进行使用
    exports com.gow.consumer;
    // 导入需要的包
    requires my.module.supplier;
    //requires org.junit.jupiter;
    requires junit;
}