package com.services;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import com.cache.Cache;
import com.pojos.Event;

@Path("cache")
public class CacheService {


	@Path("addevent")
	@POST
	public void addEvent(@QueryParam("key") String key, @QueryParam("val") Event val) {
		Cache.getInstance().addValue(key, val, val.getExp_date());
	}

	
	@Path("getevent")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Event getEvent(@QueryParam("key") String key) {
		Event evnet = (Event) Cache.getInstance().getValue(key);
		return evnet;
	}


	@Path("clearexpired")
	@DELETE
	public void clearExpiredEvents() {
		Cache.getInstance().clearExpiredEvents();
	}

}
