package main;

import helpers.PropertyHandler;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        
        //Adding executors
        
        int NumberOfThreads = 2*consumerPool.size();
        ExecutorService executor = Executors.newFixedThreadPool(NumberOfThreads);
        ArrayList<String> trendsGlobalList = new ArrayList<String>();
        while(true){
            

        }

    }
    
}
