#database
数据库包，该包封装了对数据库的增删改查操作
外部需要操作数据库时，只能通过ContentProvider的方式进行操作，使用ContentProvider是为了统一操作接口

##基本类说明
该包的基本类包括：
> BaseDbTable
> DatabaseHelper
> DatabaseManager
> GanHuoContentProvider

新建项目时，需要将上述四个类拷贝过去，因为使用了一些工具类，所以如果有报错，根据需要拷贝 utils 包中的类

**BaseDbTable** 该类主要封装了数据库表的升级策略，数据库版本升级是个常见问题，该类默认实现的升级策略支持删除旧表，
增加新表，在旧表中新增或删除列属性，更多信息可以查看类注释。该类权限为包权限，对外部隐藏升级的实现。需要注意，以后新增的数据库表
都必须继承该类。

**DatabaseHelper** 该类继承 SQLiteOpenHelper 类，使用 SQLite 数据的实现方式，就不过多介绍了。

**DatabaseManager** 该类管理着数据库中的所有表，以及管理着所有表的URI映射（ContentProvider需要用到），每次新增表
都需要对该类进行更新维护。

**GanHuoContentProvider** 继承自ContentProvider，类名根据需要更改，统一访问数据库的接口。

#使用该框架说明
该包封装的基本数据库框架，如果要使用的话，有下面几点需要注意：
1. 每次新增表都必须继承自 BaseDbTable
2. 每次新增或删除旧表时都必须对 DatabaseManager 进行更新维护
3. 建议每张表对应一个 Dao 类，Dao 类主要封装对该表的基本操作，外部只能通过 Dao 类操作数据库对象，对数据库的内部实现并不清楚
