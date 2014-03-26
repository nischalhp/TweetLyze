package twitter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import main.MrUrl;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchTrends {

	public static void getTweets(OAuthConsumer consumer, MrUrl urlObj)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, IllegalStateException,
			JSONException, URISyntaxException {
/*
 * 
 *  We have the url and the trend id 
 *  once we get the array of tweets , we shall store it in postgres
 *  
 */
		URL url = urlObj.getUrl();
		HttpGet requestObj = new HttpGet(url.toURI());
		consumer.sign(requestObj);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(requestObj);
		System.out.println(response.getStatusLine());
		 System.out.println(IOUtils.toString(response.getEntity().getContent()));
		JSONObject searchArray = new JSONObject(IOUtils.toString(response
				.getEntity().getContent()));
		JSONArray statusesJson = searchArray.getJSONArray("statuses");
		System.out.println(statusesJson);
		

	}

}
