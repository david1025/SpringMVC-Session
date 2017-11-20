# SpringMVC-Session

该项目是基于SpringMVC，集成Spring-Session实现Session共享  
这种方式比Tomcat插件方式更好，所以推荐大家使用  
未来几天出一版Spring-Boot的

# 集成步骤  

## 第一步. 搭建SpringMVC项目  
这一步我就不多说了网上很多例子自己按例子搭建就好了，也可以按照我这个搭建  

## 第二步. Maven引入依赖
```
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
    <version>1.3.1.RELEASE</version>
</dependency>
``` 

这个依赖其实是依赖了另外四个，这个下载下来是空的 
 
```
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-redis</artifactId>
    <version>1.4.2.RELEASE</version>
</dependency>  
```
```
<dependency>
    <groupId>redis.clients</groupId>
    <artifactId>jedis</artifactId>
    <version>2.5.2</version>
</dependency>
```
```
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session</artifactId>
    <version>1.0.2.RELEASE</version>
</dependency>
```
```
<dependency>
    <groupId>org.apache.commons</groupId>
    <artifactId>commons-pool2</artifactId>
    <version>2.2</version>
</dependency>
```    
如果不是使用maven构建项目的话，使用jar包，需要下载这四个jar  

## 第三步. 新建spring-session配置文件（我的项目中对应的是spring-session-redis.xml）  
在其中加入以下配置  
```
<beans>
<!-- 引入redis配置 -->
    <context:property-placeholder location="classpath:redis.properties" ignore-unresolvable="true"/>
    <context:annotation-config />
    <!--这里添加的是Redis，因为使用的是Spring里自带的Redis的Session策略 -->
    <bean id="redisConnectionFactory"
            class="org.springframework.data.redis.connection.jedis.JedisConnectionFactory"
            p:host-name="${redis.host}"
            p:port="${redis.port}"
            p:password="${redis.pass}"
            p:use-pool="true"
            p:database="8" />

    <bean id="stringRedisSerializer"
            class="org.springframework.data.redis.serializer.StringRedisSerializer" />

    <bean id="redisTemplate" class="org.springframework.data.redis.core.RedisTemplate"
          p:connection-factory-ref="redisConnectionFactory"
          p:keySerializer-ref="stringRedisSerializer" p:valueSerializer-ref="stringRedisSerializer"
          p:hashKeySerializer-ref="stringRedisSerializer"
          p:hashValueSerializer-ref="stringRedisSerializer" />
    <!-- 这里的是为了下面的 Session策略过滤器提供构造函数传入的参数，因为Session过滤器要依赖该对象来构造，所以创建一个先 -->
    <bean name="redisOperationsSessionRepository"
          class="org.springframework.session.data.redis.RedisOperationsSessionRepository">
        <constructor-arg ref="redisConnectionFactory"/>
    </bean>

    <!-- 这个是Session策略过滤器，即将容器原有的Session持久化机制，代替为Spring的 Redis持久化Session机制。 -->
    <!-- 注意，这个名字与 web.xml里的targetBean的下value是要一致的。 -->
    <bean name="springSession"
          class="org.springframework.session.web.http.SessionRepositoryFilter">
        <constructor-arg ref="redisOperationsSessionRepository"/>
    </bean>
</beans>
```
## 第四步. 在web.xml中添加配置  

再web.xml文件中加入以下配置

```
<filter>
    <filter-name>spring-session</filter-name>
    <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
    <init-param>
        <param-name>targetBeanName</param-name>
        <param-value>springSession</param-value>
    </init-param>
</filter>

<filter-mapping>
    <filter-name>spring-session</filter-name>
    <url-pattern>/*</url-pattern>
</filter-mapping>
```

## 第五步. 测试  

```
//测试一、向session中放入实现序列化的实体类（我这儿是TestEntity）
TestEntity testEntity= new TestEntity();
testEntity.setUsername("david_zh");
testEntity.setTrueName("TrueName");
request.getSession().setAttribute("testEntity", testEntity);

//测试二、向session中放入String字符串
request.getSession().setAttribute("testString", "i am a string value!");
```

注意向session中放对象的时候，这个对象必须实现序列化接口，否则session保存不到redis中，具体原因请百度redis  

# 至此，spirngmvc + spring-session 的集成就完了，有问题可以联系我  

我有那儿写的不好的希望大家指出，共同进步  
邮箱/QQ ： 990860210@qq.com


### License

MIT License

Copyright (c) 2017 David_zh

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.