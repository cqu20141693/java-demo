SELECT * FROM L_STUDENT;

CREATE INDEX stu_include_name_index ON L_STUDENT(name);
SELECT * FROM stu_include_name_index;
explain SELECT name FROM L_STUDENT WHERE NAME='晏奇_01'
explain SELECT name,WEIGHT FROM L_STUDENT WHERE NAME='晏奇_01'

CREATE INDEX stu_include_index ON L_STUDENT(name,create_time) INCLUDE(age,gender);
SELECT * FROM stu_include_index;
explain SELECT name,gender FROM L_STUDENT WHERE NAME='晏奇_01'
explain SELECT name,gender,WEIGHT FROM L_STUDENT WHERE NAME='晏奇_01'

DROP INDEX stu_func_index ON L_STUDENT
CREATE INDEX stu_func_index ON L_STUDENT(TO_CHAR(CREATE_TIME,'yyyy-MM-dd'))
SELECT * FROM stu_func_index;
explain SELECT * FROM L_STUDENT WHERE TO_CHAR(CREATE_TIME,'yyyy-MM-dd')='2020-09-01'

CREATE INDEX stu_func_str_index ON L_STUDENT(UPPER(name||'_'||gender))
SELECT * FROM stu_func_str_index;
explain SELECT UPPER(name||'_'||gender) FROM L_STUDENT WHERE UPPER(name||'_'||gender)='晏奇_0_男'


CREATE LOCAL INDEX stu_local_name_idx ON L_STUDENT (name)
SELECT * FROM stu_local_name_idx;
explain SELECT * FROM L_STUDENT WHERE NAME='晏奇_1'

CREATE LOCAL INDEX stu_local_func_str_index ON L_STUDENT(UPPER(name||'_'||gender))
SELECT * FROM stu_local_func_str_index;
explain SELECT * FROM L_STUDENT WHERE UPPER(name||'_'||gender)='晏奇_0_男'

-- a:正常
-- b:重建
-- x:失效
select TABLE_NAME,DATA_TABLE_NAME,INDEX_TYPE,INDEX_STATE,INDEX_DISABLE_TIMESTAMP from system.catalog where INDEX_TYPE is not null;