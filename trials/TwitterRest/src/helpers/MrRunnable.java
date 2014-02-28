package helpers;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.client.methods.HttpGet;

public class MrRunnable implements Runnable {
    
    private String        toFireUrl;
    
    MrRunnable(String url ) {
    
        this.toFireUrl = url;
    }
    
    @Override
    public void run() {
    
        try {
            HttpGet requestObj = new HttpGet(toFireUrl);
            consumer.sign(requestObj);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
}
