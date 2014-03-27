package twitter;

import helpers.MrPostgres;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

import main.MrUrl;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpException;
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
import org.postgresql.util.PGobject;

public class SearchTrends {
	private static String propertiesMain = "properties/property.properties";

	public static void getTweets(OAuthConsumer consumer, MrUrl urlObj)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, OAuthCommunicationException,
			ClientProtocolException, IOException, IllegalStateException,
			JSONException, URISyntaxException, SQLException, HttpException {
		/*
		 * 
		 * We have the url and the trend id once we get the array of tweets , we
		 * shall store it in postgres
		 */
		Properties propertyHandler = new Properties();
		propertyHandler.load(new FileInputStream(propertiesMain));
		String logPath = propertyHandler.getProperty("logPath");
		PropertyConfigurator.configure(new FileInputStream(logPath));
		Logger log = Logger.getLogger(SearchTrends.class.getName());

		URL url = urlObj.getUrl();
		HttpGet requestObj = new HttpGet(url.toURI());
		consumer.sign(requestObj);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(requestObj);
		System.out.println(response);
		int statusCode = response.getStatusLine().getStatusCode();
		if (statusCode == 200) {
			JSONObject searchArray = new JSONObject(IOUtils.toString(response
					.getEntity().getContent()));
			JSONArray statusesJson = searchArray.getJSONArray("statuses");
			System.out.println(statusesJson);

			Connection conn = MrPostgres.getPostGresConnection();
			String query = "INSERT INTO tweets(trendid,tweets) values(?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			int parameterPlaceHolder = 1;

			PGobject toInsertObjectJson = new PGobject();
			toInsertObjectJson.setType("json");
			toInsertObjectJson.setValue(statusesJson.toString());

			PGobject toInsertObjectUuid = new PGobject();
			toInsertObjectUuid.setType("uuid");
			toInsertObjectUuid.setValue(urlObj.getId().toString());

			stmt.setObject(parameterPlaceHolder++,toInsertObjectUuid); 
			stmt.setObject(parameterPlaceHolder++, toInsertObjectJson);

			boolean executeStatus = stmt.execute();
			log.info("execution status of inserting tweets to the table "
					+ executeStatus);
			conn.close();
		} else {
			log.error("Something went wrong will getting tweets for the trend with id "
					+ urlObj.getId());
			throw new HttpException("Check the status code"
					+ Integer.toString(statusCode));
		}
	}
}
