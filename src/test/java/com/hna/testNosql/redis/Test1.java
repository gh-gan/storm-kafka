package com.hna.testNosql.redis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import redis.clients.jedis.Jedis;

public class Test1 {
	  /** 
     * @param args 
     */  
    public static void main(String[] args) {  
          
        //连接redis服务   
        Jedis jedis = new Jedis("172.16.0.18",6379); 
        //权限认证
        jedis.auth("admin");
        
        jedis.set("redis", "myredis");  
        System.out.println(jedis.get("redis")); 
        
        jedis.append("redis", "yourredis");     
        jedis.append("content", "rabbit");  
        
        
       /* Jedis jedis2 = new Jedis("172.16.0.21",7000); 
        jedis2.lpush("mtdlist", "test");
        jedis2.rpop("mtdlist");
        System.out.println("len:"+jedis2.llen("mtdlist"));
        System.out.println("len:"+jedis2.lrange("mtdlist", 0, -1));
          
        //密码验证-如果你没有设置redis密码可不验证即可使用相关命令   
        //jedis.auth("abcdefg");  
          
        
        //简单的key-value 存储   
        jedis.set("redis", "myredis");  
        System.out.println(jedis.get("redis"));  
        
        
        //在原有值得基础上添加,如若之前没有该key，则导入该key   
        //之前已经设定了redis对应"myredis",此句执行便会使redis对应"myredisyourredis"   
        jedis.append("redis", "yourredis");     
        jedis.append("content", "rabbit");  
        
        //删除一个key
        jedis.del("redis");
        
        //mset 是设置多个key-value值   参数（key1,value1,key2,value2,...,keyn,valuen）   
        //mget 是获取多个key所对应的value值  参数（key1,key2,key3,...,keyn）  返回的是个list   
        jedis.mset("name1","yangw","name2","demon","name3","elena");  
        System.out.println(jedis.mget("name1","name2","name3"));  
          
        //map   
        Map<String,String> user = new HashMap<String,String>();  
        user.put("name", "cd");  
        user.put("password", "123456");  
        //map存入redis   
        jedis.hmset("user", user);  
        
        //mapkey个数   
        System.out.println(String.format("len:%d", jedis.hlen("user")));  
        //map中的所有键值   
        System.out.println(String.format("keys: %s", jedis.hkeys("user") ));  
        //map中的所有value   
        System.out.println(String.format("values: %s", jedis.hvals("user") ));  
        
        //取出map中的name字段值   
        List<String> rsmap = jedis.hmget("user", "name","password");  
        System.out.println(rsmap);  
        
        //删除map中的某一个键值 password   
        jedis.hdel("user", "password");  
        System.out.println(jedis.hmget("user", "name", "password"));  
        
        
        //list   
        jedis.del("listDemo");  
        System.out.println(jedis.lrange("listDemo", 0, -1));  
        jedis.lpush("listDemo", "A");  
        jedis.lpush("listDemo", "B");  
        jedis.lpush("listDemo", "C");  
        System.out.println(jedis.lrange("listDemo", 0, -1));  
        System.out.println(jedis.lrange("listDemo", 0, 1));  
        
        
        //set   
        jedis.sadd("sname", "wobby");  
        jedis.sadd("sname", "kings");  
        jedis.sadd("sname", "demon");  
        System.out.println(String.format("set num: %d", jedis.scard("sname")));  
        System.out.println(String.format("all members: %s", jedis.smembers("sname")));  
        System.out.println(String.format("is member: %B", jedis.sismember("sname", "wobby")));  
        System.out.println(String.format("rand member: %s", jedis.srandmember("sname"))); 
        
        //删除一个对象   
        jedis.srem("sname", "demon");  
        System.out.println(String.format("all members: %s", jedis.smembers("sname")));    */ 
        
    }  
  
}  



