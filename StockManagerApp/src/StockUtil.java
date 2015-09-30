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
	
}
