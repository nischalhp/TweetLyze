package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import main.MrUrl;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import twitter.GetTrends;

public class ExampleTrials {

	public static BlockingQueue<OAuthConsumer> consumerPool = null;
	private static String propertiesMain = "properties/property.properties";

	public static void main(String args[]) throws FileNotFoundException,
			IOException, SQLException, InterruptedException {

		// MrTimer.startTask();

		// getSystemTime();
		// connectToPostgresServer();

		// waitingTimeTest();while (true)
		/*
		 * Properties PropertyHandler = new Properties();
		 * 
		 * PropertyHandler.load(new FileInputStream(propertiesMain)); int
		 * numberOfTrends = Integer.parseInt(PropertyHandler
		 * .getProperty("totalTrends")); int numberOfApps =
		 * Integer.parseInt(PropertyHandler .getProperty("numberOfAccounts"));
		 * int searchQueries = Integer.parseInt(PropertyHandler
		 * .getProperty("numberOfSearchQueries"));
		 * 
		 * int totalSearchCalls = numberOfApps * searchQueries; int perTrend =
		 * (int) totalSearchCalls / numberOfTrends;
		 * 
		 * double timeSpace = 15 / perTrend; double seconds = timeSpace * 60;
		 * int milliseconds = (int) seconds * 1000; Stack<MrUrl> jobs =
		 * MrMestri.buildJobs(); while (true) {
		 * System.out.println("Creating jobs"); jobs = MrMestri.buildJobs();
		 * 
		 * int jobToken = 0;
		 * 
		 * if (jobs.isEmpty()) {
		 * 
		 * System.out
		 * .println("No trends for the day , have to wait till trends get populated"
		 * ); Thread.sleep(10000); }
		 * 
		 * while (jobs.size() > 0) {
		 * 
		 * /* Now that the job stack is there we need to pick 80 jobs at a time
		 * and based on number of apps wait to run the process again
		 */

		/*
		 * jobs.pop(); jobToken++;
		 * 
		 * if (jobToken % 70 == 0) { System.out .println(
		 * "All jobs for the clock cycle complete , waiting for next clock cycle to start. Number of jobs completed "
		 * + jobToken); System.out.println("Job stack size" + jobs.size());
		 * Thread.sleep(100);
		 * 
		 * } } }
		 */

		String q = "HTTP/1.1 200 OK [cache-control: no-cache, no-store, must-revalidate, pre-check=0, post-check=0, content-length: 38328, content-type: application/json;charset=utf-8, date: Thu, 17 Apr 2014 09:22:31 GMT, expires: Tue, 31 Mar 1981 05:00:00 GMT, last-modified: Thu, 17 Apr 2014 09:22:31 GMT, pragma: no-cache, server: tfe, set-cookie: guest_id=v1%3A139772655166947606; Domain=.twitter.com; Path=/; Expires=Sat, 16-Apr-2016 09:22:31 UTC, status: 200 OK, strict-transport-security: max-age=631138519, x-access-level: read, x-content-type-options: nosniff, x-frame-options: SAMEORIGIN, x-rate-limit-limit: 180, x-rate-limit-remaining: 142, x-rate-limit-reset: 1397727388, x-transaction: f62b84ce49c9cb84, x-xss-protection: 1; mode=block]";
		String[] getLimit = q.split("x-rate-limit-remaining:");
		String[] limit = getLimit[1].split(",");
		System.out.println(limit[0]);

		Calendar cal = Calendar.getInstance();
		Date d = cal.getTime();
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"HH");
		String d1 = dateFormat.format(d);
		System.out.println(d1);
		SimpleDateFormat dateFormat2 = new SimpleDateFormat(
				"mm");
		String d2 = dateFormat2.format(d);
		System.out.println(d2);

	}

	/*
	 * try { searchTweets(consumerPool.take(), jobs.pop()); } catch
	 * (OAuthMessageSignerException | OAuthExpectationFailedException |
	 * OAuthCommunicationException | IllegalStateException | IOException |
	 * JSONException | URISyntaxException | InterruptedException | SQLException
	 * | HttpException e) { e.printStackTrace(); }
	 * 
	 * 
	 * consumerPool = ConsumerPool.buildConsumerPool();
	 * 
	 * getTrends();
	 * 
	 * System.out.println(consumerPool.size());
	 */

	public static void waitingTimeTest() throws FileNotFoundException,
			IOException {

		Properties PropertyHandler = new Properties();
		PropertyHandler.load(new FileInputStream(propertiesMain));
		int numberOfTrends = Integer.parseInt(PropertyHandler
				.getProperty("totalTrends"));
		int numberOfApps = Integer.parseInt(PropertyHandler
				.getProperty("numberOfAccounts"));
		int searchQueries = Integer.parseInt(PropertyHandler
				.getProperty("numberOfSearchQueries"));

		int totalSearchCalls = numberOfApps * searchQueries;
		System.out.println("Total search calls " + totalSearchCalls);
		int perTrend = (int) totalSearchCalls / numberOfTrends;

		System.out.println("per trend " + perTrend);
		double timeSpace = 15 / perTrend;
		System.out.println("timeSpace " + timeSpace);
		double seconds = timeSpace * 60;
		System.out.println("seconds" + seconds);
		int milliseconds = (int) seconds * 1000;

		System.out.println(milliseconds);
	}

	public static void getTrends() {

		OAuthConsumer consumerObj = null;
		try {
			consumerObj = consumerPool.take();
			GetTrends.retrieveTrends(consumerObj);
		} catch (OAuthMessageSignerException | OAuthExpectationFailedException
				| OAuthCommunicationException | IOException | JSONException
				| ParseException | SQLException e) {
			e.printStackTrace();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			String[] splitException = e.toString().split(":");
			String statusCode = splitException[1].trim();
			if (e.equals(statusCode)) {
				System.out.println("Too many requests");
				try {
					OAuthConsumer consumerObj2 = consumerPool.take();
					consumerPool.put(consumerObj);
					GetTrends.retrieveTrends(consumerObj2);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				} catch (OAuthMessageSignerException
						| OAuthExpectationFailedException
						| OAuthCommunicationException | IOException
						| JSONException | ParseException | HttpException
						| SQLException e2) {
					e2.printStackTrace();

				}
			}
		}
	}

	public static void searchTweets(OAuthConsumer consumer, MrUrl urlObj)

	throws OAuthMessageSignerException, OAuthExpectationFailedException,
			OAuthCommunicationException, ClientProtocolException, IOException,
			IllegalStateException, JSONException, URISyntaxException,
			SQLException, HttpException, InterruptedException {
		/*
		 * 
		 * We have the url and the trend id once we get the array of tweets , we
		 * shall store it in postgres
		 */

		URL url = urlObj.getUrl();
		HttpGet requestObj = new HttpGet(url.toURI());
		consumer.sign(requestObj);
		HttpClient client = new DefaultHttpClient();
		HttpResponse response = client.execute(requestObj);
		System.out.println(response);
		int statusCode = response.getStatusLine().getStatusCode();
		putConsumerObject(consumer);
	}

	public static void getSystemTime() {

		long millis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		System.out.println(cal.getTime());

	}

	public static void connectToPostgresServer() throws SQLException {
		String hostName = "localhost";
		String port = "5432";
		String dbName = "tweetlyze";
		String uname = "postgres";
		String pwd = "tiger";

		Connection conn = null;
		conn = DriverManager.getConnection("jdbc:postgresql://" + hostName
				+ ":" + port + "/" + dbName, uname, pwd);
		if (conn != null) {
			System.out.println("Connection is succesfull");
			String q = "SELECT * FROM trends";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(q);
			if (rs == null) {
				System.out.println("Error");
			} else {
				System.out.println(rs.next());
			}
		} else {
			System.out.println("Failed, FUCK OFF");
		}
		conn.close();
	}

	public static synchronized OAuthConsumer getConsumerObject() {
		return consumerPool.peek();
	}

	public static synchronized void putConsumerObject(OAuthConsumer obj)
			throws InterruptedException {
		consumerPool.put(obj);
	}
}
