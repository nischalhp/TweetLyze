import helpers.PropertyHandler;

import java.io.IOException;
import java.io.StringWriter;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTrends {
    
    public GetTrends() {
    
    }
    
    public void buildConsumerPool(){
        
    }
    
    public static void main(String args[]) {
        
        //setting the config path
        PropertyHandler.setConfigPath("/twitter.properties");
        
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
            
            try {
                String consumerKey = splitconsumerKeys[numberOfAccounts];
                String consumerSecret = splitconsumerSecret[numberOfAccounts];
                String accessToken = splitaccessToken[numberOfAccounts];
                String tokenSecret = splittokenSecret[numberOfAccounts];
                OAuthConsumer consumer = new CommonsHttpOAuthConsumer(consumerKey, consumerSecret);
                
                consumer.setTokenWithSecret(accessToken, tokenSecret);
                HttpGet request = new HttpGet("https://api.twitter.com" + "/1.1/trends/place.json?id=2295420");
                
                consumer.sign(request);
                
                HttpClient client = new DefaultHttpClient();
                HttpResponse response = client.execute(request);
                
                int statusCode = response.getStatusLine().getStatusCode();
                System.out.println(response.getStatusLine().getReasonPhrase());
                //System.out.println(IOUtils.toString(response.getEntity().getContent()));
                StringWriter writer = new StringWriter();
                IOUtils.copy(response.getEntity().getContent(), writer);
                String jsonStr = writer.toString();
                //stem.out.println(jsonStr);
                JSONArray jsonArray = new JSONArray(jsonStr);
                JSONObject jsonObj = jsonArray.getJSONObject(0);
                JSONArray jsonArrayTrends = jsonObj.getJSONArray("trends");
                for (int i = 0; i < jsonArrayTrends.length(); i++) {
                    JSONObject jsonObjTrend = jsonArrayTrends.getJSONObject(i);
                    System.out.println(jsonObjTrend);
                    String searchUrl = jsonObjTrend.getString("url");
                    System.out.println(searchUrl);
                    searchUrl = searchUrl.replaceAll("/search", "/search/tweets.json");
                    searchUrl = searchUrl.replaceAll("//twitter.com", "//api.twitter.com/1.1");
                    searchUrl = searchUrl.replaceAll("http", "https");
                    searchUrl = searchUrl + "&lang=en";
                    System.out.println(searchUrl);
                    SearchTrends trendsQueryObj = new SearchTrends(consumer, searchUrl);
                    break;
                }
                
            } catch (OAuthMessageSignerException e) {
                e.printStackTrace();
            } catch (OAuthExpectationFailedException e) {
                e.printStackTrace();
            } catch (OAuthCommunicationException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            
        }
        
    }
}
