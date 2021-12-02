~~数据库隔离级别~~
~~幻读~~
mysql update语句 binlog 与 redolog一致性问题
undo log如何保证原子性
redo log和undo log区别
redo log和undo log是如何生成的

性能优化（SQL、并发）
慢SQL优化
数据库的锁
乐观锁和悲观锁
数据库B+树、B树和B+树区别、为什么选择B+树不选择B树、红黑树
数据库两种引擎，索引结构
sql两种引擎区别
聚簇索性和非聚簇索性
联合索引B+树索引图
画出RC和RR隔离级别下的MVCC
InnoDB优点
MyISAM索引底层是什么结构
MySQL如何支持事务
~~MySQL如何解决脏读、不可重复读、幻读~~
如何解决脏读？（读已提交）MySQL如何判断事务有没有提交？事务A中对id=1进行修改，不提交；事务B中读取id=1的数据，如何判断这个数据有没有被提交？
索引下推
MyISAM 和 InnoDB 比较
mysql的主键，唯一索引区别，怎么建索引
一条sql怎么优化
数据库的范式？【三大范式】
数据库事务，ACID，mvcc
**mysql怎么实现主从复制？**
**主从复制是怎么实现同步的 后续数据更新怎么同步**
mysql如何保证acid
~~间隙锁和nextkey锁~~
~~mysql锁是锁的什么~~
怎么实现读写分离
哪些字段适合建索引，索引失效
联合索引底层数据结构
慢查询优化
组合索引+ like 会不会导致索引失效
索引失效的情况
数据库读写分离
MySQL原子性怎么保证
数据库是如何实现分页的,假设有100万条数据如何优化分页查询
B+树的叶子节点链表是单向还是双向？双向，用于倒序
更新是如何保证一致的？当前读
如何查询慢sql产生的原因
mysql里面的数据类型
mysql 里面时间是怎么存储的
innodb的索引是什么，b和b+树的区别，为什么b+树更矮，将某个值放入b+树的节点，树结构会不会发生变化聚簇索引的节点是用什么数据结构的
两个用=判断的可以变换顺序
next-key lock的上锁区间是如何确定的
匹配组合索引怎么匹配 怎么走
索引建立的原则
分表的方法
树高三层能索引千万级别，是怎么计算的
MySQL是将索引加载到内存中，是按页存储的，那么每页的内存占多大
MySQL索引，索引优化，索引失效，explain
事务的隔离级别，MVCC原理，InnoDB索引，是不是所有场景都B+树适用
大数据量分库分表方式，作用
数据库灾备方案
Myisam 和innodb， memory区别
mysql中行锁，表锁，及使用情况
举例innodb中行锁，表锁的使用情场景（面试官：where索引主键：行锁，where索引列：表锁，无where索引：表锁，EXPLAIN中的type字段：七大访问级别）
对于 读读， 读写， 写写，mvcc会发生线程不安全问题
redo log 和 undo log分别实现了数据库的什么
B+树和hash表的对比
前缀索引长度选择
自增主键
mysql事务acid的底层实现
MySQL三种log日志以及作用
千万数据实现分页查询，以及与单纯只用limit分页的区别
mysql实现事务回滚的两个文件
mysql对B+树做了哪些优化
mysql为啥要用b+tree 不用跳表
数据库读写分离的优缺点
redoLog, undoLog, binLog 详细区别，主从同步的过程，是拉还是推
MySQL索引优化，如何优化
数据库中select count(*). count(id) count(1)的区别
Mysql了解吗，讲一下事务，那mysql是如何保证ACID的呢（答了undo-log,redo-log,加锁,mvcc），那讲一下MVCC
你提到了隐藏列有一个DB_ROW_ID，是干嘛的？那假设有10个update，到第九个回滚了，DB_ROLL_PTR如何做的，那提交了是否更新DB_ROLL_PTR
讲一下索引及其底层，非叶子节点存储的是什么，只有b+树索引吗？（MEMORY是hash索引）
讲一下Mysql聚集索引与非聚集索引，主键索引使用int与string有啥区别，你刚才说了索引底层b+树的结构，那么使用String会不会影响到这个结构
什么情况下我们会去打破数据库的三范式
问数据库索引（平衡多叉树的数据插入删除问题）
sql之left join、right join、inner join的区别
数据库查询优化，添加索引，explain
为什么用建议用自增ID作索引而不用UUID