package com.atguigu.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class JedisPoolUtil {

	public static JedisPoolConfig jps = new JedisPoolConfig();
	public static JedisPool jedisPool = null;

	static {
		jps.setBlockWhenExhausted(true); // 连接耗尽则阻塞
		jps.setLifo(true); // 后进先出
		jps.setMaxIdle(10); // 最大空闲连接数为10
		jps.setMinIdle(0); // 最小空闲连接数为0
		jps.setMaxTotal(100); // 最大连接数为100
		jps.setMaxWaitMillis(-1); // 设置最大等待毫秒数：无限制
		jps.setMinEvictableIdleTimeMillis(180); // 逐出连接的最小空闲时间：30分钟
		jps.setTestOnBorrow(true); // 获取连接时是否检查连接的有效性：是
		jps.setTestWhileIdle(true); // 空闲时是否检查连接的有效性：是
		jedisPool = new JedisPool(jps, "192.168.221.128", 6379);

	}

	public static Jedis getJedis() {
//		Jedis jedis=new Jedis("192.168.221.128", 6379);
		return jedisPool.getResource();
//		return jedis;
	}

}
