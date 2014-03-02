package main;

import helpers.PropertyHandler;

import java.util.concurrent.BlockingQueue;

import oauth.signpost.OAuthConsumer;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class iAmController {
    
    private static String propertiesMain = "/properties/property.properties";
    
    public static void main(String args[]) {
    
        PropertyHandler.setConfigPath(propertiesMain);
        ConsumerPool consumerObjects = new ConsumerPool();
        BlockingQueue<OAuthConsumer> consumerPool = consumerObjects.buildConsumerPool();
        String logPath = PropertyHandler.getProperty("logPath");
        
        PropertyConfigurator.configure(logPath);
        Logger log = Logger.getLogger(iAmController.class.getName());
        log.info("consumer pool created");
        
    }
    
}
