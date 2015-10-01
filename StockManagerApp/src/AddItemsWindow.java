import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

import com.mysql.jdbc.PreparedStatement;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;

public class AddItemsWindow extends JFrame {

	private JPanel contentPane;
	private JTextField qtyTextField;
	String[][] skuArray = null;
	private Connection skuConn;

	/**
	 * Create the frame.
	 */
	public AddItemsWindow () {
		setTitle("Add Items To Stock");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 170);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblNewLabel = new JLabel("Description");
		lblNewLabel.setBounds(186, 15, 58, 14);
		contentPane.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("SKU");
		lblNewLabel_1.setBounds(62, 15, 31, 14);
		contentPane.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("UPC");
		lblNewLabel_2.setBounds(288, 15, 31, 14);
		contentPane.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Manufacturer");
		lblNewLabel_3.setBounds(348, 15, 65, 14);
		contentPane.add(lblNewLabel_3);

		JLabel lblNewLabel_4 = new JLabel("Location");
		lblNewLabel_4.setBounds(10, 84, 47, 14);
		contentPane.add(lblNewLabel_4);

		//create string Array to populate combobox
		String[] skuComboArray = null;

		try
		{

			// create a mysql database connection
//			String myDriver = "org.gjt.mm.mysql.Driver";
//			String myUrl = "jdbc:mysql://127.0.0.1/stockdb";
//			Class.forName(myDriver);
			skuConn = StockUtil.openDb();

			//SKU database
			String skuQuery = "SELECT * FROM sku";

			// create the java statement
			java.sql.Statement st = skuConn.createStatement();

			// execute the query, and get a java resultset

			ResultSet skuRs = st.executeQuery(skuQuery);

			int numCounter = 0;

			for(numCounter = 0; skuRs.next(); numCounter++);

			skuArray = new String[numCounter][5];

			skuRs.beforeFirst();
			numCounter = 0;

			while (skuRs.next()){
				int id = skuRs.getInt(1);
				String skuDesc = skuRs.getString(2);
				String skuNum = skuRs.getString(3);
				String upc = skuRs.getString(4);
				int manufacId = skuRs.getInt(5);

				skuArray[numCounter][0] = String.valueOf(id);
				skuArray[numCounter][1] = skuDesc;
				skuArray[numCounter][2] = skuNum;
				skuArray[numCounter][3] = upc;
				skuArray[numCounter][4] = String.valueOf(manufacId);
				numCounter++;

				//create 1D array from skuArray for combox selector

				skuComboArray = new String[skuArray.length];

				for(int i = 0; i < skuComboArray.length;i++){
					skuComboArray[i] = skuArray[i][2];
				}

				for(String str: skuComboArray){

					System.out.println(str);
				}

			}

			skuConn.close();
		}
		catch (Exception e)
		{
			System.err.println("Got an exception!");
			System.err.println(e.getMessage());
			e.printStackTrace();
		}

		final JLabel manufDesc = new JLabel("...");
		manufDesc.setBounds(348, 40, 65, 14);
		contentPane.add(manufDesc);

		final JLabel descDesc = new JLabel("...");
		descDesc.setBounds(186, 40, 92, 14);
		contentPane.add(descDesc);

		final JLabel idDesc = new JLabel("...");
		idDesc.setBounds(154, 40, 46, 14);
		contentPane.add(idDesc);


		final JComboBox skuComboBox = new JComboBox(skuComboArray);
		skuComboBox.setToolTipText("Please Select a Item SKU");
		skuComboBox.setSelectedItem(null);
		skuComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				descDesc.setText(skuArray[skuComboBox.getSelectedIndex()][1]);
				idDesc.setText(String.valueOf(skuArray[skuComboBox.getSelectedIndex()][0]));

				// execute the query, and get a java resultset
				try {
					skuConn = StockUtil.openDb();
					ResultSet manufRs = skuConn.createStatement().
							executeQuery("SELECT descr FROM stockdb.manuf WHERE id="+skuArray[skuComboBox.getSelectedIndex()][4]);
					if(manufRs.next())
						manufDesc.setText(manufRs.getString(1));
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});

		skuComboBox.setBounds(62, 34, 82, 20);
		contentPane.add(skuComboBox);

		JLabel lblQty = new JLabel("Qty.");
		lblQty.setBounds(10, 15, 31, 14);
		contentPane.add(lblQty);

		qtyTextField = new JTextField();
		lblQty.setLabelFor(qtyTextField);
		qtyTextField.setBounds(10, 34, 42, 20);
		contentPane.add(qtyTextField);
		qtyTextField.setColumns(10);
		JLabel upcDesc = new JLabel("...");
		upcDesc.setBounds(288, 40, 46, 14);
		contentPane.add(upcDesc);

		String[] locId = {"1"};
		JComboBox locComboBox = new JComboBox(locId);
		locComboBox.setBounds(62, 81, 82, 20);
		contentPane.add(locComboBox);

		JLabel lblNewLabel_5 = new JLabel("ID");
		lblNewLabel_5.setBounds(155, 15, 21, 14);
		contentPane.add(lblNewLabel_5);




		JButton btnAddItem = new JButton("Add Item");
		btnAddItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AddItemsWindow();
			}
		});


		JButton btnAddToDbse = new JButton("Save");
		btnAddToDbse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				//add product to database
				try
				{

					// create a mysql database connection

					Connection conn = StockUtil.openDb();

					// Item table mysql insert statement
					String query = " insert into item (sku_id, location_id)"
							+ " values (?, ?)";

					// Item table mysql insert preparedstatement
					PreparedStatement preparedStmt = (PreparedStatement) conn.prepareStatement(query);
					preparedStmt.setString (1, String.valueOf(skuArray[skuComboBox.getSelectedIndex()][0]));
					preparedStmt.setInt  (2, 1 );

					//execute the preparedstatement
					for(int i = 0; i < Integer.valueOf(qtyTextField.getText());i++){

						preparedStmt.execute();
						
						System.out.println("Item added # " + (i+1) + " added...");
					}


					conn.close();
				}
				catch (Exception e2)
				{
					System.err.println("Got an exception!");
					System.err.println(e2.getMessage());
				}

			}
		});
		btnAddToDbse.setBounds(333, 84, 91, 23);
		contentPane.add(btnAddToDbse);


		setVisible(true);
	}

}