package com.services;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.utils.Scheduler;

/**
 * used to start proccesses on server start up
 *
 */
@WebListener
public class MyListener implements ServletContextListener {

	Scheduler scheduler = new Scheduler();

	public MyListener() {
		// TODO Auto-generated constructor stub
	}

	public void contextDestroyed(ServletContextEvent arg0) {
		System.out.println("listenner terminanted..");
		scheduler.shutDown();
	}

	public void contextInitialized(ServletContextEvent arg0) {

		System.out.println("listenner started..");
		scheduler.start();
	}

}
