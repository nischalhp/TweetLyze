package main;

import helpers.PropertyHandler;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class ConsumerPool {
    
    private static Logger log;
    private static String propertiesMain = "/properties/property.properties";
    
    //building consumer Pool
    @SuppressWarnings("finally")
    public BlockingQueue<OAuthConsumer> buildConsumerPool() {
    
        BlockingQueue<OAuthConsumer> consumerObjectsQueue = null;
        try {
            //setting the config path
            PropertyHandler.setConfigPath(propertiesMain);
            String twitterPath = PropertyHandler.getProperty("twitterPath");
            String logPath = PropertyHandler.getProperty("logPath");
            
            //setting config for twitter
            PropertyHandler.setConfigPath(twitterPath);
            //Blocking Linked Queue
            
            consumerObjectsQueue = new LinkedBlockingQueue<OAuthConsumer>();
            //initializing logger
            PropertyConfigurator.configure(logPath);
            log = Logger.getLogger(ConsumerPool.class.getName());
            
            log.info("Fetching api details for the twitter apps");
            
            //fetching required tokens for all apps
            String consumerKeySet = PropertyHandler.getProperty("consumerKey");
            String consumerSecretSet = PropertyHandler.getProperty("consumerSecret");
            String accessTokenSet = PropertyHandler.getProperty("accessToken");
            String tokenSecretSet = PropertyHandler.getProperty("tokenSecret");
            
            String[] splitconsumerKeys = consumerKeySet.split(",");
            String[] splitconsumerSecret = consumerSecretSet.split(".");
            String[] splitaccessToken = accessTokenSet.split(",");
            String[] splittokenSecret = tokenSecretSet.split(".");
            
            //creating consumer objects for each app
            for (int numberOfAccounts = 0; numberOfAccounts < splitconsumerKeys.length; numberOfAccounts++) {
                log.info("constructing consumer object for twitter api " +numberOfAccounts);
                String consumerKey = splitconsumerKeys[numberOfAccounts];
                String consumerSecret = splitconsumerSecret[numberOfAccounts];
                String accessToken = splitaccessToken[numberOfAccounts];
                String tokenSecret = splittokenSecret[numberOfAccounts];
                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
                consumer.setTokenWithSecret(accessToken, tokenSecret);
                consumerObjectsQueue.put(consumer);
                log.info("added the consumer object to que pool");
                
            }
        } catch (Exception e) {
            log.error("Something went wrong while creating a pool of consumer objects");
        } finally {
            return consumerObjectsQueue;
        }
    }
}
