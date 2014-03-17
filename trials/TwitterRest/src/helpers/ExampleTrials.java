package helpers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.Calendar;
import java.util.concurrent.BlockingQueue;

import main.ConsumerPool;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpException;
import org.json.JSONException;

import twitter.GetTrends;

public class ExampleTrials {

	public static void main(String args[]) {

		getSystemTime();
		// connectToPostgresServer();
		BlockingQueue<OAuthConsumer> consumerPool = ConsumerPool
				.buildConsumerPool();

		try {
			OAuthConsumer consumerObj = consumerPool.take();
			GetTrends trendsObj = new GetTrends();
			trendsObj.retrieveTrends(consumerObj);
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException | JSONException | ParseException
				 | SQLException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (HttpException e){
			if(e.equals("429")){
				
			}
		}
	}

	public static void getSystemTime() {

		long millis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		System.out.println(cal.getTime().getHours());

	}

	public static void connectToPostgresServer() throws SQLException {
		String hostName = "localhost";
		String port = "5433";
		String dbName = "tweetyfox";
		String uname = "postgres";
		String pwd = "tiger";

		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:postgresql://" + hostName
				+ ":" + port + "/" + dbName, uname, pwd);
		if (conn != null) {
			System.out.println("Connection is succesfull");
			String q = "INSERT INTO location (id,city)"
					+ "VALUES (1235,'BANGALORE')";
			Statement stmt = conn.createStatement();
			int i = stmt.executeUpdate(q);
			if (i == 0) {
				System.out.println("Error");
			}
		} else {
			System.out.println("Failed, FUCK OFF");
		}
		conn.close();
	}
}
