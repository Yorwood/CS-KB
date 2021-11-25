##### MySql架构以及sql查询语句的执行流程

- 架构

  MySql由客户端、**服务器层**、**存储引擎层**组成，其中服务器层包括了连接器、查询缓存、分析器、优化器、执行器等功能，所有跨存储引擎的功能都在服务器实现，存储引擎层负责数据的存储和提取，其架构模式是插件式，支持InnoDB、MyISAM、Memory等多个存储引擎。

- sql语句执行流程

  1）首先客户端通过**连接器**连接到数据库，进行用户身份认证、权限查询等，用户认证成果后建立连接；

  - 连接分为长连接和短链接，**长连接**是指连接成功后，客户端的如有持续请求，则一直**使用同一个连接**；短链接是指每次执行完很少的几次查询就断开连接，下次查询再重新建立一个。建立连接是一个较为复杂的过程，因此尽量使用长连接，然而MySql在执行过程中使用的**临时内存**是管理在连接中的，因此长连接可能会持有大量的内存资源，直到连接断开时才释放，可能会导致OOM。针对上面问题，可以定期断开长连接，以释放资源，或者通过执行mysql_reset_connection来重新初始化连接资源(释放资源，但是该过程不需要重连和重新做权限验证)。

  2）**查询缓存**，建立连接后，MySql拿到查询请求后会先到查询缓存查看之前是否执行过该语句，如果该语句和其查询结果被缓存在内存中了，则直接返回查询结果给客户端。

  - 查询缓存一般不建议使用，因为每次表更新操作都会将查询缓存清空，查询命中率低，维护开销大，MySql8.0版本已经删除了该功能。

  3）**分析器**，没有命中查询缓存，则会执行分析器，MySql对sql语句进行词法、语法等解析，识别出表名、字段、存在性、语句是否合法等。

  4）**优化器**，在执行语句之前，会对有多个索引的表进行索引的选择、以及涉及到多表关联的语句进行表的连接顺序的选择，来优化执行效率，确定执行方案。

  5）**执行器**，开始执行查询语句，执行前判断表的执行权限，有权限则打开表继续执行，使用引擎提供的接口进行全表扫描查询或者是执行索引查询，返回查询结果给客户端。

  

<img src="C:\Users\18160\Desktop\YW\JAVA\KB\CS-KB\MySql\MySql45讲\fig\mysql-逻辑架构.png" style="zoom:50%;" />

##### redo log和binlog

- WAL技术(write-ahead logging)

  先写日志，再写磁盘策略

- redo log

  MySql中对于每条更新操作，如果直接将更新记录落盘，由于是随机写所以IO开销很高，InnoDB引擎有一个日志模块redo log（**固定大小的文件**），以追加的方式追加记录到redo log中(循环写，从头到尾部，满了则擦除)，因为是**顺序写**所以IO效率高。由于记录在**更新写入内存前已经写了redo log**，因此redo log可以在数据库宕机重启后，来恢复数据，即保证crash-safe。

  <img src="C:\Users\18160\Desktop\YW\JAVA\KB\CS-KB\MySql\MySql45讲\fig\redo log.png" style="zoom:50%;" />

  

- binlog

  redo log是InnoDB存储引擎特有的日志，而MySql自带的引擎是MyISAM，因此不具有crash-safe，MySql在服务器层也实现了一个日志binlog，只能用于归档功能。

- 两者的区别

  - redo log是InnoDB特有的，而binlog是MySql服务器层持有的所有引擎都可以用。

  - redo log是物理日志，记录的是"在某个数据页上做了什么修改"，binlog是逻辑日志，记录的是这个语句的原始逻辑(satement模式是记录sql，row是记录原数据行和新数据行)。

  - redo log是固定文件大小循环写，因此会擦除不会持久化，不能保证归档功能，而binlog是追加写入，即文件可以写满切换新文件继续写，不会覆盖以前的日志，因此可以进行归档。

    <img src="C:\Users\18160\Desktop\YW\JAVA\KB\CS-KB\MySql\MySql45讲\fig\redo log file.png" style="zoom:50%;" />

- 两阶段提交

  对于一条更新语句，首先根据主键索引ID，找到记录行，判断当前数据页是否内存命中，未命中则进行磁盘读入，返回记录，将该记录的值进行更新，先写入redo log，redo log处于prepare标记，然后再更新内存中记录的值，写binlog，redo log标记为commit状态。

  <img src="C:\Users\18160\Desktop\YW\JAVA\KB\CS-KB\MySql\MySql45讲\fig\两阶段提交.png" style="zoom: 67%;" />

- 小结

  <img src="C:\Users\18160\Desktop\YW\JAVA\KB\CS-KB\MySql\MySql45讲\fig\redo log流程.png" style="zoom: 67%;" />

  - redo log

    MySql执行更新操作时，会直接对读取到内存的缓存页进行修改，此时被修改过但还未刷新到磁盘的缓存页称为脏页，后台线程等其它机制会负责将脏页刷新同步会磁盘；但是如果此时事务提交，前端收到事务成功的反馈，但此时脏页还没有刷新到磁盘，MySql挂了该怎么办？如何恢复脏页数据？

    redo log的作用就是用于解决恢复脏页数据的，当事务中有更新操作时，MySql会将该更新操作涉及到的XXX表空间中的XXX数据页XXX偏移量的地方做了XXX更新写入到redo log中(记录了物理层面的数据页、偏移量)，因此即时MySql宕机也能通过redo log来重建数据。

    其次，redo log也不是一条一条写入磁盘进行持久化的，而是按块写入，事务的redo log被写入**redo log buffer**（内存缓存）中的块中，每个事务中的多个更新操作产生的redo log被标记为一个redo log group。redo log buffer是内存缓存，怎么保证持久性?

    redo log buffer写入磁盘时机：

    1. **事物提交**时把它对应的那些redo log**写入到磁盘**中去（这个动作可由相关参数控制，下文会说）
    2. 当redo log buffer 使用量达到了参数`innndb_log_buffer_size`的一半时，会触发落盘。
    3. 会有一个后台线程，每隔1秒就会将redo log block刷新到磁盘文件中去。
    4. MySQL关闭时也会将其落盘

  
  - MySQL提供了参数`innodb_flush_log_at_trx_commit`

     该参数有几个选项：0、1、2

  想要保证ACID四大特性推荐设置为1：表示当你commit时，MySQL必须将rodolog-buffer中的数据刷新  进磁盘中。确保只要commit是成功的，磁盘上就得有对应的rodolog日志。这也是最安全的情况。

  设置为0：每秒写一次日志并将其刷新到磁盘

  设置为2：表示当你commit时，将redolog-buffer中的数据刷新进OS Cache中，然后依托于操作系统每秒刷新一次的机制将数据同步到磁盘中，也存在丢失的风险

  - binlog

    binlog也有缓存机制，也有参数设置控制一致性强弱，binlog也是顺序写文件，不过文件是追加方式，不会覆盖，因此可以做归档，而redo log会擦除而不能做归档。

- 参考资料
  - 《极客时间MySql45讲》
  - [谈谈传说中的redo log是什么？有啥用？ - 赐我白日梦 - 博客园 (cnblogs.com)](https://www.cnblogs.com/ZhuChangwu/p/14096575.html)

