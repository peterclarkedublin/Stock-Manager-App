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

@SuppressWarnings("serial")
public class LocationPicker extends JDialog {

	private JPanel contentPane;
	private Connection conn;
	private JDialog pickerWindow;
	private JButton btnOK;

	public LocationPicker (String db, final IntResult result) {
		
		result.i = -1;
		pickerWindow = this;
		
		// create a mysql database connection
		String myDriver = "org.gjt.mm.mysql.Driver";
		String myUrl = "jdbc:mysql://192.168.1.160/" + db;
		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "smadmin", "admin");
		} catch (SQLException sqle) {
			System.out.println(sqle);
			result.i = -2;
			return;
		} catch (Exception e) {
			System.out.println(e);
			result.i = -2;
			return;
		}
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
		
		
		/*
		 * For top level hierarchy, select * from location where parent = 0;
		 * Add those results as top level nodes. Then select * from location where parent = node_id
		 * and add those results to each of their parent nodes
		 */
		
		JTree tree = new JTree();
		
		/*
		 * Define a class for keeping track of the parent TreeNodes.
		 * For each new location id, an instance of this class will be made for it
		 * so that the location id and TreeNode can be pushed on a stack.
		 * Subsequent queries will search for locations which have these
		 * location ids and will be added into the associated TreeNode
		 */ 
		class LocationNode {
			private int id;
			private DefaultMutableTreeNode node;
			LocationNode (int id, DefaultMutableTreeNode node) {
				this.id = id;
				this.node = node;
			}
			public int getId () { return this.id; }
			public DefaultMutableTreeNode getNode () { return this.node; }
			public String toString () { return String.valueOf(this.node); }
		}
		
		class TNode {
			private int id;
			private String descr;
			TNode (int id, String descr) { this.id = id; this.descr = descr; }
			public String toString () { return this.descr; }
			public int getId () { return this.id; } 
		}
		
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath path = ((JTree)arg0.getSource()).getSelectionPath();
				DefaultMutableTreeNode tn = (DefaultMutableTreeNode)path.getLastPathComponent();
				result.i = ((TNode)tn.getUserObject()).getId();
				System.out.println("Tree selection path : " + result.i);
				btnOK.setEnabled(true);
			}
		});
		
		try {
			DefaultMutableTreeNode nn;

			System.out.println("Querying connection " + conn);
			Stack<LocationNode> levels = null;
			LocationNode levelNode = null;
			ResultSet rs = null;
			
			// Create the master tree node
			DefaultMutableTreeNode mn = new DefaultMutableTreeNode(new TNode (0, "All locations"));
			// top node which will track for the parent node as the recursive search progresses
			DefaultMutableTreeNode tn = mn;

			do {
				if (levels == null) {
					// First go around, initialise the stack and get the top level locations
					levels = new Stack<LocationNode>();
					rs = conn.createStatement().executeQuery("select * from location where parent_id is null");
					System.out.println("Initialising stack and creating master tree node");
					
				} else {
					// on successive iterations, pop a location off the stack and find children of it
					levelNode = levels.pop();
					tn = levelNode.getNode();
					System.out.println("Popped " + levelNode.getId() + ":" + tn.getUserObject() + " off the stack");
					System.out.println("SQL 'select * from location where parent_id = " + levelNode.getId() + "'");
					rs = conn.createStatement().executeQuery("select * from location where parent_id = " + levelNode.getId());
				}

				if (rs.next()) {

					System.out.println("Populating treeview");
					
					// Now add location nodes
					do {
						// Create a new TreeNode for this location
						nn = new DefaultMutableTreeNode(new TNode (rs.getInt(1), rs.getString(2)));
						
						// If this is a new node id, push it on the stack
						//if (levels.search(rs.getString(1)) == 0) {
							System.out.println("Pushing " + rs.getString(1) + " onto stack");
							levels.push(new LocationNode(rs.getInt(1), nn));
						//}
						
						// Add the node to the tree
						tn.add (nn);
						
					} while (rs.next());
				}
			
			} while (! levels.empty());

			tree.setModel(new DefaultTreeModel(mn));

			scrollPane.setViewportView(tree);
			
		} catch (Exception e) {
			System.out.print("\u001b[33;1m");
			e.printStackTrace();
		}
		
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

class IntResult {
	int i;
	public IntResult () { this.i = -1; }
}

