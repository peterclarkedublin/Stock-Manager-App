import java.awt.Component;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Stack;

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

public class LocationsWindow extends JFrame {

	private JPanel contentPane;
	private Connection conn;

	public LocationsWindow (String db) {
		// create a mysql database connection
		String myDriver = "org.gjt.mm.mysql.Driver";
		String myUrl = "jdbc:mysql://192.168.1.160/" + db;
		try {
			Class.forName(myDriver);
			conn = DriverManager.getConnection(myUrl, "smadmin", "admin");
		} catch (SQLException sqle) {
			System.out.println(sqle);
			return;
		} catch (Exception e) {
			System.out.println(e);
			return;
		}
		System.out.println("Connected to database " + db + " as connection " + conn);

		setTitle("Locations");
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
		
		
		/*
		 * For top level hierarchy, select * from location where parent = 0;
		 * Add those results as top level nodes. Then select * from location where parent = node_id
		 * and add those results to each of their parent nodes
		 */
		
		JTree tree = new JTree();
		try {
			DefaultMutableTreeNode nn;

			System.out.println("Querying connection " + conn);
			ResultSet rs = conn.createStatement().executeQuery("select * from location where parent_id is null");
			Stack levels = new Stack();
			if (rs.next()) {

				/*
				 * Define a class for keeping track of the parent TreeNodes.
				 * For each new location id, an instance of this class will be made for it
				 * so that the location id and TreeNode can be pushed on a stack.
				 * Subsequent queries will search for locations which have these
				 * location ids and will be added into the associated TreeNode
				 */ 
				class LocationNode {
					int id;
					DefaultMutableTreeNode node;
					LocationNode (int id, DefaultMutableTreeNode node) {
						this.id = id;
						this.node = node;
					}
				}
				
				System.out.println("Populating treeview");
				
				// Create the master tree node
				DefaultMutableTreeNode tn = new DefaultMutableTreeNode("All locations");
				
				// Add nodes for each top level location
				do {
					// Create a new TreeNode for this location
					nn = new DefaultMutableTreeNode(rs.getString(2));
					// If this is a new node id, push it on the stack
					if (levels.search(rs.getString(1)) == 0) levels.push(new LocationNode(rs.getInt(1), nn));
					// Add the node to the tree
					tn.add (nn);
				} while (rs.next());
				
				tree.setModel(new DefaultTreeModel(tn));

				scrollPane.setViewportView(tree);
			}
		} catch (Exception e) {
			System.out.print("\u001b[33;1m");
			e.printStackTrace();
		}
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(341, 10, 91, 23);
		contentPane.add(btnAdd);
		
		JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(341, 44, 91, 23);
		contentPane.add(btnRemove);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(341, 78, 91, 23);
		contentPane.add(btnEdit);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tree, btnAdd, btnRemove, btnEdit}));
		
		setVisible(true);
	}
}
