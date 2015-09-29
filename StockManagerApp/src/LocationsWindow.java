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

public class LocationsWindow extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public LocationsWindow () {
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
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("All Locations") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Main Warehouse");
						node_1.add(new DefaultMutableTreeNode("Aisle 1"));
						node_1.add(new DefaultMutableTreeNode("Aisle 2"));
						node_1.add(new DefaultMutableTreeNode("Aisle 3"));
						node_1.add(new DefaultMutableTreeNode("Aisle 4"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Shop Stores");
						node_1.add(new DefaultMutableTreeNode("Stock Room 1"));
						node_1.add(new DefaultMutableTreeNode("Stock Room 2"));
						node_1.add(new DefaultMutableTreeNode("Returns"));
						node_1.add(new DefaultMutableTreeNode("Orders"));
					add(node_1);
				}
			}
		));
		scrollPane.setViewportView(tree);
		
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
