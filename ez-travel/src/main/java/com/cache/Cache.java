package com.cache;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

	private static Cache cache = new Cache();
	private ConcurrentHashMap<String, Object> cacheMap;
	private PriorityQueue<CacheTimestamp> timeMap;

	private Cache() {
		cacheMap = new ConcurrentHashMap<String, Object>();
		CacheTimestampComperator cacheTimestampComperator = new CacheTimestampComperator();
		timeMap = new PriorityQueue<CacheTimestamp>(cacheTimestampComperator);
	}

	public void addValue(String key, Object val, Date expirationDate) {
		cacheMap.put(key, val);
		timeMap.add(new CacheTimestamp(key, expirationDate));
	}

	public Object getValue(String key) {
		return cacheMap.get(key);
	}

	public void clearExpiredEvents() {

		CacheTimestamp head = timeMap.peek();
		while (head != null) {
			if (head.getTimestamp().compareTo(new Date(System.currentTimeMillis())) < 0) {
				System.out.println("Clearing " + head.getKey());
				cacheMap.remove((head.getKey()));
				timeMap.poll();

			} else
				break;
			head = timeMap.peek();
		}
	}

	public static Cache getInstance() {
		return cache;

	}

}
