package com.cache;

import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

	private static Cache cache = null;
	private ConcurrentHashMap<String, Object> cacheMap;
	private PriorityQueue<CacheTimestamp> timeMap;

	private Cache() {
		cacheMap = new ConcurrentHashMap<String, Object>();

		CacheTimestampComperator cacheTimestampComperator = new CacheTimestampComperator();
		timeMap = new PriorityQueue<CacheTimestamp>(cacheTimestampComperator);

	}

	public ConcurrentHashMap<String, Object> getCacheMap() {
		return cacheMap;
	}

	public PriorityQueue<CacheTimestamp> getTimeMap() {
		return timeMap;
	}

	public static Cache getInstance() {

		if (cache == null)
			cache = new Cache();

		return cache;

	}

}
