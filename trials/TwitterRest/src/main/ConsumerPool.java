package main;

import java.io.FileInputStream;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ConsumerPool {

	private static Logger log;
	private static String propertiesMain = "properties/property.properties";

	// building consumer Pool
	@SuppressWarnings("finally")
	public static BlockingQueue<OAuthConsumer> buildConsumerPool() {

		BlockingQueue<OAuthConsumer> consumerObjectsQueue = null;
		try {

			// setting the config path
			Properties PropertyHandler = new Properties();
			PropertyHandler.load(new FileInputStream(propertiesMain));
			String twitterPath = PropertyHandler.getProperty("twitterPath");
			String logPath = PropertyHandler.getProperty("logPath");

			// setting config for twitter
			Properties PropertyHandlerTwitter = new Properties();
			PropertyHandlerTwitter.load(new FileInputStream(twitterPath));
			// Blocking Linked Queue

			consumerObjectsQueue = new LinkedBlockingQueue<OAuthConsumer>();
			// initializing logger
			PropertyConfigurator.configure(logPath);
			log = Logger.getLogger(ConsumerPool.class.getName());

			log.info("Fetching api details for the twitter apps");

			// fetching required tokens for all apps
			String consumerKeySet = PropertyHandlerTwitter
					.getProperty("consumerKey");
			String consumerSecretSet = PropertyHandlerTwitter
					.getProperty("consumerSecret");
			String accessTokenSet = PropertyHandlerTwitter
					.getProperty("accessToken");
			String tokenSecretSet = PropertyHandlerTwitter
					.getProperty("tokenSecret");

			String[] splitconsumerKeys = consumerKeySet.split(",");
			String[] splitconsumerSecret = consumerSecretSet.split(",");
			String[] splitaccessToken = accessTokenSet.split(",");
			String[] splittokenSecret = tokenSecretSet.split(",");

			// creating consumer objects for each app
			for (int numberOfAccounts = 0; numberOfAccounts < splitconsumerKeys.length; numberOfAccounts++) {
				log.info("constructing consumer object for twitter api "
						+ numberOfAccounts);
				String consumerKey = splitconsumerKeys[numberOfAccounts];
				String consumerSecret = splitconsumerSecret[numberOfAccounts];
				String accessToken = splitaccessToken[numberOfAccounts];
				String tokenSecret = splittokenSecret[numberOfAccounts];
				log.info("the keys used to build the oauthObject are "
						+ consumerKey + "," + consumerSecret + ","
						+ accessToken + "," + tokenSecret);
				OAuthConsumer consumer = new CommonsHttpOAuthConsumer(
						consumerKey, consumerSecret);
				consumer.setTokenWithSecret(accessToken, tokenSecret);

				/*
				 * Creating a pool of consumer objects of the same app so that i
				 * can make as many as 80 calls using different apps in a second
				 */

				for (int numberOfConsumerObjects = 0; numberOfConsumerObjects < (Integer) (Integer
						.parseInt(PropertyHandler
								.getProperty("totalConsumerObjects")) / Integer
						.parseInt(PropertyHandler
								.getProperty("numberOfAccounts"))); numberOfConsumerObjects++) {
					consumerObjectsQueue.put(consumer);
				}
				log.info("added the consumer object to que pool");

			}
		} catch (Exception e) {
			log.error("Something went wrong while creating a pool of consumer objects");
		} finally {
			return consumerObjectsQueue;
		}
	}
}
