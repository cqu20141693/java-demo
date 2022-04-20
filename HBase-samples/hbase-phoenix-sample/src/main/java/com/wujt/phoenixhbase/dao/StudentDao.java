package com.wujt.phoenixhbase.dao;


import com.wujt.phoenixhbase.entity.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author luolh
 * @version 1.0
 * @since 2020/8/24 18:12
 */
@Mapper
public interface StudentDao {
    @Insert("UPSERT INTO l_student VALUES( #{id},#{name}, #{age}, #{gender} , #{weight}, #{createTime})")
    void save(Student student);

    @Select("SELECT * from l_student")
    List<Student> queryAll();
}
