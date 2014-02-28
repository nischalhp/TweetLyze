package main;

import java.util.concurrent.BlockingQueue;

import oauth.signpost.OAuthConsumer;


public class iAmController {
    
    public static void main(String args[]){
        
        ConsumerPool consumerObjects = new ConsumerPool();
        BlockingQueue<OAuthConsumer> consumerPool = consumerObjects.buildConsumerPool();
        
        
        
        
        
        
    }
    
}
