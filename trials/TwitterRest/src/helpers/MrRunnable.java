package helpers;

import java.io.IOException;

import main.ConsumerPool;
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
    
        try {
           //creating an object of getDataFromTwitter
           //check the url for trends or search , create an object respective to that 
            
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            
        }
        
    }
    
}
