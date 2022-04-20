## cassandra
### sql
``` 
创建Cassandra键空间
CREATE KEYSPACE IF NOT EXISTS cc_iot WITH replication = {'class':'SimpleStrategy', 'replication_factor':1};

 创建表
use cc_iot;
 自定义类型
 CREATE TYPE cc_iot.superpower (
    strength text,
    durability text,
    canFly boolean
);
DROP TABLE tutorial;
CREATE TABLE tutorial(
   id bigint PRIMARY KEY,
   name text,
   super_name text,
   profession text,
   age int
   super_powers frozen<superpower>
);


```
### 数据类型
``` 
int 
bigint
text
boolean
UDT

Cassandra自动数据到期Cassandra提供了数据可以自动过期的功能。
在数据插入期间，您必须以秒为单位指定“ttl”值。 ‘ttl‘值是数据生存价值的时间。 在这段时间之后，数据将被自动删除。

```