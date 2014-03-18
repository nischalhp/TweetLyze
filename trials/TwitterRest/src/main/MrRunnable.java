package main;

import java.io.IOException;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MrRunnable implements Runnable {
    
    private String        toFireUrl;
    private OAuthConsumer consumer;
    
    MrRunnable(String url, OAuthConsumer cons) {
    
        this.consumer = cons;
        this.toFireUrl = url;
    }
    
    @Override
    public void run() {
    
        
    }
    
}
