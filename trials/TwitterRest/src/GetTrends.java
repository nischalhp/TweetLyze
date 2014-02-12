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
    
    public GetTrends(){
        
    }
    
    public static void main(String args[]){
        
        String accessToken = "126065173-Sl3zH61tgYeIO7EgFsltkGz7NOSjnH9yhJuknIQB";
        String tokenSecret = "a3GK8YxsqF7kNtYbDoR0FAsO5GkYwpxB37I9Aq0fUctew";
        String consumerKey = "3pJIcVf0QUzcLpWlWXqB5A";
        String consumerSecret = "6XTtv1cMTNlDbdTVyCh14HN5DKCSOBasLwpqGgiWKM";
        
        try{
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer
                (consumerKey, consumerSecret);
       
        consumer.setTokenWithSecret(accessToken,tokenSecret);
        HttpGet request = new HttpGet("https://api.twitter.com"
                + "/1.1/trends/place.json?id=2295420");
        
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
        for(int i =0;i<jsonArrayTrends.length();i++){
            JSONObject jsonObjTrend = jsonArrayTrends.getJSONObject(i);
            System.out.println(jsonObjTrend);
            String searchUrl = jsonObjTrend.getString("url");
            System.out.println(searchUrl);
            searchUrl = searchUrl.replaceAll("/search","/search/tweets.json");
            searchUrl = searchUrl.replaceAll("//twitter.com","//api.twitter.com/1.1");
            searchUrl = searchUrl.replaceAll("http","https");
            searchUrl = searchUrl+"&lang=en";
            System.out.println(searchUrl);
            SearchTrends trendsQueryObj = new SearchTrends(consumer,searchUrl);
            break;
        }
        
        }catch(OAuthMessageSignerException e){
            e.printStackTrace();
        }catch(OAuthExpectationFailedException e){
            e.printStackTrace();
        }catch(OAuthCommunicationException e){
            e.printStackTrace();
        }catch(ClientProtocolException e ){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }catch(JSONException e ){
            e.printStackTrace();
        }
        
        
        
    }
    
}
