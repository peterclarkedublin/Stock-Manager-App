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

public class ItemsWindow extends JFrame {

	private JPanel contentPane;
	private JTable table;

	/**
	 * Create the frame.
	 */
	public ItemsWindow () {
		setTitle("Items");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		scrollPane.setBounds(10, 11, 321, 251);
		contentPane.add(scrollPane);


		table = new JTable();

		table.setFillsViewportHeight(true);
		scrollPane.setViewportView(table);

		JButton btnExamine = new JButton("Examine");
		btnExamine.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				//database connection
				try
				{
		                       
					// create a mysql database connection
					String myDriver = "org.gjt.mm.mysql.Driver";
					String myUrl = "jdbc:mysql://192.168.1.160/stockdb";
					Class.forName(myDriver);
					Connection conn = DriverManager.getConnection(myUrl, "smadmin", "admin");
						
				      String query1 = "SELECT * FROM item";
				      
//				      SELECT item.id, sku.descr, location.descr, manuf.descr
//				      from item
//				      inner join sku
//				      on item.sku_id=sku.id
//				      inner join location
//				      on item.location_id=location.id
//				      inner join manuf
//				      on sku.manuf_id=manuf.id;				      
				      
				      // create the java statement
				      java.sql.Statement st = conn.createStatement();
				       
				      // execute the query, and get a java resultset
			
				      ResultSet rs = st.executeQuery(query1);
				      int numCounter;
				      for(numCounter = 0; rs.next(); numCounter++);
					  String[][] testArray = new String[numCounter][3];
					  rs.beforeFirst();
				      numCounter = 0;
				      
				      while (rs.next()){
				        int id = rs.getInt("id");
				        int sku = rs.getInt("sku_id");
				        int loc = rs.getInt("location_id");

				         
				        // print the results
				        System.out.println("" + id + sku + loc);
				        
						testArray[numCounter][0] = String.valueOf(id);
						testArray[numCounter][1] = String.valueOf(sku);
						testArray[numCounter][2] = String.valueOf(loc);
						numCounter++;
		                     
				      }

						table.setModel(new DefaultTableModel(
								testArray,new String[] {"Id" , "SKU" , "Location"}
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
		});

		btnExamine.setBounds(341, 11, 91, 23);
		contentPane.add(btnExamine);

		JButton btnAdjust = new JButton("Adjust");
		btnAdjust.setBounds(341, 45, 91, 23);
		contentPane.add(btnAdjust);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnExamine, btnAdjust}));

		setVisible(true);
	}

	public void updateTable(){




	}


}