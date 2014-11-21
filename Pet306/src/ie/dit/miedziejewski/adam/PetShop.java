package ie.dit.miedziejewski.adam;

import java.sql.*;
import java.io.*;
import java.sql.CallableStatement;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PetShop {
	// Main
	public static void main (String args [])
			throws SQLException, IOException {
		try {
				Class.forName("oracle.jdbc.driver.OracleDriver");
				System.out.println("driver loaded");
		    } catch (ClassNotFoundException e) {
			    System.out.println ("Could not load the driver");
			}
		
		System.out.println("Is customer registered [y/n]?");
		BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
		String s = bufferRead.readLine().toLowerCase();
	
	    if(s.equals("y")) {
	    	System.out.println("Enter the customer number.");
	    	BufferedReader bufferRead4 = new BufferedReader(new InputStreamReader(System.in));
	  	    String cNo = bufferRead4.readLine();
	  	    findCustomer(cNo);
	  	    
	    }
	    else if(s.equals("n")) {
	    	System.out.println("Do you want to register [y/n]?");
			BufferedReader bufferRead1 = new BufferedReader(new InputStreamReader(System.in));
			String s1 = bufferRead1.readLine().toLowerCase();
			if(s1.equals("y")) {
				System.out.println("Enter the customer name.");
		    	BufferedReader bufferRead2 = new BufferedReader(new InputStreamReader(System.in));
		  	    String cName = bufferRead2.readLine();
		  	    System.out.println("Enter the customer phone number.");
		    	BufferedReader bufferRead3 = new BufferedReader(new InputStreamReader(System.in));
		  	    Integer cPhone = Integer.parseInt(bufferRead3.readLine());
		  	    
		  	    Statement stmt = getDBConnection().createStatement ();
		  	    ResultSet rset = stmt.executeQuery("select customer_id from dit.customer");
		  	    String cNo = "";
		  	    Integer min = 0;
		  	    while (rset.next()) {
		  	    	cNo = rset.getString(1);
		  	    	cNo = cNo.substring(1);
		  	    	int temp = Integer.parseInt(cNo);
		  	    	if(temp > min) {
		  	    		min = temp;
		  	    	}
		  	    }
			  	min++;
			  	cNo = "C" + min;
			  	insertCustomer(cNo, cName, cPhone);
			  	System.out.println("New customer: " + cNo + " " + cName + " " + cPhone);
			}
	    }
	}
	
    private static void insertCustomer(String cno, String cname, Integer cphone) throws SQLException {   	 
    	Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			dbConnection = getDBConnection();			
			CallableStatement preparedStatement1 = dbConnection.prepareCall ("{call dit.register_customer(?,?,?)}");
			preparedStatement1.setString(1, cno);
			preparedStatement1.setString(2, cname);
			preparedStatement1.setInt(3, cphone); 
			preparedStatement1.executeUpdate();
			
			System.out.println("Record was added to CUSTOMER table!");
 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
    }
    
    private static void findCustomer(String cno) throws SQLException {   	 
    	Connection dbConnection = null;
		PreparedStatement preparedStatement = null;
		
		try {
			dbConnection = getDBConnection();			
			CallableStatement preparedStatement1 = dbConnection.prepareCall ("{call dit.find_customer(?,?,?)}");
			preparedStatement1.setString(1,cno);
			preparedStatement1.registerOutParameter(2,Types.VARCHAR);
			preparedStatement1.registerOutParameter(3,Types.NUMERIC); 
			preparedStatement1.executeUpdate();
			
			System.out.println("Name         Phone");
	        System.out.println(preparedStatement1.getString(2)+" | "+preparedStatement1.getString(3));
 
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			if (preparedStatement != null) {
				preparedStatement.close();
			}
			if (dbConnection != null) {
				dbConnection.close();
			}
		}
    }
    
    private static Connection getDBConnection() {   	 
		Connection dbConnection = null;
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println(e.getMessage());
		}
		try {
			String servername = "localhost";
		    //"147.252.224.76";
		    String portnumber = "1521";
		    String servicename = "xe";
		    //sid = "ORA11GDB";
		    String url = "jdbc:oracle:thin:@" + servername + ":" + portnumber + ":" + servicename;// sid;
		    String user, pass;
		    
			user = "dit";//readEntry("userid  : ");
			pass = "miodasso32";//readEntry("password: ");
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection conn = DriverManager.getConnection(url, user, pass);
			return conn;
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return dbConnection;
    }
}

