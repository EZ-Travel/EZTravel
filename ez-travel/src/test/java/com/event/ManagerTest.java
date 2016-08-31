package com.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.pojos.Event;
import com.utils.EventManager;

public class ManagerTest {

	public static void main(String[] args) {
		EventManager manager = EventManager.getManager();

		Event event = new Event(1, "name", "desc", "text", "v_id", new Date(), 20, 20, 500, "IMAGE", "weather");

		System.out.println(manager.persistEvent(event));

		List<Event> events = new ArrayList<>();

		events = manager.findAll();
		System.out.println(events);
		System.out.println(events.get(0).getId());

	}
}
