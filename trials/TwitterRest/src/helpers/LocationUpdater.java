package helpers;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LocationUpdater {

	private static String propertiesMain = "properties/property.properties";
	
	// updating the postgres locations db
	public static void updateLocationTable() throws IOException {

		PropertyHandler.setConfigPath(propertiesMain);
		String places = PropertyHandler.getProperty("places");
		String locations = PropertyHandler.getProperty("geoId");
		String[] place = places.split(",");
		String[] location = locations.split(",");

		for (int numberOfLocations = 0; numberOfLocations < place.length; numberOfLocations++) {

//			System.out.println("Place name is " + place[numberOfLocations]
//					+ " and the geoid is " + location[numberOfLocations]);


		}
	}

	public static void main(String args[]) {

		try {
			updateLocationTable();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
