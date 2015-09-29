import java.sql.*;
import java.util.Calendar;

public class TestApp
{

	public static void main(String[] args)
	{
		try
		{
			// create a mysql database connection
			String myDriver = "org.gjt.mm.mysql.Driver";
			String myUrl = "jdbc:mysql://192.168.1.160/stockdb";
			Class.forName(myDriver);
			Connection conn = DriverManager.getConnection(myUrl, "smadmin", "admin");

			// create a sql date object so we can use it in our INSERT statement
			Calendar calendar = Calendar.getInstance();
			java.sql.Date startDate = new java.sql.Date(calendar.getTime().getTime());

			// the mysql insert statement
			String query = " insert into users (first_name, last_name, date_created, is_admin, num_points)"
					+ " values (?, ?, ?, ?, ?)";

			// create the mysql insert preparedstatement
			PreparedStatement preparedStmt = conn.prepareStatement(query);
			preparedStmt.setString (1, "Barney");
			preparedStmt.setString (2, "Rumble");
			preparedStmt.setDate   (3, startDate);
			preparedStmt.setBoolean(4, false);
			preparedStmt.setInt    (5, 5000);

			/////////////////////////// execute the preparedstatement
			preparedStmt.execute();
			
			
		      String query1 = "SELECT * FROM users";
		      
		      // create the java statement
		      Statement st = conn.createStatement();
		       
		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query1);
		      
		      while (rs.next())
		      {
		        int id = rs.getInt("id");
		        String firstName = rs.getString("first_name");
		        String lastName = rs.getString("last_name");
		        Date dateCreated = rs.getDate("date_created");
		        boolean isAdmin = rs.getBoolean("is_admin");
		        int numPoints = rs.getInt("num_points");
		         
		        // print the results
		        System.out.format("%s, %s, %s, %s, %s, %s\n", id, firstName, lastName, dateCreated, isAdmin, numPoints);
		      }


			

			conn.close();
		}
		catch (Exception e)
		{
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());     
		}

	}
}
