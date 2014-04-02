package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;

import oauth.signpost.OAuthConsumer;

public class ExampleTrials {

	public static BlockingQueue<OAuthConsumer> consumerPool = null;
	private static String propertiesMain = "properties/property.properties";

	public static void main(String args[]) throws FileNotFoundException, IOException {

//		getSystemTime();
		Properties PropertyHandler = new Properties();
		PropertyHandler.load(new FileInputStream(propertiesMain));
		int numberOfTrends = Integer.parseInt(PropertyHandler
				.getProperty("totalTrends"));
		int numberOfApps = Integer.parseInt(PropertyHandler
				.getProperty("numberOfAccounts"));
		int searchQueries = Integer.parseInt(PropertyHandler
				.getProperty("numberOfSearchQueries"));

		int totalSearchCalls = numberOfApps * searchQueries;
		System.out.println(totalSearchCalls);
		int perTrend = (int) totalSearchCalls / numberOfTrends;

		System.out.println(perTrend);
		double timeSpace = 60 / perTrend;
		System.out.println(timeSpace);
		double seconds = timeSpace * 60;
		System.out.println(seconds);
		int milliseconds = (int) seconds * 1000;

		System.out.println(milliseconds);
		// connectToPostgresServer();

		// consumerPool = ConsumerPool.buildConsumerPool();

		/*
		 * System.out.println(consumerPool.size());
		 * 
		 * MrTimer.startTask();
		 */

		/*
		 * OAuthConsumer consumerObj = null; try { consumerObj =
		 * consumerPool.take(); GetTrends.retrieveTrends(consumerObj); } catch
		 * (OAuthMessageSignerException | OAuthExpectationFailedException |
		 * OAuthCommunicationException | IOException | JSONException |
		 * ParseException | SQLException e) { e.printStackTrace();
		 * 
		 * } catch (InterruptedException e) { e.printStackTrace(); } catch
		 * (HttpException e) { String[] splitException =
		 * e.toString().split(":"); String statusCode =
		 * splitException[1].trim(); if (e.equals(statusCode)) {
		 * System.out.println("Too many requests"); try { OAuthConsumer
		 * consumerObj2 = consumerPool.take(); consumerPool.put(consumerObj);
		 * GetTrends.retrieveTrends(consumerObj); } catch (InterruptedException
		 * e1) { e1.printStackTrace(); } catch (OAuthMessageSignerException |
		 * OAuthExpectationFailedException | OAuthCommunicationException |
		 * IOException | JSONException | ParseException | HttpException |
		 * SQLException e2) { e2.printStackTrace();
		 * 
		 * } } }
		 * 
		 * Stack<MrUrl> jobs = MrMestri.buildJobs();
		 * //System.out.println(jobs.size());
		 * 
		 * /* for (int i = 0; i < jobs.size(); i++) {
		 */

		/*
		 * try { SearchTrends.getTweets(consumerPool.take(), jobs.pop()); }
		 * catch (OAuthMessageSignerException | OAuthExpectationFailedException
		 * | OAuthCommunicationException | IllegalStateException | IOException |
		 * JSONException | URISyntaxException | InterruptedException |
		 * SQLException | HttpException e) { e.printStackTrace(); }
		 */

	}

	public static void getSystemTime() {

		long millis = System.currentTimeMillis();
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(millis);
		System.out.println(cal.getTime());

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

	public static synchronized OAuthConsumer getConsumerObject() {
		return consumerPool.peek();
	}

	public static synchronized void putConsumerObject(OAuthConsumer obj)
			throws InterruptedException {
		consumerPool.put(obj);
	}
}
