package com.wujt;


import com.alibaba.fastjson.JSON;
import com.wujt.phoenixhbase.dao.LStudentMapper;
import com.wujt.phoenixhbase.entity.LStudent;
import com.wujt.phoenixhbase.entity.LStudentExample;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author luolh
 * @version 1.0
 * @since 2020/9/4 17:27
 */
@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = App.class)
public class StudentMapperTest {
    private static AtomicInteger count = new AtomicInteger(0);

    @Resource
    private LStudentMapper studentMapper;

    @Test
    public void testUpsert() {
        for (int i = 0; i < 30; i++) {
            LStudent s1 = new LStudent();
            s1.setId(count.getAndIncrement());
            s1.setName("晏奇_" + i);
            s1.setAge(RandomUtil.getRandomNumer(50) + 21);

            String gender = (i % 2 == 0) ? "男" : "女";
            s1.setGender(gender);
            s1.setWeight(RandomUtil.randomDouble(90,2)+60);
            s1.setCreateTime(new Date());

            studentMapper.upsert(s1);
        }
    }

    @Test
    public void testUpsertSelective() {
        LStudent s1 = new LStudent();
        s1.setId(30);
        s1.setName("晏奇_" + 30);
        s1.setCreateTime(new Date());

        studentMapper.upsertSelective(s1);
    }

    @Test
    public void testSelectByExample() {
        LStudentExample studentExample = new LStudentExample();
        LStudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andNameLike("%1%");
        criteria.andAgeGreaterThan(10);
        criteria.andCreateTimeLessThanOrEqualTo(new Date());
        List<LStudent> students = studentMapper.selectByExample(studentExample);
        System.out.println("result-->" + JSON.toJSONString(students));
    }

    @Test
    public void testSelectByPrimaryKey() {
        LStudent student = studentMapper.selectByPrimaryKey(11);
        System.out.println("result-->" + JSON.toJSONString(student));
    }

    @Test
    public void testCountByExample() {
        LStudentExample studentExample = new LStudentExample();
        LStudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andNameLike("%1%");
        criteria.andAgeGreaterThan(10);
        criteria.andCreateTimeLessThanOrEqualTo(new Date());
        long cn = studentMapper.countByExample(studentExample);
        System.out.println("result-->" + JSON.toJSONString(cn));
    }

    @Test
    public void testDeleteByPrimaryKey() {
        int delRow = studentMapper.deleteByPrimaryKey(0);
        System.out.println("result-->" + JSON.toJSONString(delRow));
    }

    @Test
    public void testDeleteByExample() {
        LStudentExample studentExample = new LStudentExample();
        LStudentExample.Criteria criteria = studentExample.createCriteria();
        criteria.andIdGreaterThan(29);
        int delRow = studentMapper.deleteByExample(studentExample);
        System.out.println("result-->" + JSON.toJSONString(delRow));
    }

    /**
     * 只能批量插入所有不为空的字段
     */
    @Test
    public void testBatchUpsertAll() {
        List<LStudent> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            LStudent s1 = new LStudent();
            s1.setId(i);
            s1.setName("晏奇_" + i);
            s1.setAge(RandomUtil.getRandomNumer(50) + 21);

            String gender = (i % 2 == 0) ? "男" : "女";
            s1.setGender(gender);
            s1.setWeight(RandomUtil.randomDouble(90,2)+60);
            s1.setCreateTime(new Date());

            list.add(s1);
        }

        int i = studentMapper.batchUpsert(list);
        System.out.println("result-->" + i);
    }

    @Test
    public void testBatchUpsertSelectiveAll() {
        List<LStudent> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            LStudent s1 = new LStudent();
            s1.setId(i);
            s1.setName("luolh_" + i);
            s1.setCreateTime(new Date());
            list.add(s1);
        }

        int i = studentMapper.batchUpsertSelective(list,
                LStudent.Column.id,
                LStudent.Column.name,
                LStudent.Column.createTime);
        System.out.println("result-->" + i);
    }

    @Test
    public void testBatchUpsertNotAll() {
        List<LStudent> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            LStudent s1 = new LStudent();
            s1.setId(i);
            s1.setName("晏奇_" + i);

            list.add(s1);
        }

        int i = studentMapper.batchUpsert(list);
        System.out.println("result-->" + i);
    }

    @Test
    public void testBatchUpsertSelectiveNotAll() {
        List<LStudent> list = new ArrayList<>();
        for (int i = 1; i <= 30; i++) {
            LStudent s1 = new LStudent();
            s1.setId(i);
            s1.setName("luolh_new_" + i);
            list.add(s1);
        }

        int i = studentMapper.batchUpsertSelective(list,
                LStudent.Column.id,
                LStudent.Column.name,
                LStudent.Column.createTime);
        System.out.println("result-->" + i);
    }
}
