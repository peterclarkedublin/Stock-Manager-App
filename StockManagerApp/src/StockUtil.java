import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility methods for the stock management app
 * 
 * @author Kevin Phair
 * @date 30 Sep 2015
 */
public class StockUtil {

	/**
	 * Use the LocationPicker class to create a location picker window and return the result.
	 * The LocationPicker class is implemented as a modal window so that the calling dialog
	 * is blocked until the location is picked or the window is cancelled.
	 * 
	 * @return integer with the row id of the location from the location table
	 */
	static public int getLocation () {
		IntResult result = new IntResult();

		new LocationPicker ("stockdb", result);
		return result.i;
	}
	
	/**
	 * Open a new connection to the database and return the new open connection reference
	 * 
	 * @returns sql Connection for the database or null if failed
	 */
	static public Connection openDb () {
		String myDriver = "org.gjt.mm.mysql.Driver";
		String myUrl = "jdbc:mysql://192.168.1.160/stockdb";
		final Connection conn;
		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "smadmin", "admin");
			return conn;
		} catch (SQLException sqle) {
			System.out.println(sqle);
			return null;
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
	}
}
