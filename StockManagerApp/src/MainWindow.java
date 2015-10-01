import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.UIManager;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;
import java.awt.SystemColor;

public class MainWindow {

	private JFrame frmStockManagementApplication;
	private JTable tblCalendar;

	/**
	 * Launch the application.
	 */
	public static void main (String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				try {
					MainWindow window = new MainWindow();
					window.frmStockManagementApplication.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow () {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize () {
		
		frmStockManagementApplication = new JFrame();
		frmStockManagementApplication.setTitle("Stock Management Application");
		frmStockManagementApplication.setBounds(100, 100, 365, 302);
		frmStockManagementApplication.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmStockManagementApplication.getContentPane().setLayout(null);
		frmStockManagementApplication.setResizable(false);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		JButton btnLocations = new JButton("Locations");
		btnLocations.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new LocationsWindow("stockdb");
			}
		});
		
		JLabel lblUstock = new JLabel("\u00FCStock");
		lblUstock.setHorizontalAlignment(SwingConstants.CENTER);
		lblUstock.setFont(new Font("Tahoma", Font.BOLD, 22));
		lblUstock.setBounds(10, 11, 186, 32);
		frmStockManagementApplication.getContentPane().add(lblUstock);
		
		JLabel lblMonth1 = new JLabel("October");
		lblMonth1.setHorizontalAlignment(SwingConstants.CENTER);
		lblMonth1.setBounds(10, 71, 186, 14);
		frmStockManagementApplication.getContentPane().add(lblMonth1);
		
		tblCalendar = new JTable();
		tblCalendar.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		tblCalendar.setShowGrid(false);
		tblCalendar.setRowSelectionAllowed(false);
		tblCalendar.setBounds(10, 96, 186, 106);
		frmStockManagementApplication.getContentPane().add(tblCalendar);
		tblCalendar.setBackground(SystemColor.control);
		tblCalendar.setModel(new DefaultTableModel(
			new Object[][] {
				{null, "M", "T", "W", "T", "F", "S", "S"},
				{null, null, null, null, "1", "2", "3", "4"},
				{null,  "5", "6", "7", "8", "9", "10", "11"},
				{null, "12", "13", "14", "15", "16", "17", "18"},
				{null, "19", "20", "21", "22", "23", "24", "25"},
				{null, "26", "27", "28", "29", "30", null, null},
			},
			new String[] {
				"New Column", "New column", "New column", "New column", "New column", "New column", "New column", "New column"
			}
		) {
			boolean[] columnEditables = new boolean[] {
				false, false, false, false, false, false, false, false
			};
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		tblCalendar.getColumnModel().getColumn(0).setResizable(false);
		tblCalendar.getColumnModel().getColumn(4).setResizable(false);
		btnLocations.setBounds(232, 11, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnLocations);
		
		JButton btnStock = new JButton("Stock Items");
		btnStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ItemsWindow();
			}
		});
		btnStock.setBounds(232, 45, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnStock);
		
		JButton btnPersonnel = new JButton("Personnel");
		btnPersonnel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new PersonnelWindow();
			}
		});
		btnPersonnel.setBounds(232, 79, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnPersonnel);
		
		JButton btnAccounting = new JButton("Accounting");
		btnAccounting.setEnabled(false);
		btnAccounting.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnAccounting.setBounds(232, 113, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnAccounting);
		
		JButton btnCustomers = new JButton("Customers");
		btnCustomers.setEnabled(false);
		btnCustomers.setBounds(232, 147, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnCustomers);
		
		JButton btnNotebook = new JButton("Notebook");
		btnNotebook.setEnabled(false);
		btnNotebook.setBounds(232, 181, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnNotebook);
		
		JButton btnAdministration = new JButton("Administration");
		btnAdministration.setEnabled(false);
		btnAdministration.setBounds(232, 241, 114, 23);
		frmStockManagementApplication.getContentPane().add(btnAdministration);
		
		JLabel lblLoginState = new JLabel("Logged-in user: J Smith");
		lblLoginState.setBounds(10, 225, 186, 14);
		frmStockManagementApplication.getContentPane().add(lblLoginState);
		
		JLabel lblTodo = new JLabel("0 Notifications, 0 Tasks");
		lblTodo.setBounds(10, 250, 186, 14);
		frmStockManagementApplication.getContentPane().add(lblTodo);
		frmStockManagementApplication.getContentPane().setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tblCalendar, btnLocations, btnStock, btnPersonnel, btnAccounting, btnCustomers, btnAdministration}));
		frmStockManagementApplication.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tblCalendar, btnLocations, btnStock, btnPersonnel, btnAccounting, btnCustomers, btnNotebook, btnAdministration}));
	}
	
}

