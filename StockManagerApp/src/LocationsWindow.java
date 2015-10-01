import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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
 * A window which displays all the locations in the database and allows the
 * user to add, modify or delete items in this table.
 * 
 * @author Kevin Phair
 * @date 30 Sep 2015
 */
public class LocationsWindow extends JFrame {

	private JPanel contentPane;
	private TNode selectedTNode;

	public LocationsWindow (String db) {
		final Connection conn = StockUtil.openDb();
		if (conn == null) {
			JOptionPane.showMessageDialog (null, 
					"There was an error opening the database\n"
					+ "Please check the server and/or network connection", 
					"Database connection error",
					JOptionPane.NO_OPTION);
			return;
		};
		
		System.out.println("Connected to database " + db + " as connection " + conn);

		setTitle("Locations");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		scrollPane.setBounds(10, 11, 321, 251);
		contentPane.add(scrollPane);
		
		final JTree tree = new JTree();
		tree.addTreeSelectionListener(new TreeSelectionListener() {
			public void valueChanged(TreeSelectionEvent arg0) {
				TreePath path = ((JTree)arg0.getSource()).getSelectionPath();
				if (path != null) {
					DefaultMutableTreeNode tn = (DefaultMutableTreeNode)path.getLastPathComponent();
					selectedTNode = (TNode)tn.getUserObject();
				}
			}
		});
		tree.setModel(new DefaultTreeModel(createTreeModel(conn)));
		scrollPane.setViewportView(tree);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("Getting new location...");
				new AddLocation (conn, selectedTNode);
				System.out.println("Updating JTree");
				tree.setModel(new DefaultTreeModel(createTreeModel(conn)));
			}
		});
		btnAdd.setBounds(341, 10, 91, 23);
		contentPane.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Give the user a chance to cancel
				String q = "delete from location where id = " + selectedTNode.getId() + " or parent_id = " + selectedTNode.getId();
				System.out.println("About to execute " + q);
				if (JOptionPane.showConfirmDialog (null, 
						"This operation will delete the selected\n"
						+ "location AND ALL SUB-LOCATIONS.\n"
						+ "It cannot be undone. Proceed?",
						"Confirm delete location", 
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0) return;
				// Delete the location and refresh the tree
				try {
					conn.prepareStatement(q).execute();
					tree.setModel(new DefaultTreeModel(createTreeModel(conn)));
				} catch (SQLException e1) {
					JOptionPane.showMessageDialog (null, 
							"There was an error deleting the location:\n"
							+ e1.toString() + "\n"
							+ "This operation has been cancelled",
							"SQL error deleting location", 
							JOptionPane.NO_OPTION);
				}
				return;

			}
		});
		btnRemove.setBounds(341, 44, 91, 23);
		contentPane.add(btnRemove);
		
		JButton btnEdit = new JButton("Amend");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new AmendLocation(conn, selectedTNode);
				System.out.println("Node id " + selectedTNode.getId());
				if (selectedTNode.getId() < 0) {
					tree.setModel(new DefaultTreeModel(createTreeModel(conn)));
				}
			}
		});
		btnEdit.setBounds(341, 78, 91, 23);
		contentPane.add(btnEdit);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tree, btnAdd, btnRemove, btnEdit}));
		
		setVisible(true);
	}
	
	/**
	 * createTreeModel
	 * Create a TreeNode hierarchy which can be passed into the JTree's setModel method.
	 * For the top level hierarchy, "select * from location where parent = 0".
	 * Add those results as top level nodes. Then select * from location where parent = node_id
	 * and add those results to each of their parent nodes.
	 * A stack is used to recusively scan the hierarchy in the database tables.
	 * 
	 * @param the database connection currently open by the caller
	 * @return a DefaultMutableTreeNode which links all the location nodes together
	 */
	public static DefaultMutableTreeNode createTreeModel (final Connection conn) {
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
					System.out.println("Initialising stack and creating master tree node");
					levels = new Stack<LocationNode>();
					rs = conn.createStatement().executeQuery("select * from location where parent_id = 0");
					
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
			return mn;
			
		} catch (Exception e) {
			System.out.print("\u001b[33;1m");
			e.printStackTrace();
			return null;
		}
		
	}
}

/**
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

/**
 * A placeholder class so that a TreeNode in the JTree's data model can hold the 
 * location's row id column as well as the string that's displayed on screen
 */
class TNode {
	private int id;
	private String descr;
	TNode (int id, String descr) { this.id = id; this.descr = descr; }
	public String toString () { return this.descr; }
	public int getId () { return this.id; } 
	public void invalidate () { this.id = -1; }
}

