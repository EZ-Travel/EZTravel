package com.utils;

import com.api.EventApi;
import com.exceptions.EventApiException;

/**
 * starts a thread that adds new events every hour
 *
 */
public class Scheduler extends Thread {

	private EventManager manager = EventManager.getManager();
	private EventApi api = new EventApi();
	private boolean on = true;

	@Override
	public void run() {

		System.out.println("starting...");
		try {
			while (on) {

				addEvents();
				Thread.sleep(1000 * 60 * 60);
			}

		} catch (InterruptedException e) {
			System.out.println("interruptted... ");
			e.printStackTrace();
		}

		System.out.println("shutting down...");

	}

	private void addEvents() {
		System.out.println("adding...");
		try {
			manager.persistEvents(api.getAllEvents());
		} catch (EventApiException e) {

			e.printStackTrace();
		}
	}

	public void shutDown() {
		on = false;
		this.interrupt();
	}

}
