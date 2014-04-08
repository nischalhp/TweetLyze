package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.Stack;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import oauth.signpost.OAuthConsumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class JuliusCaesar {

	private static String propertiesMain = "properties/property.properties";
	public static BlockingQueue<OAuthConsumer> consumerPool;
	/*
	 * Adding a consumer Holder The reason being , usage of OAuth objects across
	 * all APPs spread equally
	 */
	public static int sizeOfConsumerPool;

	public static void main(String args[]) {

		Properties PropertyHandler = new Properties();
		Logger log = null;
		try {
			PropertyHandler.load(new FileInputStream(propertiesMain));

			consumerPool = ConsumerPool.buildConsumerPool();
			sizeOfConsumerPool = consumerPool.size();
			String logPath = PropertyHandler.getProperty("logPath");

			PropertyConfigurator.configure(logPath);
			log = Logger.getLogger(JuliusCaesar.class.getName());
			log.info("consumer pool created");

			Stack<MrUrl> jobStack = null;

			/*
			 * Per trend how many calls per 15 mins and waiting time after each
			 * call
			 */

			int numberOfTrends = Integer.parseInt(PropertyHandler
					.getProperty("totalTrends"));
			int numberOfApps = Integer.parseInt(PropertyHandler
					.getProperty("numberOfAccounts"));
			int searchQueries = Integer.parseInt(PropertyHandler
					.getProperty("numberOfSearchQueries"));

			int totalSearchCalls = numberOfApps * searchQueries;
			int perTrend = (int) totalSearchCalls / numberOfTrends;

			double timeSpace = 15 / perTrend;
			double seconds = timeSpace * 60;
			int milliseconds = (int) seconds * 1000;

			// Adding executors

			int NumberOfThreads = Integer.parseInt(PropertyHandler
					.getProperty("numberOfThreads"));

			ExecutorService executor = Executors
					.newFixedThreadPool(NumberOfThreads);
			log.info("Executor class has been initialized");

			MrTimer.startTask();
			log.info("Started the timer to insert trends");
			/*
			 * This is the main logic here What happens is first it goes into
			 * the while loop Then creates the job stack Enters 2nd while If job
			 * stack is empty it waits then gets out of the loop else it takes a
			 * job assigns it to the executor this way i dont need to shutdown
			 * the executor at all
			 */
			while (true) {
				jobStack = MrMestri.buildJobs();
				log.info("Creating a job stack of the search urls");
				int jobToken = 0;
				while (true) {
					if (jobStack.isEmpty()) {
						log.info("No trends for the day , have to wait till trends get populated");
						Thread.sleep(10000);
						break;
					} else {
						/*
						 * Now that the job stack is there we need to pick 80
						 * jobs at a time and based on number of apps wait to
						 * run the process again
						 */
						jobToken++;

						MrRunnable worker = new MrRunnable(jobStack.pop());
						executor.execute(worker);

						if (jobToken
								% Integer.parseInt(PropertyHandler
										.getProperty("totalTrends")) == 0) {
							log.info("All jobs for the clock cycle complete , waiting for next clock cycle to start. Number of jobs completed "
									+ jobToken);
							Thread.sleep(milliseconds);

						}
					}
				}
			}
		} catch (FileNotFoundException e) {
			log.error(e);
		} catch (InterruptedException e) {
			log.error(e);
		} catch (IOException e) {
			log.error(e);
		}
	}

	public static synchronized OAuthConsumer getConsumerObject() {
		OAuthConsumer consumerObj;
		consumerObj = consumerPool.peek();

		return consumerObj;
	}

	public static synchronized void putConsumerObject(OAuthConsumer obj)
			throws InterruptedException {
		consumerPool.put(obj);
	}
}
