package com.utils;

import java.util.Collection;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.pojos.Event;

public class EventManager {

	private static SessionFactory factory;
	private static EventManager instance = new EventManager();

	private EventManager() {
		Configuration config = new Configuration().configure().addAnnotatedClass(Event.class);
		factory = config.buildSessionFactory();
	}

	public synchronized Event persistEvent(Event event) {
		Session session = factory.openSession();
		session.beginTransaction();
		session.save(event);
		session.getTransaction().commit();
		session.close();
		return event;
	}

	public Event findEvent(int eventID) {
		Session session = factory.openSession();
		session.beginTransaction();
		Event event = session.load(Event.class, eventID);
		session.getTransaction().commit();
		session.close();
		return event;
	}

	public List<Event> findAll() {
		Session session = factory.openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Event> events = session.createQuery("from Event").getResultList();
		session.getTransaction().commit();
		session.close();

		return events;
	}

	public synchronized void removeEvent(int id) {
		Session session = factory.openSession();
		session.beginTransaction();
		Event event = findEvent(id);
		session.delete(event);
		session.getTransaction().commit();
		session.close();
	}

	public synchronized Event updateEvent(Event event) {
		Session session = factory.openSession();
		session.beginTransaction();
		session.update(event);
		session.getTransaction().commit();
		session.close();
		return findEvent(event.getId());

	}

	public List<Event> searchEvents(String weather, double lon, double lat) {

		Session session = factory.openSession();
		session.beginTransaction();
		@SuppressWarnings("unchecked")
		List<Event> events = session
				.createQuery("select ev from Event ev where ev.lon = ?1 and ev.lat = ?2 and ev.weather = ?3")
				.setParameter(1, lon).setParameter(2, lat).setParameter(3, weather).getResultList();
		session.getTransaction().commit();
		session.close();
		return events;
	}

	public void persistEvents(Collection<Event> events) {
		List<Event> list = (List<Event>) events;

		Session session = factory.openSession();
		session.beginTransaction();
		System.out.println(list.size());
		for (int i = 0; i < list.size(); i++) {

			session.save(list.get(i));
			if (i % 100 == 0) {
				session.flush();
				session.clear();
			}

		}
		session.getTransaction().commit();
		session.close();
	}

	public static EventManager getManager() {
		return instance;
	}

	public static void closeManager() {
		factory.close();
	}
}
