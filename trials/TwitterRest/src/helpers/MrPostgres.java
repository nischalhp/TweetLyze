package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MrPostgres {

	private static String propertiesMain = "properties/property.properties";

	@SuppressWarnings("finally")
	public static Connection getPostGresConnection() {

		Connection conn = null;
		Logger log = null;
		try {
			Properties PropertyHandler = new Properties();
			PropertyHandler.load(new FileInputStream(propertiesMain));
			String hostName = PropertyHandler.getProperty("hostName");
			String port = PropertyHandler.getProperty("port");
			String dbName = PropertyHandler.getProperty("dbName");
			String uname = PropertyHandler.getProperty("uname");
			String pwd = PropertyHandler.getProperty("pwd");
			String logPath = PropertyHandler.getProperty("logPath");

			PropertyConfigurator.configure(logPath);
			log = Logger.getLogger(MrPostgres.class.getName());
			log.info(" Required credentials to connect to the postgres server is complete");
			conn = DriverManager.getConnection("jdbc:postgresql://" + hostName
					+ ":" + port + "/" + dbName, uname, pwd);
			if (conn != null) {
				log.info("Connection to postgres is succesfull");
			} else {
				log.error("Connection to postgres is unsuccesfull ");
			}
		} catch (SQLException e) {
			log.error("Something went wrong whille opening the connection to postgres " +e.getMessage() );
		} catch (FileNotFoundException e) {
			log.error("Properties file missing " +e.getMessage());
		} catch (IOException e) {
			log.error(e.getMessage());
		}
		return conn;

	}

	public static void main(String args[]) {
		Connection conn = getPostGresConnection();
	}
}
