package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpException;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONException;

import twitter.GetTrends;

public class JuliusCaesar {

	private static String propertiesMain = "properties/property.properties";

	public static void main(String args[]) {

		Properties PropertyHandler = new Properties();
		Logger log = null;
		try {
			PropertyHandler.load(new FileInputStream(propertiesMain));

			BlockingQueue<OAuthConsumer> consumerPool = ConsumerPool
					.buildConsumerPool();
			String logPath = PropertyHandler.getProperty("logPath");

			PropertyConfigurator.configure(logPath);
			log = Logger.getLogger(JuliusCaesar.class.getName());
			log.info("consumer pool created");

			// Adding executors

			int NumberOfThreads = 80; 
			ExecutorService executor = Executors
					.newFixedThreadPool(NumberOfThreads);
			log.info("Executor class has been initialized");
			while (true) {
				int timeToGetTrends = getSystemTime();
				if (timeToGetTrends == Integer.parseInt(PropertyHandler
						.getProperty("trendsTime"))) {
					// peeking to see if a consumer object is free
					OAuthConsumer consumerObj = consumerPool.peek();
					// if its null you need to wait to pick up the object
					if (consumerObj != null) {
						// No need to spawn thread , just let the trends do its job
						GetTrends.retrieveTrends(consumerObj);
						log.info("Trends for the day has been added to the database");
					}
				}
				/*
				 * based on the number of retrieved trends set time gaps so that
				 * we do not let them exceed rate limit
				 */
				log.info("Creating a job stack of the search urls");
			}
		} catch (FileNotFoundException e) {
			log.error("Cant set properties file" + e.getMessage());

		} catch (OAuthMessageSignerException e) {
			log.error("When signing the request using the consumer object something went wrong "
					+ e.getMessage());

		} catch (ClientProtocolException e) {
			log.error("While requesting for data from twitter using the client something went wrong "
					+ e.getMessage());

		} catch (IOException e) {
			log.error("Something went wrong while setting properties file"
					+ e.getMessage());

		} catch (OAuthExpectationFailedException e) {
			log.info(e.getMessage());
		} catch (JSONException e) {
			log.info(e.getMessage());
		} catch (SQLException e) {
			log.info(e.getMessage());
		} 
		 catch (OAuthCommunicationException e) {
			log.info(e.getMessage());
		} catch (HttpException e) {
			log.info(e.getMessage());
		} catch (ParseException e) {
			log.info(e.getMessage());
		}
	}

	public static int getSystemTime() {

		long millis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		return (cal.getTime().getHours());

	}

}
