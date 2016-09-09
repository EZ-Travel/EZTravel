package com.event;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.PriorityQueue;

import org.junit.Test;

import com.cache.Cache;
import com.cache.CacheTimestamp;
import com.pojos.Event;

public class CasheTest {

	@Test
	public void test() {
		Cache.getInstance()
				.addValue("2", new Event(1, "later", "desc", "text", "v_id",
						new Date(System.currentTimeMillis() + 10000), 20, 20, 500, "IMAGE", "weather"),
						new Date(System.currentTimeMillis() + 10000));
		Cache.getInstance().addValue("1", new Event(1, "now", "desc", "text", "v_id",
				new Date(System.currentTimeMillis()), 10, 10, 100, "IMAGE", "weather"),
				new Date(System.currentTimeMillis()));
		Cache.getInstance()
				.addValue(
						"3", new Event(1, "earlier", "desc", "text", "v_id",
								new Date(System.currentTimeMillis() - 10000), 10, 10, 100, "IMAGE", "weather"),
						new Date(System.currentTimeMillis() - 10000));

//		ONLY FOR TEST add this func to Cache
//		public PriorityQueue<CacheTimestamp> test (){
//			return timeMap;
//		}
		
//		PriorityQueue<CacheTimestamp> ads = Cache.getInstance().test();

//		assertTrue(ads.peek().getKey().equals("3"));
	}

}
