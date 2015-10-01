import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.eclipse.wb.swing.FocusTraversalOnArray;

/**
 * LocationPicker
 * Provides a convenience class that can be called by any window to bring up a
 * dialog from which a location can be selected.
 * 
 * The method to call for using this class is StockUtil.getLocation() which will 
 * open this window and retrieve the id of the row in the location table containing
 * the location which was selected (-1 if no location was selected)
 * 
 * This class opens a new database connection so the caller does not need to
 * have an active connection ready just to get a location.
 * 
 * @see StockUtil
 * @author Kevin Phair
 * @date 30 Sep 2015
 */
@SuppressWarnings("serial")
public class LocationPicker extends JDialog {

	private JPanel contentPane;
	private JDialog pickerWindow;
	private JButton btnOK;
	private Connection conn;

	public LocationPicker (String db, final IntResult result) {
		
		result.i = -1;
		pickerWindow = this;
		
		conn = StockUtil.openDb();
		
		System.out.println("Connected to database " + db + " as connection " + conn);

		setTitle("Select a location");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 350, 387);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setResizable(false);
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
		scrollPane.setBounds(10, 11, 321, 304);
		contentPane.add(scrollPane);
	
		JTree tree = new JTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath path = ((JTree)arg0.getSource()).getSelectionPath();
				DefaultMutableTreeNode tn = (DefaultMutableTreeNode)path.getLastPathComponent();
				result.i = ((TNode)tn.getUserObject()).getId();
				System.out.println("Tree selection path : " + result.i);
				btnOK.setEnabled(true);
			}
		});
		tree.setModel(new DefaultTreeModel(LocationsWindow.createTreeModel(conn)));
		scrollPane.setViewportView(tree);
		
		btnOK = new JButton("OK");
		btnOK.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispatchEvent(new WindowEvent(pickerWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnOK.setEnabled(false);
		btnOK.setBounds(10, 326, 91, 23);
		contentPane.add(btnOK);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispatchEvent(new WindowEvent(pickerWindow, WindowEvent.WINDOW_CLOSING));
			}
		});
		btnCancel.setBounds(240, 326, 91, 23);
		contentPane.add(btnCancel);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tree, btnOK, btnCancel}));
		
		setVisible(true);
		pickerWindow = this;
	}

}

/**
 * A placeholder class to hold the result from the picker window. Can't use
 * Integer class because that's immutable and changes to the result value
 * would not be passed back due to the original Integer object being discarded
 * 
 * @author Kevin Phair
 * @date 30 Sep 2015
 */
class IntResult {
	int i;
	public IntResult () { this.i = -1; }
}

