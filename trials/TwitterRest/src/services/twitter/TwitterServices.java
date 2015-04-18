package services.twitter;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Properties;

import models.UrlDTO;
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

import util.DateUtilFunctions;
import util.PostgresConnector;

public class TwitterServices {
	private static String propertiesMain = "properties/property.properties";

	public static void getTweets(OAuthConsumer consumer, UrlDTO urlObj)
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
		Logger log = Logger.getLogger(TwitterServices.class.getName());

		URL url = urlObj.getUrl();
		HttpGet requestObj = new HttpGet(url.toURI());
		// log.info("signing object using consumerObj "
		// +consumer.getConsumerKey());
		consumer.sign(requestObj);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(requestObj);
		// System.out.println(response);
		// log.info(response);
		/*
		 * String res = response.toString(); String[] getLimit =
		 * res.split("x-rate-limit-remaining:"); String[] limit =
		 * getLimit[1].split(","); //log.info("limit for consumer Obj "
		 * +consumer.getConsumerKey() + " is " +limit[0]);
		 */
		int statusCode = response.getStatusLine().getStatusCode();

		if (statusCode == 200) {
			JSONObject searchArray = new JSONObject(IOUtils.toString(response
					.getEntity().getContent()));
			JSONArray statusesJson = searchArray.getJSONArray("statuses");
			// System.out.println(statusesJson);

			Connection conn = PostgresConnector.getPostGresConnection();
			String query = "INSERT INTO tweets(trendid,tweets) values(?,?)";
			PreparedStatement stmt = conn.prepareStatement(query);
			int parameterPlaceHolder = 1;

			PGobject toInsertObjectJson = new PGobject();
			toInsertObjectJson.setType("json");
			toInsertObjectJson.setValue(statusesJson.toString());

			PGobject toInsertObjectUuid = new PGobject();
			toInsertObjectUuid.setType("uuid");
			toInsertObjectUuid.setValue(urlObj.getId().toString());

			stmt.setObject(parameterPlaceHolder++, toInsertObjectUuid);
			stmt.setObject(parameterPlaceHolder++, toInsertObjectJson);

			boolean executeStatus = stmt.execute();
			log.info("execution status of inserting tweets to the table for the trend "
					+ url);
			conn.close();
		} else {
			log.error("Something went wrong will getting tweets for the trend with id "
					+ urlObj.getId());
			throw new HttpException("Check the status code"
					+ Integer.toString(statusCode));
		}
	}

	public static void retrieveTrends(OAuthConsumer consumer)
			throws OAuthMessageSignerException,
			OAuthExpectationFailedException, ClientProtocolException,
			IOException, JSONException, SQLException,
			OAuthCommunicationException, HttpException, ParseException {

		// Output is an arraylist of trends
		Logger log = null;
		// Setting the property file handler and retrieving url and places
		Properties PropertyHandler = new Properties();
		PropertyHandler.load(new FileInputStream(propertiesMain));
		PropertyConfigurator.configure(PropertyHandler.getProperty("logPath"));

		log = Logger.getLogger(TwitterServices.class.getName());
		log.info("Property file initialized , logger initialized");
		String trendsUrl = PropertyHandler.getProperty("trendsUrl");
		log.info("trend url retrieved " + trendsUrl);

		// Get postgres connection and call the location tables to get the codes

		Connection conn = PostgresConnector.getPostGresConnection();
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
				String toFireUrl = trendsUrl + id;
				HttpGet request = new HttpGet(toFireUrl);
				log.info("Firing request to " + toFireUrl);
				consumer.sign(request);
				log.info("Request object has been signed");
				HttpClient client = new DefaultHttpClient();
				HttpResponse response = client.execute(request);
				/*
				 * getting status code based on status code continue the process
				 */
				int statusCode = response.getStatusLine().getStatusCode();
				/*
				 * System.out.println(response.getStatusLine().getReasonPhrase()
				 * + " , status code " + statusCode);
				 * System.out.println(response);
				 */
				if (statusCode == 200) {
					StringWriter writer = new StringWriter();
					IOUtils.copy(response.getEntity().getContent(), writer);
					String jsonStr = writer.toString();
					JSONArray jsonArray = new JSONArray(jsonStr);
					JSONObject jsonObj = jsonArray.getJSONObject(0);
					JSONArray jsonArrayTrends = jsonObj.getJSONArray("trends");
					for (int i = 0; i < jsonArrayTrends.length(); i++) {
						JSONObject jsonObjTrend = jsonArrayTrends
								.getJSONObject(i);
						String name = jsonObjTrend.getString("name");
						Date currentDate = DateUtilFunctions.getCurrentDate();
						java.sql.Date sqlDate = new java.sql.Date(
								currentDate.getTime());
						String insertQuery = "INSERT INTO trends(trend,date,locationId) values(?,?,?)";
						PreparedStatement insertStmt = conn
								.prepareStatement(insertQuery);
						int parameterPlaceHolder = 1;
						insertStmt.setString(parameterPlaceHolder++, name);
						insertStmt.setDate(parameterPlaceHolder++, sqlDate);
						insertStmt.setString(parameterPlaceHolder++, id);
						log.info(" Trend insert query " + insertStmt.toString());
						insertStmt.executeUpdate();
					}

				} else {
					log.error("Something went wrong while retrieving trends,perform further operations based on the status code"
							+ statusCode);
					throw new HttpException(Integer.toString(statusCode));
				}
				// break;
			}
			// always close the connection object
			conn.close();
		}

		// return trends;
	}
}
