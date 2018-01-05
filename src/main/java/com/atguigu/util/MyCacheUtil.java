package com.atguigu.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class MyCacheUtil {

	public static <T> List<T> getListRedis(String key, Class<T> t) {
		List<T> list_t = new ArrayList<T>();

		try {

			Jedis jedis = JedisPoolUtil.getJedis();
			Set<String> list = jedis.zrange(key, 0, -1);
			Iterator<String> iterator = list.iterator();

			while (iterator.hasNext()) {

				String next = iterator.next();
				T obj = MyJsonUtil.json_to_object(next, t);
				list_t.add(obj);

			}

		} catch (Exception e) {
			return null;
		}

		return list_t;

	}

	public static <T> void setListRedis(String key, List<T> list) {
		try {

			Jedis jedis = JedisPoolUtil.getJedis();

			for (int i = 0; i < list.size(); i++) {

				jedis.zadd(key, i, MyJsonUtil.object_to_json(list.get(i)));

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
