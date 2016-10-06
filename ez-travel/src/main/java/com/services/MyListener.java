package com.services;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.utils.Scheduler;

/**
 * Application Lifecycle Listener implementation class MyListener
 *
 */
@WebListener
public class MyListener implements ServletContextListener {

	Scheduler scheduler = new Scheduler();

	/**
	 * Default constructor.
	 */
	public MyListener() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see ServletContextListener#contextDestroyed(ServletContextEvent)
	 */
	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("listenner terminanted..");
		scheduler.shutDown();
	}

	/**
	 * @see ServletContextListener#contextInitialized(ServletContextEvent)
	 */
	public void contextInitialized(ServletContextEvent arg0) {

		System.out.println("listenner started..");
		scheduler.start();
	}

}
