package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oauth.signpost.OAuthConsumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class iAmControllerOfTrends {

	private static String propertiesMain = "/properties/property.properties";

	public static void main(String args[]) {

		Properties PropertyHandler = new Properties();
		Logger log = null;
		try {
			PropertyHandler.load(new FileInputStream(propertiesMain));

			ConsumerPool consumerObjects = new ConsumerPool();
			BlockingQueue<OAuthConsumer> consumerPool = consumerObjects
					.buildConsumerPool();
			String logPath = PropertyHandler.getProperty("logPath");

			PropertyConfigurator.configure(logPath);
			log = Logger.getLogger(iAmControllerOfTrends.class.getName());
			log.info("consumer pool created");

			// Adding executors

			int NumberOfThreads = 2 * consumerPool.size();
			ExecutorService executor = Executors
					.newFixedThreadPool(NumberOfThreads);
			while (true) {

//				int timeToGetTrends = getSystemTime();
				

			}
		} catch (FileNotFoundException e) {
			log.error("Cant set properties file"+e.getMessage()); 

		} catch (IOException e) {
			log.error("Something went wrong while setting properties file"
					+e.getMessage()); 
		}

	}

	public static int getSystemTime() {

		long millis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return (cal.getTime().getHours());

	}

}
