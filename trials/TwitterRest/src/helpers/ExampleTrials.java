package helpers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

public class ExampleTrials {
    
    public static void main(String args[]) throws SQLException {
    
        getSystemTime();
        connectToPostgresServer();
    }
    
    public static void getSystemTime() {
    
        long millis = System.currentTimeMillis();
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);
        System.out.println(cal.getTime().getHours());

        
    }
    
    public static void connectToPostgresServer() throws SQLException{
    	String hostName = "localhost";
    	String port = "5433";
    	String dbName = "tweetyfox";
    	String uname = "postgres";
    	String pwd = "tiger";
    	
    	Connection conn = null;
    	conn = DriverManager.getConnection("jdbc:postgresql://"+hostName+":"+port+"/"+dbName,uname,pwd);
    	if(conn!=null){
    		System.out.println("Connection is succesfull");
    		String q = "INSERT INTO location (id,city)"+ "VALUES (1235,'BANGALORE')";
    		Statement stmt = conn.createStatement();
    		int i = stmt.executeUpdate(q);
    		if(i==0){
    			System.out.println("Error");
    		}
    	}else{
    		System.out.println("Failed, FUCK OFF");
    	}
    	conn.close();
    }
}
