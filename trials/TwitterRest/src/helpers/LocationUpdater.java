package helpers;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class LocationUpdater {

	private static String propertiesMain = "properties/property.properties";

	// updating the postgres locations db
	public static void updateLocationTable() {

		Logger log = null;
		try {
			PropertyHandler.setConfigPath(propertiesMain);
			String places = PropertyHandler.getProperty("places");
			String locations = PropertyHandler.getProperty("geoId");
			String[] place = places.split(",");
			String[] location = locations.split(",");


			String logPath = PropertyHandler.getProperty("logPath");
			PropertyConfigurator.configure("properties/log.properties");
			log = Logger.getLogger(LocationUpdater.class.getName());

			String dbTables = PropertyHandler.getProperty("dbTables");
			PropertyHandler.setConfigPath(dbTables);

			for (int numberOfLocations = 0; numberOfLocations < place.length; numberOfLocations++) {

				// System.out.println("Place name is " +
				// place[numberOfLocations]
				// + " and the geoid is " + location[numberOfLocations]);

				// get a postGres connection object
				MrPostgres postgresObj = new MrPostgres();
				Connection conn = postgresObj.getPostGresConnection();
				log.info("Connection object to postgres database recieved");
				Statement stmt = conn.createStatement();
				String locationTableName = PropertyHandler.getProperty("location");
				System.out.println(locationTableName);
				break;
			}
		} catch (SQLException e) {
			log.error("Properties file not found");
		}
	}

	public static void main(String args[]) {

			updateLocationTable();
	}
}
