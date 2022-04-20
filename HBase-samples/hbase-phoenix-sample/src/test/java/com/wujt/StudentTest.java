package com.wujt;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.wujt.phoenixhbase.dao.StudentDao;
import com.wujt.phoenixhbase.entity.Student;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = App.class)
public class StudentTest {
    private static AtomicInteger count = new AtomicInteger(0);

    @Resource
    private StudentDao studentDao;

    @Test
    public void testUpsertOne(){
        Student s1 = new Student();
        s1.setId(30);
        s1.setName("晏奇_" + 1);
        s1.setAge(RandomUtil.getRandomNumer(50)+21);

        String gender = "女";
        s1.setGender(gender);
        s1.setWeight(RandomUtil.randomDouble(90,2)+60);
        s1.setCreateTime(new Date());

        studentDao.save(s1);
    }

    @Test
    public void testUpsert() {
        for (int i = 0; i < 30; i++) {
            Student s1 = new Student();
            s1.setId(count.getAndIncrement());
            s1.setName("晏奇_" + i);
            s1.setAge(RandomUtil.getRandomNumer(50)+21);

            String gender = (i % 2 == 0) ? "男" : "女";
            s1.setGender(gender);
            s1.setWeight(RandomUtil.randomDouble(90,2)+60);
            s1.setCreateTime(new Date());

            studentDao.save(s1);
        }

    }

    @Test
    public void testQuery() {
        List<Student> students = studentDao.queryAll();
        System.out.println("result->" + JSON.toJSONString(students));
    }

    /**
     * 分页查询
     */
    @Test
    public void testPageQuery() {
        int page = 3;
        int pageSize = 10;
        PageInfo<Student> pageInfo = PageHelper.startPage(page, pageSize).doSelectPageInfo(() -> studentDao.queryAll());
        System.out.println("result->" + JSON.toJSONString(pageInfo));
    }
}
