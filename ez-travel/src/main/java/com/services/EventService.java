package com.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.pojos.Event;
import com.utils.EventManager;
import com.utils.Util;

@Path("event")
public class EventService {

	private EventManager manager = EventManager.getManager();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Event> getAllEvents() {

		return manager.findAll();

	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Event createEvent(Event event) {

		return manager.persistEvent(event);

	}

	@DELETE
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteEvent(@QueryParam("id") int id) {
		manager.removeEvent(id);

		return "event with removed successfully";
	}

	@PUT
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Event updateEvent(Event event) {

		return manager.updateEvent(event);
	}

	@GET
	@Path("{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Event grtEvent(@PathParam("id") int id) {

		return manager.findEvent(id);

	}

	@GET
	@Path("weather")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Set<Event> searchEvents(@QueryParam("lon") Double lon, @QueryParam("lat") Double lat,
			@QueryParam("weather") String weather) {
		List<Event> raw = manager.searchEvents(weather, lon, lat);
		Set<Event> events = new HashSet<>();
		for (Event event : raw) {
			if (event.getRadius() >= Util.distance(lat, lon, event.getLat(), event.getLon())) {
				events.add(event);
			}
		}
		return events;
	}

}
