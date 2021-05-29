package com.restaurant.web.listener;

import lombok.extern.slf4j.Slf4j;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * Context web.listener.
 * 
 * @author B.Loiko
 * 
 */
@Slf4j
public class ContextListener implements ServletContextListener {
/*


	public void contextDestroyed(ServletContextEvent event) {
		log("Servlet context destruction starts");
		// do nothing
		log("Servlet context destruction finished");
	}

	public void contextInitialized(ServletContextEvent event) {
		log("Servlet context initialization starts");

		ServletContext servletContext = event.getServletContext();
		initLog4J(servletContext);

		log("Servlet context initialization finished");
	}


	*/
/**
	 * Initializes log4j framework.
	 * 
	 * @param servletContext
	 *//*

	private void initLog4J(ServletContext servletContext) {
		log("Log4J initialization started");
		try {
			PropertyConfigurator.configure(servletContext.getRealPath(
							"WEB-INF/log4j.properties"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		log("Log4J initialization finished");
	}

	private void log(String msg) {
		System.out.println("[ContextListener] " + msg);
	}
*/

}