package main;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MrRunnable implements Runnable {

	private static String propertiesMain = "properties/property.properties";
	private URL toFireUrl;

	MrRunnable(URL url) {

		this.toFireUrl = url;
	}

	@Override
	public void run() {

		Logger log = null;
		Properties propertyHandler = new Properties();
		try{
		propertyHandler.load(new FileInputStream(propertiesMain));
		String logPath = propertyHandler.getProperty("logPath");
		PropertyConfigurator.configure(new FileInputStream(logPath));
		log = Logger.getLogger(MrRunnable.class.getName());
		
		
		}catch(FileNotFoundException e){
			log.error(e);
		}catch(IOException e){
			log.error(e);
		}
	}

}
