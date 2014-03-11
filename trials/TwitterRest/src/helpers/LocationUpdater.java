package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LocationUpdater {

	private static String propertiesMain = "properties/property.properties";

	// updating the postgres locations db
	public static void updateLocationTable() {

		Logger log = null;
		try {
			Properties mainProperties = new Properties();
			mainProperties.load(new FileInputStream(propertiesMain));
			String places = mainProperties.getProperty("places");
			String locations = mainProperties.getProperty("geoId");
			String[] place = places.split(",");
			String[] location = locations.split(",");

			String logPath = mainProperties.getProperty("logPath");
			PropertyConfigurator.configure("properties/log.properties");
			log = Logger.getLogger(LocationUpdater.class.getName());

			String dbTables = mainProperties.getProperty("dbTables");
			Properties dbProperties = new Properties();
			dbProperties.load(new FileInputStream(dbTables));
			// get a postGres connection object
			Connection conn = MrPostgres.getPostGresConnection();
			log.info("Connection object to postgres database recieved");
			String locationTableName = dbProperties.getProperty("location");
			String queryToInsert = "INSERT INTO location(id,city) values(?,?)";
			PreparedStatement stmt = conn.prepareStatement(queryToInsert);
			for (int numberOfLocations = 0; numberOfLocations < place.length; numberOfLocations++) {

				// System.out.println("Place name is " +
				// place[numberOfLocations]
				// + " and the geoid is " + location[numberOfLocations]);
				stmt.setString(1, location[numberOfLocations]);
				stmt.setString(2, place[numberOfLocations]);
				log.info("insert query " + stmt.toString());
				stmt.executeUpdate();

			}
			conn.close();
		} catch (SQLException e) {
			log.error(" Something went wrong while creating connectin to postgres or inserting to location table in postgres");
		} catch (FileNotFoundException e) {
			log.error("Properties file for DB not found");
		} catch (IOException e) {
			log.error("Something went wrong while reading db properties file");
		}
	}

	public static void main(String args[]) {

		updateLocationTable();
	}
}
