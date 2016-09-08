package com.services;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.cache.Cache;
import com.cache.CacheTimestamp;
import com.pojos.Event;

@Path("cache")
public class CacheService {

	// ADD
	@Path("addevent")
	@POST
	public void addEvent(@QueryParam("key") String key, @QueryParam("val") Event val) {

		System.out.println("Inside Cache addEvent");

		ConcurrentHashMap<String, Object> cacheMap = Cache.getInstance().getCacheMap();
		PriorityQueue<CacheTimestamp> timeMap = Cache.getInstance().getTimeMap();

		cacheMap.put(key, val);
		timeMap.add(new CacheTimestamp(key, ((Event) val).getExp_date()));

		System.out.println("Done Cache addEvent");
	}

	// GET
	@Path("getevent")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEvent(@QueryParam("key") String key) {

		System.out.println("Inside Cache getEvent, key recived - " + key);

		ConcurrentHashMap<String, Object> cacheMap = Cache.getInstance().getCacheMap();

		Event evnet = (Event) cacheMap.get(key);
		System.out.println("Done Cache getEvent, returning - " + evnet.toString());

		// Returns null if missing.
		return evnet;

	}

	// CLEAR EXPIRED
	@Path("clearexpired")
	@DELETE
	public void clearExpiredEvents() {

		System.out.println("Inside Cache clearExpiredEvents");

		ConcurrentHashMap<String, Object> cacheMap = Cache.getInstance().getCacheMap();
		PriorityQueue<CacheTimestamp> timeMap = Cache.getInstance().getTimeMap();

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
		System.out.println("Done Cache clearExpiredEvents");
	}

}
