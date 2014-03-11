package twitter;
import helpers.PropertyHandler;

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;

import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTrends {
    
    private static Logger log;
    private static String propertiesMain = "/properties/property.properties";
    
    public GetTrends() {
    
    }
    
    public ArrayList<String> GetTrends(OAuthConsumer consumer) throws OAuthMessageSignerException, OAuthExpectationFailedException,
            OAuthCommunicationException, ClientProtocolException, IOException, JSONException {
    
        //Output is an arraylist of trends
        ArrayList<String> trends = null;
       
        
        //Setting the property file handler and retrieving url and places
        PropertyHandler.setConfigPath(propertiesMain);
        String searchTrendUrl = PropertyHandler.getProperty("searchTrends");
        
        PropertyConfigurator.configure(PropertyHandler.getProperty("logPath"));
       
        
        // Get postgres connection and call the location tables to get the codes
        
        MrPostgres postGresConnection = new MrPostgres();
        
        //Builing request objects for every city and getting the trends
        for (int numberOfCities = 0; numberOfCities < city.length; numberOfCities++) {
            
            //request object created
            HttpGet request = new HttpGet(searchTrendUrl + city[numberOfCities]);
            consumer.sign(request);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(request);
            
            //getting status code 
            int statusCode = response.getStatusLine().getStatusCode();
            System.out.println(response.getStatusLine().getReasonPhrase());
                        StringWriter writer = new StringWriter();
                        IOUtils.copy(response.getEntity().getContent(), writer);
                        String jsonStr = writer.toString();
                        JSONArray jsonArray = new JSONArray(jsonStr);
                        JSONObject jsonObj = jsonArray.getJSONObject(0);
                        JSONArray jsonArrayTrends = jsonObj.getJSONArray("trends");
                        for (int i = 0; i < jsonArrayTrends.length(); i++) {
                            JSONObject jsonObjTrend = jsonArrayTrends.getJSONObject(i);
                            System.out.println(jsonObjTrend);
                            //String searchUrl = jsonObjTrend.getString("url");
                        }
                        
        }
        return trends;
    }
    
/*                            System.out.println(searchUrl);
                            searchUrl = searchUrl.replaceAll("/search", "/search/tweets.json");
                            searchUrl = searchUrl.replaceAll("//twitter.com", "//api.twitter.com/1.1");
                            searchUrl = searchUrl.replaceAll("http", "https");
                            searchUrl = searchUrl + "&lang=en";
                            System.out.println(searchUrl);
*/    public static void main(String args[]) {
    
    }
}
