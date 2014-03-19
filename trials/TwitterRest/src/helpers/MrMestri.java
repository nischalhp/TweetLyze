package helpers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
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

	public static Stack<String> buildJobs(){
	
			Stack<String> jobStack = new Stack<String>();
			Logger log = null;

			try{
			Properties PropertyHandler = new Properties();
			PropertyHandler.load(new FileInputStream(propertiesMain));
			String logPath = PropertyHandler.getProperty("logPath");
			log = Logger.getLogger(MrMestri.class.getName());
			Connection postgresConn = MrPostgres.getPostGresConnection();

			if(postgresConn!=null){
				log.error("Postgres connection object could not be instantiated");
			}
			else{
				log.info("Connection object to postgres created successfully");
			}
			
			
			
			}catch(FileNotFoundException e){
				log.error("Properties file path error");
			} catch (IOException e) {
				log.error(e.getMessage());
			}
		
	}
	
	
}
