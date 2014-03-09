package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;


public class PropertyHandler {

	private static String configPath;
	private static Properties prop;
	/*private static Logger log = Logger.getLogger(PropertyHandler.class
			.getName());
*/
	/**
	 * Sets the path of the Properties file
	 * 
	 * @param path
	 */
	public static void setConfigPath(String path) {
		configPath = path;
	}

	public static String getConfigPath(){
		return configPath;
	}
	/**
	 * Property Handler method for all the classes
	 */
	private static void propertiesLoad() {
		FileInputStream config;
		try {
			prop = new Properties();
			config = new FileInputStream(configPath);
			prop.load(config);
			/*log.info("Properties loaded");
*/
		} catch (FileNotFoundException e) {
/*			log.error(MessageHandler.printStackTrace(e));*/
		} catch (IOException e) {
	//		log.error(MessageHandler.printStackTrace(e));
		}
	}

	/**
	 * Returns the string value for given property key
	 * 
	 * @param name
	 * @return
	 */
	public static String getProperty(String name) {
		if (prop == null) {
		//	#log.info(configPath);
			propertiesLoad();
		}
		return prop.getProperty(name);
	}
}