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
import javax.swing.JTable;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ItemsWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;
    String sku;
    int skuId;
    String selectedItem;
	private JButton btnAdjust;
    

	/**
	 * Create the frame.
	 */
	public ItemsWindow () {
		setTitle("Items");
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

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e)
			
			//pass select jtable item to adjustment window
			{
				if (e.getClickCount() == 1)
				{
					String selectedData = null;

					JTable target = (JTable)e.getSource();

					int selectedRow = target.getSelectedRow();

					if (selectedRow >= 0) {
						btnAdjust.setEnabled(true);
						selectedItem = target.getValueAt(selectedRow, 1).toString();
						//selectedItemSku=target.getValueAt(selectedRow, 1);
						
						//selectedItemTxt.setText(selectedItemSku.toString());
						//System.out.println(selectedItemId);
					}

				}
			}
		});

		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);
		
		//load table contents from database
		updateTable();
		
		JButton btnExamine = new JButton("Refresh");
		btnExamine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				updateTable();
			}
		});

		btnExamine.setBounds(368, 8, 91, 23);
		contentPane.add(btnExamine);

		btnAdjust = new JButton("Adjust");
		btnAdjust.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AdjustItemWindow(selectedItem);
			}
		});
		btnAdjust.setBounds(368, 45, 91, 23);
		btnAdjust.setEnabled(false);
		contentPane.add(btnAdjust);

		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AddItemsWindow();
			}
		});
		btnAddItem.setBounds(370, 79, 89, 23);
		contentPane.add(btnAddItem);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnExamine, btnAdjust}));
		
		setVisible(true);
	}
	
	public void updateTable(){
		
		//database connection
		try
		{

			// create a mysql database connection
			Connection conn = StockUtil.openDb();

		      String query1 = "SELECT  count(1) as count, sku.sku, location.descr, sku.descr\n" +
		    		  "from item\n" +
		    		  "inner join sku\n" +
		    		  "on item.sku_id=sku.id\n" +
		    		  "inner join location\n" +
		    		  "on item.location_id=location.id\n" +
		    		  "inner join manuf\n" +
		    		  "on sku.manuf_id=manuf.id\n" +
		    		  "group by sku_id\n";
		      			      
		      // create the java statement
		      java.sql.Statement st = conn.createStatement();

		      // execute the query, and get a java resultset
		      ResultSet rs = st.executeQuery(query1);

		      int numCounter;
		      for(numCounter = 0; rs.next(); numCounter++);
			  String[][] itemArray = new String[numCounter][4];
			  rs.beforeFirst();
		      numCounter = 0;

		      while (rs.next()){
		        int qty = rs.getInt(1);
		        sku = rs.getString(2);
		        String loc = rs.getString(3);
		        String desc = rs.getString(4);
		            
				itemArray[numCounter][0] = String.valueOf(qty);
				itemArray[numCounter][1] = sku;
				itemArray[numCounter][2] = loc;			
				itemArray[numCounter][3] = String.valueOf(desc);
				numCounter++;

		      }
    
				table.setModel(new DefaultTableModel(
						itemArray,new String[] {"Qty" , "SKU" , "Location" , "Description"}
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