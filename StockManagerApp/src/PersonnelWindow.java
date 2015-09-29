import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.eclipse.wb.swing.FocusTraversalOnArray;

public class PersonnelWindow extends JFrame {

	private JPanel contentPane;
	private JTextField txtSearchFor;

	/**
	 * Create the frame.
	 */
	public PersonnelWindow () {
		setTitle("Personnel");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		try	{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 200, 251);
		contentPane.add(scrollPane);
		
		JTree tree = new JTree();
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("All Personnel") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("Managers");
						node_1.add(new DefaultMutableTreeNode("John Smith"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("Staff");
						node_1.add(new DefaultMutableTreeNode("Alan Smith"));
						node_1.add(new DefaultMutableTreeNode("Bridget Smith"));
						node_1.add(new DefaultMutableTreeNode("Colin Smith"));
						node_1.add(new DefaultMutableTreeNode("Daisy Smith"));
					add(node_1);
				}
			}
		));
		scrollPane.setViewportView(tree);
		
		JButton btnNewButton = new JButton("Timesheets");
		btnNewButton.setBounds(341, 205, 91, 23);
		contentPane.add(btnNewButton);
		
		JButton btnEdit = new JButton("Edit");
		btnEdit.setBounds(341, 171, 91, 23);
		contentPane.add(btnEdit);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(341, 137, 91, 23);
		contentPane.add(btnAdd);
		
		JButton btnHolidays = new JButton("Holidays");
		btnHolidays.setBounds(341, 239, 91, 23);
		contentPane.add(btnHolidays);
		
		txtSearchFor = new JTextField();
		txtSearchFor.setText("Search for...");
		txtSearchFor.setToolTipText("Enter the search term here and press ENTER");
		txtSearchFor.setBounds(220, 39, 168, 23);
		contentPane.add(txtSearchFor);
		txtSearchFor.setColumns(10);
		
		JButton btnX = new JButton("X");
		btnX.setToolTipText("Clear the search query");
		btnX.setBounds(391, 39, 41, 23);
		contentPane.add(btnX);
		
		JLabel lblQuickFind = new JLabel("Quick Find");
		lblQuickFind.setBounds(220, 14, 74, 14);
		contentPane.add(lblQuickFind);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{tree, txtSearchFor, btnX, btnAdd, btnEdit, btnNewButton, btnHolidays}));
		
		setVisible(true);
	}
}
