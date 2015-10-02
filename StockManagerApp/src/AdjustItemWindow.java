import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.mysql.jdbc.PreparedStatement;

import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AdjustItemWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
    String sku;
    int skuId;
    Object selectedItemQty;
    Object selectedItemSku;
    String selectedItem; //item passed in from itemWindow
	private int[] itemLocId;
	private int selectedRow;
	private JButton btnEdit;
	private JButton btnDelete;

	/**
	 * Create the frame.
	 */
	public AdjustItemWindow (String selectedItem) {
		this.selectedItem = selectedItem;
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent arg0) {
				
				updateTable();
			}
		});
		
		setTitle("Adjust Items");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 485, 341);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 348, 280);
		contentPane.add(scrollPane);
		
		final JLabel selectedItemTxt = new JLabel("...");
		selectedItemTxt.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
	
			}
		});
		
		selectedItemTxt.setBounds(371, 212, 91, 14);
		contentPane.add(selectedItemTxt);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e)
			{
				if (e.getClickCount() == 1)
				{
					String selectedData = null;

					JTable target = (JTable)e.getSource();

					selectedRow = target.getSelectedRow();

					if(selectedRow >= 0){
						selectedItemQty=target.getValueAt(selectedRow, 0);
						selectedItemSku=target.getValueAt(selectedRow, 1);
					
						btnEdit.setEnabled(true);
						btnDelete.setEnabled(true);
						
					selectedItemTxt.setText(selectedItemSku.toString());
					}
					//System.out.println(selectedItemId);
					
				}
			}
		});

		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);

		JButton btnExamine = new JButton("Refresh");
		btnExamine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				updateTable();

			}
		});

		btnExamine.setBounds(371, 11, 91, 23);
		contentPane.add(btnExamine);

		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AddItemsWindow();
			}
		});
		btnAddItem.setBounds(371, 45, 89, 23);
		contentPane.add(btnAddItem);
		
		btnEdit = new JButton("Edit Loc.");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String moveQty = JOptionPane.showInputDialog(null,
                        "Enter Qty to move (1 - " + selectedItemQty + " ) ?", null);
				try {
					if(moveQty == null || moveQty.length() == 0 || Integer.parseInt(moveQty) < 1 || Integer.parseInt(moveQty) > Integer.parseInt(selectedItemQty.toString())){
						
						return;
					}
				} catch (NumberFormatException e2) {
					// TODO Auto-generated catch block
					return;
				}
				
				int newLoc = StockUtil.getLocation();
				
				if(newLoc == -1){
					return;
				}
				
				//System.out.println(newLoc);
				
				//update the item's location
				
				try
			    {
			      // create the mysql database connection
			      Connection updateItem = StockUtil.openDb();
			      
			      String queryQtyMove = "SELECT item.id from item where item.location_id =" + itemLocId[selectedRow] +
			    		  				" and item.sku_id = " + skuId;
	      
			      System.out.println(queryQtyMove);
			      
			      ResultSet rs2 = updateItem.createStatement().executeQuery(queryQtyMove);
		
			      if(rs2.next()){
			    	  System.out.println("Moving " + moveQty + "items");
			    	  for(int i = 0; i < Integer.parseInt(moveQty); i++){
			    		  
			    		  String queryUpdateItem = "update stockdb.item set location_id = ? where id = ?";
			    		  System.out.println(queryUpdateItem);

			    		  PreparedStatement preparedStmt = (PreparedStatement) updateItem.prepareStatement(queryUpdateItem);

			    		  preparedStmt.setInt(1, newLoc);

			    		  preparedStmt.setInt(2, rs2.getInt(1));

			    		  // execute the preparedstatement
			    		  preparedStmt.execute();
			    		  rs2.next();
			    	  }
			      }
			       
			      updateItem.close();

			    }
			    catch (Exception e1)
			    {
			      System.err.println("Got an exception! ");
			      System.err.println(e1.getMessage());
			    }
				
				
			}
		});
		btnEdit.setBounds(371, 237, 89, 23);
		btnEdit.setEnabled(false);
		contentPane.add(btnEdit);
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String deleteQty = JOptionPane.showInputDialog(null,
                        "Enter Qty to delete (1 - " + selectedItemQty + " ) ?", null);
				try {
					if(deleteQty == null || deleteQty.length() == 0 || Integer.parseInt(deleteQty) < 1 || Integer.parseInt(deleteQty) > Integer.parseInt(selectedItemQty.toString())){
						return;
					}
				} catch (NumberFormatException e2) {
					return;
				}
				
				// delete selected item id from database
				try {
					// create the mysql database connection
					Connection deleteItem = StockUtil.openDb();

					String queryDelete = "SELECT item.id from item where item.location_id =" + itemLocId[selectedRow]
							+ " and item.sku_id = " + skuId;
					System.out.println(queryDelete);
					ResultSet rs2 = deleteItem.createStatement().executeQuery(queryDelete);

					if (rs2.next()) {
						System.out.println("Deleting " + deleteQty + " items");
						for (int i = 0; i < Integer.parseInt(deleteQty); i++) {

							String queryDeleteItem = "delete from stockdb.item where id = ?";
							System.out.println(queryDeleteItem);

							PreparedStatement preparedStmt = (PreparedStatement) deleteItem
									.prepareStatement(queryDeleteItem);

							preparedStmt.setInt(1, rs2.getInt(1));

							// execute the preparedstatement
							preparedStmt.execute();
							rs2.next();
						}
					}
					// String queryDelete = "delete from stockdb.item where id =
					// ?" ;
					// PreparedStatement preparedStmt = (PreparedStatement)
					// deleteItem.prepareStatement(queryDelete);
					// preparedStmt.setInt(1,
					// Integer.parseInt(selectedItemQty.toString()));
					//
					// // execute the preparedstatement
					// preparedStmt.execute();

					deleteItem.close();

					updateTable();
				} catch (Exception e1) {
					System.err.println("Got an exception! ");
					System.err.println(e1.getMessage());
				}

			}
		});
		btnDelete.setBounds(371, 271, 89, 23);
		btnDelete.setEnabled(false);
		contentPane.add(btnDelete);
		
		JLabel selectedItemLbl = new JLabel("Selected Item:");
		selectedItemLbl.setBounds(368, 189, 70, 14);
		contentPane.add(selectedItemLbl);
		

		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnExamine}));

		setVisible(true);
	}

	public void updateTable(){
		
		try
		{

			// create a mysql database connection
			Connection conn = StockUtil.openDb();

		      String query1 = "SELECT count(1) as count, sku.sku, location.descr, manuf.descr, item.sku_id, item.location_id\n" +
				      "from item\n" +
				      "inner join sku\n" +
				      "on item.sku_id=sku.id\n" +
				      "inner join location\n" +
				      "on item.location_id=location.id\n" +
				      "inner join manuf\n" +
				      "on sku.manuf_id=manuf.id\n" +
		      			"where sku.sku =\""+selectedItem+"\"\n" +
		      			"group by item.location_id";
		      

		      // create the java statement
		      java.sql.Statement st = conn.createStatement();

		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query1);

		      int numCounter;
		      for(numCounter = 0; rs.next(); numCounter++);
			  String[][] itemArray = new String[numCounter][4];
			  itemLocId = new int[numCounter];
			  rs.beforeFirst();
		      numCounter = 0;

		      while (rs.next()){
		    	System.out.print(".");
		        int qty = rs.getInt(1);
		        sku = rs.getString(2);
		        String loc = rs.getString(3);
		        String manuf = rs.getString(4);
		        skuId = rs.getInt(5);
		        itemLocId[numCounter] = rs.getInt(6);
		            
				itemArray[numCounter][0] = String.valueOf(qty);
				itemArray[numCounter][1] = sku;
				itemArray[numCounter][2] = loc;			
				itemArray[numCounter][3] = manuf;
				numCounter++;

		      }
    
				table.setModel(new DefaultTableModel(
						itemArray,new String[] {"Qty" , "SKU" , "Location" , "Manufacturer"}
						));
			conn.close();
		}
		catch (Exception e)
		{
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}	
}