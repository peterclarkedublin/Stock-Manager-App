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
		scrollPane.setViewportView(table);
		
		JButton btnExamine = new JButton("Examine");
		btnExamine.setBounds(341, 11, 91, 23);
		contentPane.add(btnExamine);
		
		JButton btnAdjust = new JButton("Adjust");
		btnAdjust.setBounds(341, 45, 91, 23);
		contentPane.add(btnAdjust);
		contentPane.setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{btnExamine, btnAdjust}));
		
		setVisible(true);
	}
}
