package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Properties;
import java.util.Stack;

import org.apache.log4j.Logger;

/*
 This class will help in building the job stack 
 that will be used by threads to work on
 Mestri in kannada means a person who assigns jobs 
 to people in construction business
 */

public class MrMestri {
	private static String propertiesMain = "properties/property.properties";

	public static Stack<URL> buildJobs() {

		Stack<URL> jobStack = new Stack<URL>();
		Logger log = null;

		try {
			Properties PropertyHandler = new Properties();
			PropertyHandler.load(new FileInputStream(propertiesMain));
			String logPath = PropertyHandler.getProperty("logPath");
			log = Logger.getLogger(MrMestri.class.getName());
			Connection postgresConn = MrPostgres.getPostGresConnection();

			String dbTables = PropertyHandler.getProperty("dbTables");
			Properties dbTablesPropertyHandler = new Properties();
			dbTablesPropertyHandler.load(new FileInputStream(dbTables));
			log.info("db tables property file loaded");

			if (postgresConn == null) {
				log.error("Postgres connection object could not be instantiated");
			} else {
				log.info("Connection object to postgres created successfully");
			}
			/*
			 * This is the logic that builds the jobs based on the number of
			 * authorized apps
			 */
			int numberOfSearchQueries = Integer.parseInt(PropertyHandler
					.getProperty("numberOfAccoutns"));

			int totalSearchQueries = numberOfSearchQueries
					* Integer.parseInt(PropertyHandler
							.getProperty("numberOfSearchQueries"));

			int perTrendCalls = (totalSearchQueries
					/ Integer.parseInt(PropertyHandler
							.getProperty("totalTrends")))
			for (int jobPerTrends = 0; jobPerTrends < Integer
					.parseInt(PropertyHandler.getProperty("totalTrends"))
					* perTrendCalls; jobPerTrends++) {

				String selectTrends = "SELECT trend,date from "
						+ dbTablesPropertyHandler.getProperty("trends")
						+ " where date = '" + Utilities.getCurrentDate() + "'";

				log.info(selectTrends);

				PreparedStatement stmt = postgresConn
						.prepareStatement(selectTrends);

				ResultSet rs = stmt.executeQuery();

				if (rs == null) {
					log.error("Was unable to fetch trends for the date"
							+ Utilities.getCurrentDate()
							+ " , something has gone wrong ");
				} else {
					log.info("Building the stack of jobs");
					while (rs.next()) {
						String trend = rs.getString(1);
						String searchUrl = PropertyHandler
								.getProperty("searchUrl");
						trend = URLEncoder.encode(trend, "ISO-8859-1");
						searchUrl = searchUrl + trend + "&lang=en";
						URL searchURI = new URL(searchUrl);
						jobStack.push(searchURI);

					}

				}

			}
			postgresConn.close();
		} catch (FileNotFoundException e) {
			log.error("Properties file path error");
		} catch (IOException e) {
			log.error(e.getMessage());
		} catch (ParseException e) {
			log.error("Error while parsing the date");
		} catch (SQLException e) {
			log.error(e.getMessage());
		}

		return jobStack;
	}

}
