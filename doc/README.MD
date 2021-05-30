Author：zhouyu

DateTime：2021-05-30

GitHub：https://github.com/TheCodeYu

# 第一章：注解相关

## 1.1	多重Bean问题

在遇到某个接口有多个实现类的时候，可以给各个Bean命名，然后在注入时指定需要使用的Bean。

# 第二章：mybatis框架

## 2.1 xml文件标签

### 2.1.1	顶层标签

SQL 映射文件只有很少的几个顶级元素（按照应被定义的顺序列出）：

- cache – 对给定命名空间的缓存配置。

- cache-ref – 对其他命名空间缓存配置的引用。

- resultMap – 是最复杂也是最强大的元素，用来描述如何从数据库结果集中来加载对象。

- parameterMap – 已被废弃！老式风格的参数映射。更好的办法是使用内联参数，此元素可能在将来被移除。

- sql – 可被其他语句引用的可重用语句块。

- insert – 映射插入语句

- update – 映射更新语句

- delete – 映射删除语句

- select – 映射查询语句

### 2.1.2	select

```xml
<select id="selectPerson" parameterType="int" resultType="hashmap">
   SELECT * FROM PERSON WHERE ID = #{id}
 </select>
// 返回类型是Map结构

// 当我们在查询并返回一条数据的时候，可以把{字段名，字段值}封装成Map结构
Map<String, Object> findUserByName(Integer id);  // mapper 接口
<select id="findUserByName" resultType="string">
    select userName from user where id=#{id};
</select>

// 返回是List类型
List<User> getUsers();  // mapper 接口
// SQL映射文件，这里需要注意的是返回是List类型 但是resultType依然是javaBean
<select id="getUsers" resultType="com.tx.entity.User">
    select * from user
</select>
    
// 可以把参数封装到Map里面 有些时候我们的业务数据查询没有定义对应的POJO，就进行参数的封装操作。
public List<User> findUser1(Map<String, Object> map);
// 对应的SQL文件：
<select id="findUser1" parameterType="java.util.Map" resultType="com.tx.springboottestdemo.entity.User">
    select * from user_test where userName = #{username} and realName = #{realname}
</select>
```

这个语句被称作 selectPerson，接受一个 int（或 Integer）类型的参数，并返回一个 HashMap 类型的对象，其中的键是列名，值便是结果行中的对应值。\#{id}告诉 MyBatis 创建一个预处理语句（PreparedStatement）参数，在 JDBC 中，这样的一个参数在 SQL 中会由一个“?”来标识，并被传递到一个新的预处理语句。

| 属性          | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| id            | 在命名空间中唯一的标识符，可以被用来引用这条语句。           |
| parameterType | 将会传入这条语句的参数类的完全限定名或别名。这个属性是可选的，因为 MyBatis 可以通过类型处理器（TypeHandler） 推断出具体传入语句的参数，默认值为未设置（unset）。基本数据类型，包装类，String类，POJO类，多个参数时可以使用参数下标，或者在mapper文件中使用@Param(“”)指定，可以将多个参数进行包装传入，此时就可以直接使用#{}。 |
| parameterMap  | 这是引用外部 parameterMap 的已经**被废弃**的方法。请使用内联参数映射和 parameterType 属性。 |
| resultType    | 从这条语句中返回的期望类型的类的完全限定名或别名。 注意如果返回的是集合，那应该设置为集合包含的类型，而不是集合本身。可以使用 resultType 或 resultMap，**但不能同时使用**。 |
| resultMap     | 外部 resultMap 的命名引用。结果集的映射是 MyBatis 最强大的特性，如果你对其理解透彻，许多复杂映射的情形都能迎刃而解。可以使用 resultMap 或 resultType，**但不能同时使用**。 |
| flushCache    | 将其设置为 true 后，只要语句被调用，都会导致本地缓存和二级缓存被清空，默认值：false。 |
| useCache      | 将其设置为 true 后，将会导致本条语句的结果被二级缓存缓存起来，默认值：对 select 元素为 true。 |
| timeout       | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为未设置（unset）（依赖驱动） |
| fetchSize     | 这是一个给驱动的提示，尝试让驱动程序每次批量返回的结果行数和这个设置值相等。 默认值为未设置（unset）（依赖驱动）。 |
| statementType | STATEMENT，PREPARED 或 CALLABLE 中的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。 |
| resultSetType | FORWARD_ONLY，SCROLL_SENSITIVE, SCROLL_INSENSITIVE 或 DEFAULT（等价于 unset） 中的一个，默认值为 unset （依赖驱动）。 |
| databaseId    | 如果配置了数据库厂商标识（databaseIdProvider），MyBatis 会加载所有的不带 databaseId 或匹配当前 databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。 |
| resultOrdered | 这个设置仅针对嵌套结果 select 语句适用：如果为 true，就是假设包含了嵌套结果集或是分组，这样的话当返回一个主结果行的时候，就不会发生有对前面结果集的引用的情况。 这就使得在获取嵌套的结果集的时候不至于导致内存不够用。默认值：`false`。 |
| resultSets    | 这个设置仅对多结果集的情况适用。它将列出语句执行后返回的结果集并给每个结果集一个名称，名称是逗号分隔的。 |

### 2.1.3	insert, update 和 delete

```xml
<insert
   id="insertAuthor"
   parameterType="domain.blog.Author"
   flushCache="true"
   statementType="PREPARED"
   keyProperty=""
   keyColumn=""
   useGeneratedKeys=""
   timeout="20">
 
 <update
   id="updateAuthor"
   parameterType="domain.blog.Author"
   flushCache="true"
   statementType="PREPARED"
   timeout="20">
 
 <delete
   id="deleteAuthor"
   parameterType="domain.blog.Author"
   flushCache="true"
   statementType="PREPARED"
   timeout="20">
```

| 属性             | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| id               | 命名空间中的唯一标识符，可被用来代表这条语句。               |
| parameterType    | 将要传入语句的参数的完全限定类名或别名。这个属性是可选的，因为 MyBatis 可以通过类型处理器推断出具体传入语句的参数，默认值为未设置（unset）。 |
| parameterMap     | 这是引用外部 parameterMap 的已经**被废弃**的方法。请使用内联参数映射和 parameterType 属性。 |
| flushCache       | 将其设置为 true 后，只要语句被调用，都会导致本地缓存和二级缓存被清空，默认值：true（对于 insert、update 和 delete 语句）。 |
| timeout          | 这个设置是在抛出异常之前，驱动程序等待数据库返回请求结果的秒数。默认值为未设置（unset）（依赖驱动）。 |
| statementType    | STATEMENT，PREPARED 或 CALLABLE 的一个。这会让 MyBatis 分别使用 Statement，PreparedStatement 或 CallableStatement，默认值：PREPARED。 |
| useGeneratedKeys | （仅对 insert 和 update 有用）这会令 MyBatis 使用 JDBC 的 getGeneratedKeys 方法来取出由数据库内部生成的主键（比如：像 MySQL 和 SQL Server 这样的关系数据库管理系统的自动递增字段），默认值：false。 |
| keyProperty      | （仅对 insert 和 update 有用）唯一标记一个属性，MyBatis 会通过 getGeneratedKeys 的返回值或者通过 insert 语句的 selectKey 子元素设置它的键值，默认值：未设置（unset）。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| keyColumn        | （仅对 insert 和 update 有用）通过生成的键值设置表中的列名，这个设置仅在某些数据库（像 PostgreSQL）是必须的，当主键列不是表中的第一列的时候需要设置。如果希望使用多个生成的列，也可以设置为逗号分隔的属性名称列表。 |
| databaseId       | 如果配置了数据库厂商标识（databaseIdProvider），MyBatis 会加载所有的不带 databaseId 或匹配当前 databaseId 的语句；如果带或者不带的语句都有，则不带的会被忽略。 |

### 2.1.4	生成主键

```xml
<selectKey keyProperty="id" resultType="int" order="BEFORE" statementType="PREPARED">
    
<selectKey resultType="java.lang.Long" order="AFTER" keyProperty="id">
   SELECT LAST_INSERT_ID() AS id
 </selectKey>
```

| 属性          | 描述                                                         |
| ------------- | ------------------------------------------------------------ |
| keyProperty   | selectKey 语句结果应该被设置的目标属性。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| keyColumn     | 匹配属性的返回结果集中的列名称。如果希望得到多个生成的列，也可以是逗号分隔的属性名称列表。 |
| resultType    | 结果的类型。MyBatis 通常可以推断出来，但是为了更加精确，写上也不会有什么问题。MyBatis 允许将任何简单类型用作主键的类型，包括字符串。如果希望作用于多个生成的列，则可以使用一个包含期望属性的 Object 或一个 Map。 |
| order         | 这可以被设置为 BEFORE 或 AFTER。如果设置为 BEFORE，那么它会首先生成主键，设置 keyProperty 然后执行插入语句。如果设置为 AFTER，那么先执行插入语句，然后是 selectKey 中的语句 - 这和 Oracle 数据库的行为相似，在插入语句内部可能有嵌入索引调用。 |
| statementType | 与前面相同，MyBatis 支持 STATEMENT，PREPARED 和 CALLABLE 语句的映射类型，分别代表 PreparedStatement 和 CallableStatement 类型。 |

### 2.1.5	sql include

**这个元素可以被用来定义可重用的 SQL 代码段，**这些 SQL 代码可以被包含在其他语句中。它可以（在加载的时候）被静态地设置参数，在不同的包含语句中可以设置不同的值到参数占位符上

```xml
<sql id="userColumns"> ${alias}.id,${alias}.username,${alias}.password </sql>
<select id="selectUsers" resultType="map">
   select
     <include refid="userColumns"><property name="alias" value="t1"/></include>,
     <include refid="userColumns"><property name="alias" value="t2"/></include>
   from some_table t1
     cross join some_table t2
 </select>
```

### 2.1.6	resultMap

resultMap 元素是 MyBatis 中最重要最强大的元素，在一些情形下允许你进行一些 JDBC 不支持的操作。实际上，在为一些比如连接的复杂语句编写映射代码的时候，一份 resultMap 能够代替实现同等功能的长达数千行的代码。ResultMap 的设计思想是，对于简单的语句根本不需要配置显式的结果映射，而对于复杂一点的语句只需要描述它们的关系就行了。

在JavaBean中定义一个有 3 个属性：id，username 和 hashedPassword的类，然后在mapper.xml中，这些属性会对应到 select 语句中的列名，这样的一个 JavaBean 可以被映射到 ResultSet，就像映射到 HashMap 一样简单

```xml
<select id="selectUsers" resultType="com.someapp.model.User">
   select id, username, hashedPassword
   from some_table
   where id = #{id}
 </select>

<resultMap id="userResultMap" type="User">
   <id property="id" column="user_id" />
   <result property="username" column="user_name"/>
   <result property="password" column="hashed_password"/>
</resultMap>
<select id="selectUsers" resultMap="userResultMap">
   select user_id, user_name, hashed_password
   from some_table
   where id = #{id}
</select>
```

resultMap属性：

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| id          | 当前命名空间中的一个唯一标识，用于标识一个结果映射。         |
| type        | 类的完全限定名, 或者一个类型别名（关于内置的类型别名，可以参考上面的表格）。 |
| autoMapping | 如果设置这个属性，MyBatis将会为本结果映射开启或者关闭自动映射。 这个属性会覆盖全局的属性 autoMappingBehavior。默认值：未设置（unset）。 |

constructor-用于在实例化类时，注入结果到构造方法中

idArg - ID 参数；标记出作为 ID 的结果可以帮助提高整体性能

arg - 将被注入到构造方法的一个普通结果

id – 一个 ID 结果；标记出作为 ID 的结果可以帮助提高整体性能

result – 注入到字段或 JavaBean 属性的普通结果

**association**（一对一）– 一个复杂类型的关联；许多结果将包装成这种类型

嵌套结果映射 – 关联本身可以是一个 resultMap 元素，或者从别处引用一个

**collection**（一对多）– 一个复杂类型的集合

嵌套结果映射 – 集合本身可以是一个 resultMap 元素，或者从别处引用一个

discriminator– 使用结果值来决定使用哪个resultMap

case– 基于某些值的结果映射

嵌套结果映射 – case 本身可以是一个 resultMap 元素，因此可以具有相同的结构和元素，或者从别处引用一个。

id 和 result 元素都将一个列的值映射到一个简单数据类型（String, int, double, Date 等）的属性或字段。

这两者之间的唯一不同是，id 元素表示的结果将是对象的标识属性，这会在比较对象实例时用到。 这样可以提高整体的性能，尤其是进行缓存和嵌套结果映射（也就是连接映射）的时候。

| 属性        | 描述                                                         |
| ----------- | ------------------------------------------------------------ |
| property    | 映射到列结果的字段或属性。如果用来匹配的 JavaBean 存在给定名字的属性，那么它将会被使用。否则 MyBatis 将会寻找给定名称的字段。 无论是哪一种情形，你都可以使用通常的点式分隔形式进行复杂属性导航。 比如，你可以这样映射一些简单的东西：“username”，或者映射到一些复杂的东西上：“address.street.number”。 |
| column      | 数据库中的列名，或者是列的别名。一般情况下，这和传递给 `resultSet.getString(columnName)` 方法的参数一样 |
| javaType    | 一个 Java 类的完全限定名，或一个类型别名（关于内置的类型别名，可以参考上面的表格）。 如果你映射到一个 JavaBean，MyBatis 通常可以推断类型。然而，如果你映射到的是 HashMap，那么你应该明确地指定 javaType 来保证行为与期望的相一致。 |
| jdbcType    | JDBC 类型，所支持的 JDBC 类型参见这个表格之后的“支持的 JDBC 类型”。 只需要在可能执行插入、更新和删除的且允许空值的列上指定 JDBC 类型。这是 JDBC 的要求而非 MyBatis 的要求。如果你直接面向 JDBC 编程，你需要对可能存在空值的列指定这个类型。 |
| typeHandler | 我们在前面讨论过默认的类型处理器。使用这个属性，你可以覆盖默认的类型处理器。 这个属性值是一个类型处理器实现类的完全限定名，或者是类型别名。 |

#### **Association和Collection使用区别**

Association 在一对一,多对一时使用

Collection 在一对多,多对多时使用

在association标签中 javaType属性指向的是实体类的属性

在collection标签中 javaType属性指向的是集合的类型 ofType指向的是集合的泛型类型

```xml

<!--直接通过sql查询,并且直接通过属性名点. 的形式进行赋值-->
    <resultMap id="resultMap1" type="com.yyc.platform.user.model.Emp">
        <id column="id" property="id"/>
        <result column="emp_name" property="empName"/>
        <result column="name" property="name"/>
        <result column="sex" property="sex"/>
        <result column="email" property="email"/>
        <result column="dept_id" property="dept.id"/>
        <result column="dept_name" property="dept.deptName"/>
    </resultMap>
 
    <select id="getEmpById1" resultMap="resultMap1">
         SELECT e.id  ,e.emp_name ,e.email ,e.sex,e.dept_id ,d.dept_name
         FROM employee e
         LEFT JOIN dept d  on e.dept_id=d.id
         where e.id=#{id}
    </select>

<!-- 通过使用association 标签直接进行赋值-->
<resultMap id="resultMap2" type="com.yyc.platform.user.model.Emp">
        <id column="id" property="id"/>
        <result column="emp_name" property="empName"/>
        <result column="name" property="name"/>
        <result column="sex" property="sex"/>
        <result column="email" property="email"/>
        <association property="dept" javaType="com.yyc.platform.user.model.Dept">
            <id column="dept_id" property="id"/>
            <result column="dept_name" property="deptName"/>
        </association>
    </resultMap>
 
    <select id="getEmpById2" resultMap="resultMap2">
         SELECT e.id  ,e.emp_name ,e.email ,e.sex,e.dept_id ,d.dept_name
         FROM employee e
         LEFT JOIN dept d  on e.dept_id=d.id
         where e.id=#{id}
    </select>

<!--使用association 中的select进行分步查询,参数就是员工表中的id
第三步需要在 部门xml中提供者个getDeptById 的方法,如下:-->
<resultMap id="resultMap3" type="com.yyc.platform.user.model.Emp">
        <id column="id" property="id"/>
        <result column="emp_name" property="empName"/>
        <result column="name" property="name"/>
        <result column="sex" property="sex"/>
        <result column="email" property="email"/>
        <!--会根据查询到的dept_id 作为参数 去找这个方法com.yyc.platform.user.mapper.DeptMapper.getDeptById 然后进行查询-->
        <association property="dept" select="com.yyc.platform.user.mapper.DeptMapper.getDeptById" column="dept_id">
        </association>
    </resultMap>
 
    <select id="getEmpById3" resultMap="resultMap3">
         SELECT e.id  ,e.emp_name ,e.email ,e.sex,e.dept_id
         FROM employee e
         where e.id=#{id}
    </select>

 <!--只查询部门的信息-->
    <resultMap id="resultMapDept" type="com.yyc.platform.user.model.Dept">
        <id column="id" property="id"/>
        <result column="dept_name" property="deptName"/>
    </resultMap>
 
    <select id="getDeptById" resultMap="resultMapDept">
        select id ,dept_name from dept where id =#{id}
    </select>
```



### 2.1.7	缓存

默认情况下，只启用了本地的会话缓存，它仅仅对一个会话中的数据进行缓存。 要启用全局的二级缓存，只需要在你的 SQL 映射文件中添加一行：<cache/>

基本上就是这样。这个简单语句的效果如下:

- 映射语句文件中的所有 select 语句的结果将会被缓存。

- 映射语句文件中的所有 insert、update 和 delete 语句会刷新缓存。

- 缓存会使用最近最少使用算法（LRU, Least Recently Used）算法来清除不需要的缓存。

- 缓存不会定时进行刷新（也就是说，没有刷新间隔）。

- 缓存会保存列表或对象（无论查询方法返回哪种）的 1024 个引用。

- 缓存会被视为读/写缓存，这意味着获取到的对象并不是共享的，可以安全地被调用者修改，而不干扰其他调用者或线程所做的潜在修改。

**提示：** 缓存只作用于 cache 标签所在的映射文件中的语句。如果你混合使用 Java API 和 XML 映射文件，在共用接口中的语句将不会被默认缓存。你需要使用 @CacheNamespaceRef 注解指定缓存作用域。

```xml
<cache eviction="FIFO" flushInterval="60000" size="512" readOnly="true"/>
```

这个更高级的配置创建了一个 FIFO 缓存，每隔 60 秒刷新，最多可以存储结果对象或列表的 512 个引用，而且返回的对象被认为是只读的，因此对它们进行修改可能会在不同线程中的调用者产生冲突。

可用的清除策略有：

- LRU – 最近最少使用：移除最长时间不被使用的对象。

- FIFO – 先进先出：按对象进入缓存的顺序来移除它们。

- SOFT – 软引用：基于垃圾回收器状态和软引用规则移除对象。

- WEAK – 弱引用：更积极地基于垃圾收集器状态和弱引用规则移除对象。


默认的清除策略是 LRU。

flushInterval（刷新间隔）属性可以被设置为任意的正整数，设置的值应该是一个以毫秒为单位的合理时间量。 默认情况是不设置，也就是没有刷新间隔，缓存仅仅会在调用语句时刷新。

size（引用数目）属性可以被设置为任意正整数，要注意欲缓存对象的大小和运行环境中可用的内存资源。默认值是 1024。

readOnly（只读）属性可以被设置为 true 或 false。只读的缓存会给所有调用者返回缓存对象的相同实例。 因此这些对象不能被修改。这就提供了可观的性能提升。而可读写的缓存会（通过序列化）返回缓存对象的拷贝。 速度上会慢一些，但是更安全，因此默认值是 false。

提示 二级缓存是事务性的。这意味着，当 SqlSession 完成并提交时，或是完成并回滚，但没有执行 flushCache=true 的 insert/delete/update 语句时，缓存会获得更新。

#### 自定义缓存

除了上述自定义缓存的方式，你也可以通过实现你自己的缓存，或为其他第三方缓存方案创建适配器，来完全覆盖缓存行为。

 <cache type="com.domain.something.MyCustomCache"/>

```java
public interface Cache {
   String getId();
   int getSize();
   void putObject(Object key, Object value);
   Object getObject(Object key);
   boolean hasKey(Object key);
   Object removeObject(Object key);
   void clear();
 }
```

type 属性指定的类必须实现 org.mybatis.cache.Cache 接口，且提供一个接受 String 参数作为 id 的构造器。

### 2.1.8	动态SQL

- Mybatis 动态 SQL ，可以让我们在 XML 映射文件内，**以 XML 标签的形式编写动态 SQL ，完成逻辑判断和动态拼接 SQL 的功能。**
- Mybatis 提供了 9 种动态 SQL 标签：<if />、<choose />、<when />、<otherwise />、<trim />、<where />、<set />、<foreach />、<bind /> 。
- 其执行原理为，使用 **OGNL** 的表达式，从 SQL 参数对象中计算表达式的值，根据表达式的值动态拼接 SQL ，以此来完成动态 SQL 的功能。

#### 2.1.8.1	if

动态 SQL 通常要做的事情是根据条件包含 where 子句的一部分。

```xml
<select id="findActiveBlogWithTitleLike" resultType="Blog">
   SELECT * FROM BLOG WHERE state = ‘ACTIVE’
   <if test="title != null">
     AND title like #{title}
   </if>
 </select>
```

这条语句提供了一种可选的查找文本功能。**如果没有传入“title”，那么所有处于“ACTIVE”状态的BLOG都会返回；反之若传入了“title”，那么就会对“title”一列进行模糊查找并返回 BLOG 结果**（“title”参数值是可以包含一些掩码或通配符的）。

#### 2.1.8.2	choose, when, otherwise

有时我们不想应用到所有的条件语句，而只想从中择其一项。针对这种情况，MyBatis 提供了 choose 元素，它有点像 Java 中的 switch 语句。

这次变为提供了“title”就按“title”查找，提供了“author”就按“author”查找的情形，若两者都没有提供，就返回所有符合条件的 BLOG（实际情况可能是由管理员按一定策略选出 BLOG 列表，而不是返回大量无意义的随机结果）。

```xml
<select id="findActiveBlogLike" resultType="Blog">
   SELECT * FROM BLOG WHERE state = ‘ACTIVE’
   <choose>
     <when test="title != null">
       AND title like #{title}
     </when>
     <when test="author != null and author.name != null">
       AND author_name like #{author.name}
     </when>
   	 <otherwise>
       AND featured = 1
     </otherwise>
   </choose>
</select>
```

#### 2.1.8.3	trim, where, set

上面的几个例子，如果条件不满足的话，会拼凑成不成一条sql语句，导致无法查询，如：SELECT * FROM BLOG WHERE

```xml
<select id="findActiveBlogLike" resultType="Blog">
   SELECT * FROM BLOG
   <where>
     <if test="state != null">
          state = #{state}
     </if>
     <if test="title != null">
         AND title like #{title}
     </if>
     <if test="author != null and author.name != null">
         AND author_name like #{author.name}
     </if>
   </where>
</select>

 <trim prefix="WHERE" prefixOverrides="AND |OR ">
   ...
 </trim>

<update id="updateAuthorIfNecessary">
   update Author
     <set>
       <if test="username != null">username=#{username},</if>
       <if test="password != null">password=#{password},</if>
       <if test="email != null">email=#{email},</if>
       <if test="bio != null">bio=#{bio}</if>
     </set>
   where id=#{id}
</update>

<trim prefix="SET" suffixOverrides=",">
   ...
 </trim>
```

where 元素只会在至少有一个子元素的条件返回 SQL 子句的情况下才去插入“WHERE”子句。而且，若语句的开头为“AND”或“OR”，where 元素也会将它们去除。

如果 where 元素没有按正常套路出牌，我们可以通过自定义 trim 元素来定制 where 元素的功能。比如，和 where 元素等价的自定义 trim 元素为：

prefixOverrides、suffixOverrides 属性会忽略通过管道符分隔的文本序列（注意此例中的空格是必要的）。上述例子会移除所有 prefixOverrides(suffixOverrides) 属性中指定的内容，并且插入 prefix(suffix) 属性中指定的内容。

类似的用于动态更新语句的解决方案叫做 set。set 元素可以用于动态包含需要更新的列，而舍去其它的。

这里，*set* 元素会动态前置 SET 关键字，同时也会删掉无关的逗号，因为用了条件语句之后很可能就会在生成的 SQL 语句的后面留下这些逗号。（译者注：**因为用的是“if”元素，若最后一个“if”没有匹配上而前面的匹配上，SQL 语句的最后就会有一个逗号遗留**）

#### 2.1.8.4	foreach

动态 SQL 的另外一个常用的操作需求是对一个集合进行遍历，通常是在构建 IN 条件语句的时候

```xml
<select id="selectPostIn" resultType="domain.blog.Post">
   SELECT * FROM POST P WHERE ID in
   <foreach item="item" index="index" collection="list"
       open="(" separator="," close=")">
         #{item}
   </foreach>
 </select>

<insert id="batchInsert" parameterType="java.util.List" useGeneratedKeys="true" keyProperty="id">
    INSERT INTO user (name, age) VALUES
    <foreach collection="list" item="user" index="index" separator="," >
        (#{user.name}, #{user.age})
    </foreach>
</insert>
```

*foreach* 元素的功能非常强大，它允许你指定一个集合，声明可以在元素体内使用的集合项（item）和索引（index）变量。它也允许你指定开头与结尾的字符串以及在迭代结果之间放置分隔符。这个元素是很智能的，因此它不会偶然地附加多余的分隔符。

可以将任何可迭代对象（如 List、Set 等）、Map 对象或者数组对象传递给 foreach 作为集合参数。当使用可迭代对象或者数组时，index 是当前迭代的次数，item 的值是本次迭代获取的元素。当使用 Map 对象（或者 Map.Entry 对象的集合）时，index 是键，item 是值。

#### 2.1.8.5	bind

`bind` 元素可以从 OGNL 表达式中创建一个变量并将其绑定到上下文。

```xml
<select id="selectBlogsLike" resultType="Blog">
   <bind name="pattern" value="'%' + _parameter.getTitle() + '%'" />
   SELECT * FROM BLOG
   WHERE title LIKE #{pattern}
</select>
```



## 2.2	配置文件

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
"http://mybatis.org/dtd/mybatis-3-config.dtd">
<!-- 配置文件的根元素 -->
<configuration>
    <!-- 属性：定义配置外在化 -->
    <properties></properties>
    <!-- 设置：定义mybatis的一些全局性设置 -->
    <settings>
       <!-- 具体的参数名和参数值 -->
       <setting name="" value=""/> 
    </settings>
    <!-- 类型名称：为一些类定义别名 -->
    <typeAliases></typeAliases>
    <!-- 类型处理器：定义Java类型与数据库中的数据类型之间的转换关系 -->
    <typeHandlers></typeHandlers>
    <!-- 对象工厂 -->
    <objectFactory type=""></objectFactory>
    <!-- 插件：mybatis的插件,插件可以修改mybatis的内部运行规则 -->
    <plugins>
       <plugin interceptor=""></plugin>
    </plugins>
    <!-- 环境：配置mybatis的环境 -->
    <environments default="">
       <!-- 环境变量：可以配置多个环境变量，比如使用多数据源时，就需要配置多个环境变量 -->
       <environment id="">
          <!-- 事务管理器 -->
          <transactionManager type=""></transactionManager>
          <!-- 数据源 -->
          <dataSource type=""></dataSource>
       </environment> 
    </environments>
    <!-- 数据库厂商标识 -->
    <databaseIdProvider type=""></databaseIdProvider>
    <!-- 映射器：指定映射文件或者映射类 -->
    <mappers></mappers>
</configuration>
```

### 2.2.1	properties

properties元素主要是用来定义配置外在化，比如数据库的连接属性等。这些属性都是可外部配置且可动态替换的，既可以在典型的Java属性文件中配置，亦可以通过properties元素的子元素来传递。

```xml
<properties resource="org/mybatis/example/config.properties">
  <property name="username" value="dev_user"/>
  <property name="password" value="F2Fa3!33TYyg"/>
</properties>

<dataSource type="POOLED">
  <property name="driver" value="${driver}"/>
  <property name="url" value="${url}"/>
  <property name="username" value="${username}"/>
  <property name="password" value="${password}"/>
</dataSource>
```

username和password将会由properties元素中设置的相应值来替换。driver和url属性将会由config.properties文件中对应的值来替换。

加载顺序：

- 在properties元素体内指定的属性首先被读取。
- 根据properties元素中的resource属性读取类路径下属性文件或根据url属性指定的路径读取属性文件，并覆盖已读取的同名属性。
- 读取作为方法参数传递的属性，并覆盖已读取的同名属性。

因此，通过方法参数传递的属性具有最高优先级，resource/url属性中指定的配置文件次之，最低优先级的是properties属性中指定的属性。

### 2.2.2	settings

setting是指定MyBatis的一些全局配置属性，这是MyBatis中极为重要的调整设置，它们会改变MyBatis的运行时行为。

| 设置参数                  | 描述                                                         | 有效值                                                       | 默认值                                                       |
| ------------------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| cacheEnabled              | 该配置影响的所有映射器中配置的缓存的全局开关                 | true \|false                                                 | true                                                         |
| lazyLoadingEnabled        | 延迟加载的全局开关。当开启时，所有关联对象都会延迟加载。 特定关联关系中可通过设置`fetchType`属性来覆盖该项的开关状态 | true \|false                                                 | false                                                        |
| aggressiveLazyLoading     | 当启用时，对任意延迟属性的调用会使带有延迟加载属性的对象完整加载；反之，每种属性将会按需加载。 | true \|false                                                 | true                                                         |
| multipleResultSetsEnabled | 是否允许单一语句返回多结果集（需要兼容驱动）。               | true \|false                                                 | true                                                         |
| useColumnLabel            | 使用列标签代替列名。不同的驱动在这方面会有不同的表现， 具体可参考相关驱动文档或通过测试这两种不同的模式来观察所用驱动的结果。 | true \|false                                                 | true                                                         |
| useGeneratedKeys          | 允许 JDBC 支持自动生成主键，需要驱动兼容。 如果设置为 true 则这个设置强制使用自动生成主键，尽管一些驱动不能兼容但仍可正常工作（比如 Derby）。 | true \|false                                                 | False                                                        |
| autoMappingBehavior       | 指定 MyBatis 应如何自动映射列到字段或属性。 NONE 表示取消自动映射；PARTIAL 只会自动映射没有定义嵌套结果集映射的结果集。 FULL 会自动映射任意复杂的结果集（无论是否嵌套）。 | NONE, PARTIAL, FULL                                          | PARTIAL                                                      |
| defaultExecutorType       | 配置默认的执行器。SIMPLE 就是普通的执行器；REUSE 执行器会重用预处理语句（prepared statements）； BATCH 执行器将重用语句并执行批量更新。 | SIMPLE REUSE BATCH                                           | SIMPLE                                                       |
| defaultStatementTimeout   | 设置超时时间，它决定驱动等待数据库响应的秒数。               | Any positive integer                                         | Not Set (null)                                               |
| defaultFetchSize          | Sets the driver a hint as to control fetching size for return results. This parameter value can be override by a query setting. | Any positive integer                                         | Not Set (null)                                               |
| safeRowBoundsEnabled      | 允许在嵌套语句中使用分页（RowBounds）。                      | true \|false                                                 | False                                                        |
| mapUnderscoreToCamelCase  | 是否开启自动驼峰命名规则（camel case）映射，即从经典数据库列名 A_COLUMN 到经典 Java 属性名 aColumn 的类似映射。 | true \|false                                                 | False                                                        |
| localCacheScope           | MyBatis 利用本地缓存机制（Local Cache）防止循环引用（circular references）和加速重复嵌套查询。 默认值为 SESSION，这种情况下会缓存一个会话中执行的所有查询。 若设置值为 STATEMENT，本地会话仅用在语句执行上，对相同 SqlSession 的不同调用将不会共享数据。 | SESSION \|STATEMENT                                          | SESSION                                                      |
| jdbcTypeForNull           | 当没有为参数提供特定的 JDBC 类型时，为空值指定 JDBC 类型。 某些驱动需要指定列的 JDBC 类型，多数情况直接用一般类型即可，比如 NULL、VARCHAR 或 OTHER。 | JdbcType enumeration. Most common are: NULL, VARCHAR and OTHER | OTHER                                                        |
| lazyLoadTriggerMethods    | 指定哪个对象的方法触发一次延迟加载。                         | A method name list separated by commas                       | equals,clone,hashCode,toString                               |
| defaultScriptingLanguage  | 指定动态 SQL 生成的默认语言。                                | A type alias or fully qualified class name.                  | org.apache.ibatis.scripting.xmltags.XMLDynamicLanguageDriver |
| callSettersOnNulls        | 指定当结果集中值为 null 的时候是否调用映射对象的 setter（map 对象时为 put）方法，这对于有 Map.keySet() 依赖或 null 值初始化的时候是有用的。**注意基本类型（int、boolean等）是不能设置成 null 的**。 | true \|false                                                 | false                                                        |
| logPrefix                 | 指定 MyBatis 增加到日志名称的前缀。                          | Any String                                                   | Not set                                                      |
| logImpl                   | 指定 MyBatis 所用日志的具体实现，未指定时将自动查找。        | SLF4J \|LOG4J \|LOG4J2 \|JDK_LOGGING \|COMMONS_LOGGING \|STDOUT_LOGGING \|NO_LOGGING | Not set                                                      |
| proxyFactory              | 指定 Mybatis 创建具有延迟加载能力的对象所用到的代理工具。    | CGLIB \|JAVASSIST                                            | JAVASSIST (MyBatis 3.3 or above)                             |

### 2.2.3	typeAliases

类型别名是为Java类型设置一个短的名字。它只和xml配置有关，存在的意义仅在于用来减少类完全限定名的冗余

```xml

<typeAliases>
  <typeAlias alias="Author" type="domain.blog.Author"/>
  <typeAlias alias="Blog" type="domain.blog.Blog"/>
</typeAliases>
<!-- 当这样配置时，Blog可以用在任何使用domain.blog.Blog的地方。-->

<typeAliases>
  <package name="domain.blog"/>
</typeAliases>
<!--指定一个包名，MyBatis会在包名下搜索需要的JavaBean,每一个在包domain.blog中的JavaBean，在没有注解的情况下，会使用Bean的首字母小写的非限类名来作为它的别名。比如domain.blog.Author的别名为author；若有注解，则别名为注解值  -->
```

### 2.2.4	typeHandlers

无论是 MyBatis 在预处理语句（PreparedStatement）中设置一个参数时，还是从结果集中取出一个值时， 都会用类型处理器将获取的值以合适的方式转换成 Java 类型

可以重写类型处理器或创建自己的类型处理器来处理不支持的或非标准的类型。具体的做法为：实现org.apache.ibatis.type.TypeHandler接口，或继承一个很便利的类org.apache.ibatis.type.BaseTypeHandler,然后可以选择性地将它映射到一个JDBC类型

```java
//
ExampleTypeHandler.java
@MappedJdbcTypes(JdbcType.VARCHAR) public class ExampleTypeHandler extends BaseTypeHandler<String>
 
  {
  @Override public void setNonNullParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException
    { ps.setString(i,
  parameter)
;
  }
  @Override public String getNullableResult(ResultSet rs, String columnName) throws SQLException
    { return
  rs.getString(columnName)
;
  }
  @Override public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException
    { return
  rs.getString(columnIndex)
;
  }
  @Override public String getNullableResult(CallableStatement cs, int columnIndex) throws SQLException
    { return
  cs.getString(columnIndex);
}}
/**
在配置文件里面加上
<typeHandlers>
  <typeHandler handler="org.mybatis.example.ExampleTypeHandler"/>
</typeHandlers>
*/
```

使用这个的类型处理器将会覆盖已经存在的处理Java的String类型属性和VARCHAR参数及结果的类型处理器。要注意MyBatis不会窥探数据库元信息来决定使用哪种类型，所以必须在参数和结果映射中指明是VARCHAR类型字段，以使其能绑定到正确的类型处理器上。这是因为，MyBatis直到语句被执行才清楚数据类型。
		通过类型处理器的泛型，MyBatis可以得知该类型处理器的Java类型，不过这种行为可以通过两种方法改变：
		在类型处理器的元素（typeHandler element）上增加一个javaType属性（比如，javaType="String"）;
		在类型处理器的类上（TypeHandler class）增加一个@MappedTypes注解来指定与其关联的Java类型列表。如果在javaType属性中也同时制定，则注解方式将被忽略。
可以通过两种方式来指定被关联的JDBC类型：
		在类型处理器的配置元素上增加一个javaType属性（比如：javaType="VARCHAR"）;
		在类型处理器的类上（TypeHandler class）增加一个@MappedJdbcTypes注解来指定与其关联的JDBC类型列表。如果在javaType属性中也同时指定，则注解方式将被忽略。

### 2.2.5	objectFactory

MyBatis每次创建结果对象的新实例时，它都会使用一个对象工厂（ObjectFactory）实例来完成。默认的对象工厂需要做的仅仅是实例化目标类，要么通过默认构造方法，要么在参数映射存在的时候通过参数构造方法来实例化。如果想覆盖对象工厂的行为，则可以通过创建自己的对象工厂来实现

```java
//
ExampleObjectFactory.java public class ExampleObjectFactory extends DefaultObjectFactory
  { public Object create(Class type)
    { return
  super.create(type);
  } public Object create(Class type, List<Class> constructorArgTypes, List<Object> constructorArgs)
    { return super.create(type, constructorArgTypes,
  constructorArgs);
  } public void setProperties(Properties properties)
    {
  super.setProperties(properties);
  } public <T> boolean isCollection(Class<T> type){
    return Collection.class.isAssignableFrom(type);
  }}
//////////////////////////////////////////////////
<objectFactory type="org.mybatis.example.ExampleObjectFactory">
  <property name="someProperty" value="100"/>
</objectFactory>
    ObjectFactory接口很简单，它包含两个创建用的方法，一个是处理默认构造方法的，另外一个是处理带参数的构造方法。最后setProperties方法可以被用来配置ObjectFactory,初始化你的ObjectFactory实例后，objectFactory元素体内定义的属性会被传递给setProperties方法。
```

### 2.2.6	plugins

MyBatis允许你在已映射的语句执行过程中的某一点进行拦截调用。默认情况下，Mybatis允许使用插件来拦截的方法调用包括

- Executor(update,query,flushStatements,commit,rollback,getTransaction,close,isClosed)
- ParameterHandler(getParameterObejct,setParameters)
- ResultSetHandler(handlerResultSets,handlerOutputParameters)
- StatementHandler(prepare,parameterize,batch,update,query)

通过MyBatis提供强大的机制，使用插件是非常简单的，只需要实现Interceptor接口，并指定想要拦截的方法签名即可

```java

// ExamplePlugin.java
@Intercepts({@Signature(
  type= Executor.class,
  method = "update",
  args = 
{MappedStatement.class,Object.class})}) public class ExamplePlugin implements Interceptor
  { public Object intercept(Invocation invocation) throws Throwable
    { return
  invocation.proceed();
  } public Object plugin(Object target)
    { return Plugin.wrap(target,
  this);
  } public void setProperties(Properties properties)
  {}
}
<!-- mybatis-config.xml -->
<plugins>
  <plugin interceptor="org.mybatis.example.ExamplePlugin">
    <property name="someProperty" value="100"/>
  </plugin>
</plugins>
```

上面的插件将会拦截Executor实例中所有的“update”方法调用，这里的Executor是负责执行底层映射语句的内部对象。
覆盖配置类,除了用插件来修改MyBatis核心行为之外，还可以通过完全覆盖配置类来达到目的。只需继承后覆盖其中的每个方法，再把它传递到sqlSessionFactoryBuilder.build(myConfig)方法即可。再次重申，这可能会严重影响Mybatis的行为，务请慎之又慎！

### 2.2.7	environments

MyBatis可以配置成适应多种环境，这种机制有助于将sql映射应用于多种数据库中，现实情况下有多种理由需要这么做。例如，开发、测试和生产环境需要有不同的配置；或者共享相同的Schema的多个生产数据库，想使用相同的sql映射。许多类似的用例。
		尽管可以配置多个环境，但是每个SqlSessionFactory实例只能选择其一,所以，如果想连接两个数据库，就需要创建两个SqlSessionFactory实例，每个数据库对应一个。而如果是三个数据库，就需要三个实例，依此类推。每个数据库对应一个SqlSessionFactory实例

为了指定创建哪种环境，只要将它作为可选参数传递给SqlSessionFactoryBuilder即可

SqlSessionFactory factory = sqlSessionFactoryBuilder.build(reader, environment);
SqlSessionFactory factory = sqlSessionFactoryBuilder.build(reader, environment,properties);

如果忽略了环境参数，那么默认环境将会被加载,环境元素定义了如何配置环境

```xml
<environments default="development">
  <environment id="development">
    <transactionManager type="JDBC">
      <property name="..." value="..."/>
    </transactionManager>
    <dataSource type="POOLED">
      <property name="driver" value="${driver}"/>
      <property name="url" value="${url}"/>
      <property name="username" value="${username}"/>
      <property name="password" value="${password}"/>
    </dataSource>
  </environment>
</environments>
```

- 默认环境的ID（比如：default="development"）
- 每个environment元素定义的环境ID（比如：id="development"）
- 事务管理器的配置（比如：type="JDBC"）
- 数据源的配置（比如：type="POOLED"）

默认的环境和环境ID是一目了然的。随你怎么命名，只要保证默认环境要匹配其中一个环境ID
事务管理器（transactionManager）
在MyBatis中有两种类型的事务管理器（也就是 type="[JDBC|MANAGED]"）

JDBC --- 这个配置就是直接使用了JDBC的提交和回滚设置，它依赖于从数据源得到的连接来管理事务范围。
MANAGED --- 这个配置几乎没做什么。它从来不提交或回滚一个连接，而是让容器来管理事务的整个生命周期（比如JEE应用服务器上下文）。默认情况下它会关闭连接，然而一些容器并不希望这样，因此需要将closeConnection属性设置为false来阻止它默认的行为。

### 2.2.8	dataSource

dataSource元素使用了标准的JDBC数据源接口来配置JDBC连接对象的资源。
		许多MyBatis的应用程序将会按示例中的例子来配置数据源。然而它并不是必须的。要知道为了方便使用延迟加载，数据源才是必须的。
		有三种内建的数据源类型（也就是 type="[]UNPOOLED|POOLED|JNDI]"）；
		**UNPOOLED** --- 这个数据源的实现只是被请求时打开和关闭连接。虽然有一点慢，它对在及时可用连接方面没有性能要求的简单应用是一个很好的选择。不同的数据库在这方面表现也是不一样的，所以对某些数据库来说使用连接池并不重要，这个配置也是理想。UNPOOLED类型的数据源仅仅需要配置以下5种属性：

driver  -- 这是JDBC驱动的Java类的完全限定名（并不是JDBC驱动中可能包含的数据源类）
		url -- 这是数据库的JDBC URL 地址。
		username -- 登录数据库的用户名。
		password -- 登录数据库的密码。
		defaultTransactionIsolationLevel -- 默认的连接事务隔离级别。
作为可选项，可以传递属性给数据库驱动。要这样做，属性的前缀为"driver."，例如：
		driver.encoding=UTF-8
		这将通过DriverManager,getConnection(url,driverProperties)方法传递值为UTF-8的encoding属性给数据库驱动。

**POOLED** --- 这种数据源的实现利用“池”的概念将JDBC连接对象组织起来，避免了创建新的连接实例时所必需的初始化和认证时间。这是一种使得并发web应用快速响应请求的流行处理方式。

除了上述提到UNPOOLED下的属性外，会有更多属性用来配置POOLED的数据源：

poolMaximumActiveConnections  -- 在任意时间可以存在的活动（也就是正在使用）连接数量，默认值10
		poolMaximumIdleConnections  -- 任意时间可能存在的空闲连接数。
		poolMaximumCheckoutTime  -- 在被强制返回之前，池中连接被检出（checked out）时间，默认值：20000毫秒（即20秒）
		poolTimeToWait  -- 这是一个底层设置，如果获取连接花费的相当长的时间，它会给连接池打印状态日志并重新尝试获取一个连接（避免在误配置的情况下一直安静的失败），默认值20000毫秒（即20秒）。
		poolPingQuery  -- 发送到数据库的侦测查询，用来检验连接是否处在正常的工作秩序中，并且准备接受请求。默认是"NOT PING QUERY SET"，这会导致多数数据库连接失败时带有一个恰当的错误信息。
		poolPingEnabled  -- 是否启用侦测。若开启，也必须使用一个可执行的SQL语句设置poolPingQuery属性（最好是一个非常快的SQL）,默认值：false。
		poolPingConnectionsNotUsedFor  -- 配置poolPingQuery使用的频度。这可以被设置成匹配具体的数据库连接超时时间，来避免不必要的侦测，默认值：0（即所有连接每一时刻都被侦测  --  当然仅当 poolPingEnabled为true时适用）。
		**JNDI**  -- 这个数据源的实现是为了能在如EJB或应用服务器这类容器中使用，容器可以集中或在外部配置数据源，然后放置一个JNDI上下文的引用。这种数据源配置只要两个属性：
		initial_context  -- 这个属性用来在InitialContext中寻找上下文（initialContext.lookup(initial_context)）。这是个可选属性，如果忽略，那么data_source属性将会直接从InitialContext中寻找。
		data_source  -- 这是引用数据源实例位置的上下文的路径。提供了 initial_context配置时会在其返回的上下文中进行查找，没有提供时则直接在InitialContext中查找。
和其他数据源配置类似，可以通过添加前缀"env."直接把属性传递给初始上下文。比如：
env.encoding=UTF-8

### 2.2.9	mappers

既然MyBatis的行为已经由上述元素配置完了，现在就要定义SQL映射语句了。但是首先需要告诉MyBatis到哪里去找到这些语句。Java在自动查找这方面没有提供一个很好的方法，所以最佳的方式是告诉MyBatis到哪里去找映射文件。可以使用相对于类路径的资源引用、或完全限定资源定位符（包括file:///的URL），或类名和包名;