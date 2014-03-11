package twitter;

import helpers.MrPostgres;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

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

	public void retrieveTrends(OAuthConsumer consumer)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, ClientProtocolException,
			IOException, JSONException, SQLException,
			OAuthCommunicationException {

		// Output is an arraylist of trends
		ArrayList<String> trends = null;
		Logger log = null;
		// Setting the property file handler and retrieving url and places
		Properties PropertyHandler = new Properties();
		PropertyHandler.load(new FileInputStream(propertiesMain));
		PropertyConfigurator.configure(PropertyHandler.getProperty("logPath"));

		log = Logger.getLogger(GetTrends.class.getName());
		log.info("Property file initialized , logger initialized");
		String trendsUrl = PropertyHandler.getProperty("trendsUrl");
		log.info("trend url retrieved " + trendsUrl);

		// Get postgres connection and call the location tables to get the codes

		Connection conn = MrPostgres.getPostGresConnection();
		if (conn != null) {
			log.info("Connection object to postgres is created");

		}

		String dbPath = PropertyHandler.getProperty("dbTables");
		Properties dbPropertyHandler = new Properties();
		dbPropertyHandler.load(new FileInputStream(dbPath));
		String selectQuery = "Select id,city from "
				+ dbPropertyHandler.getProperty("location");
		PreparedStatement stmt = conn.prepareStatement(selectQuery);
		log.info("Statement prepared to get city and id from location table "
				+ stmt.toString());
		ResultSet rs = stmt.executeQuery();
		if (rs == null) {
			log.error("Unable to fetch data of city and id from "
					+ dbPropertyHandler.getProperty("location"));
		} else {
			while (rs.next()) {
				String id = rs.getString(1);
				String cityName = rs.getString(2);
				// request object created
				HttpGet request = new HttpGet(trendsUrl + id);
				consumer.sign(request);
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				int statusCode = response.getStatusLine().getStatusCode();
				log.info(response.getStatusLine().getReasonPhrase()
						+ " , status code " + statusCode);
				StringWriter writer = new StringWriter();
				IOUtils.copy(response.getEntity().getContent(), writer);
				String jsonStr = writer.toString();
				System.out.println(jsonStr);
				JSONArray jsonArray = new JSONArray(jsonStr);
				JSONObject jsonObj = jsonArray.getJSONObject(0);
				JSONArray jsonArrayTrends = jsonObj.getJSONArray("trends");
			}
		}

		// getting status code
		/*
		 * for (int i = 0; i < jsonArrayTrends.length(); i++) { JSONObject
		 * jsonObjTrend = jsonArrayTrends.getJSONObject(i);
		 * System.out.println(jsonObjTrend); // String searchUrl =
		 * jsonObjTrend.getString("url");
		 * 
		 * }
		 */
		// return trends;
	}

	/*
	 * System.out.println(searchUrl); searchUrl =
	 * searchUrl.replaceAll("/search", "/search/tweets.json"); searchUrl =
	 * searchUrl.replaceAll("//twitter.com", "//api.twitter.com/1.1"); searchUrl
	 * = searchUrl.replaceAll("http", "https"); searchUrl = searchUrl +
	 * "&lang=en"; System.out.println(searchUrl);
	 */public static void main(String args[]) {

	}
}
