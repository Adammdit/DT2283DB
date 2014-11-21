import java.sql.*;
import java.io.*;
public class Simple {
  public static void main (String args [])
	throws SQLException, IOException {
    try {
	  Class.forName("oracle.jdbc.driver.OracleDriver");
      System.out.println("driver loaded");
    } catch (ClassNotFoundException e) {
	    System.out.println ("Could not load the driver");
	}
    String servername = "localhost";
    //"147.252.224.76";
    String portnumber = "1521";
    String servicename = "xe";
    //sid = "ORA11GDB";
    String url = "jdbc:oracle:thin:@" + servername + ":" + portnumber + ":" + servicename;// sid;
    String user, pass;
    
	user = "dit";//readEntry("userid  : ");
	pass = "miodasso32";//readEntry("password: ");
	System.out.println(">> PLEASE MAKE SALE <<");
	//System.out.println(url);
    DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
	//System.out.println ("before connection");

    Connection conn = DriverManager.getConnection(url, user, pass);
	//System.out.println ("after connection");
    /*
	Statement stmt = conn.createStatement ();

    ResultSet rset = stmt.executeQuery
	("select customer_name, customer_phoneno from dit.customer");
    while (rset.next()) {
	System.out.println(rset.getString(1) + " " +
	                   rset.getString(2));
    }
    */
    String isReg = readEntry("Is customer registered [y/n]: ").toLowerCase();
    if(isReg.equals("y")) {
    	String cnum = readEntry("Enter the customer number: ");
        CallableStatement stmt = conn.prepareCall ("{call dit.find_customer(?,?,?)}");
        stmt.setString(1,cnum);
        stmt.registerOutParameter(2,Types.VARCHAR);
        stmt.registerOutParameter(3,Types.NUMERIC);
        stmt.execute();
        
        if(stmt.getString(2) == null && stmt.getString(3) == null) {
        	String ans = readEntry("Customer not in database do you want to register [y/n]: ").toLowerCase();       	
        	if(ans.equals("y")) {
        		String cName = readEntry("Enter the customer name: ");
        		String cPhone = readEntry("Enter the customer phone number: ");
        		
        		Statement stmt2 = conn.createStatement ();
        	    ResultSet rset = stmt2.executeQuery("select customer_id from dit.customer");
        	    String cNo = "";
        	    while (rset.next()) {
        	    	cNo = rset.getString(1);
        	    }
        	    // Increment customer number
        	    cNo = cNo.substring(1);
        	    int temp = Integer.parseInt(cNo);
        	    temp++;
        	    cNo = "C" + temp;
        	    
        	    
        	    CallableStatement stmt3 = conn.prepareCall ("{call dit.register_customer(?,?,?)}");
        	    stmt3.setString(1, cNo);
                stmt3.setString(2, cName);
        	    stmt3.setInt(3, 877875001);
                stmt3.execute(); 
        	    
        	    System.out.println(temp);
        	    
        	    
        	}
        		
        }
        System.out.println("Name         Phone");
        System.out.println(stmt.getString(2)+" | "+stmt.getString(3));
        
        stmt.close();
    
    }
    
    
    
    
    
    
    
    
    
    //stmt.close();
    
    conn.close();
  }
//readEntry function -- to read input string
static String readEntry(String prompt) {
	   try {
		 StringBuffer buffer = new StringBuffer();
		 System.out.print(prompt);
		 System.out.flush();
		 int c = System.in.read();
		 while (c != '\n' && c != -1) {
		   buffer.append((char)c);
		   c = System.in.read();
	     }
	     return buffer.toString().trim();
	  }  catch (IOException e) {
		 return "";
	     }
  }
}
