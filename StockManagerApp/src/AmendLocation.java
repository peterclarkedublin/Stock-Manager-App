import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.nio.channels.SelectableChannel;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.Dialog.ModalityType;

public class AmendLocation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private AmendLocation dialog;
	private int newParent;
	private int oldParent;
	private String newDescr;
	private String oldDescr;
	private JButton btnLocation;

	/**
	 * Create the dialog.
	 */
	public AmendLocation (final Connection conn, final TNode selectedTNode) {
		dialog = this;
		setTitle("Amend Location");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 383, 154);
		setModalityType(ModalityType.APPLICATION_MODAL);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 89, 375, 38);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String q = null;
						newDescr = textField.getText();
						// If the location has moved but name is the same
						if (oldParent != newParent && oldDescr.equals(newDescr)) {
							q = "update location set parent_id = " + newParent + " where id = " + selectedTNode.getId();

						// if the location hasn't moved, but name has changed
						} else if (oldParent == newParent && !(oldDescr.equals(newDescr))) {
							q = "update location set descr = \"" + newDescr + "\" where id = " + selectedTNode.getId();

						// If the location has moved and the name has changed
						} else if (oldParent != newParent && !(oldDescr.equals(newDescr))) {
							q = "update location set parent_id = " + newParent + ", descr = \"" + newDescr + "\" where id = " + selectedTNode.getId();
						}
						System.out.println("About to execute " + q);

						if (q != null) {
							// Give the user a chance to cancel
							if (JOptionPane.showConfirmDialog (null, 
									"You are about to rename or move a location\n"
									+ "This will affect all items currently at that location\n"
									+ "as well as all sub-locations.",
									"Confirm delete location", 
									JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) != 0) return;
							try {
								conn.prepareStatement(q).execute();
								selectedTNode.invalidate();
								//ResultSet rs = conn.createStatement().executeQuery(q);
							} catch (SQLException sqle) {
								sqle.printStackTrace();
							}
						}
						dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
					}
				});
				okButton.setBounds(10, 5, 90, 23);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispatchEvent(new WindowEvent(dialog, WindowEvent.WINDOW_CLOSING));
					}
				});
				cancelButton.setBounds(275, 5, 90, 23);
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
		JLabel lblDescription = new JLabel("Description");
		lblDescription.setBounds(10, 11, 95, 14);
		contentPanel.add(lblDescription);
		
		textField = new JTextField();
		textField.setToolTipText("Current description of this location");
		textField.setBounds(104, 8, 262, 20);
		oldDescr = selectedTNode.toString();
		newDescr = oldDescr;
		textField.setText(oldDescr);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblParentLocation = new JLabel("Parent location");
		lblParentLocation.setBounds(10, 49, 95, 14);
		contentPanel.add(lblParentLocation);
		
		btnLocation = new JButton("Current location");
		btnLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				newParent = StockUtil.getLocation();
				btnLocation.setText(getLocationName (conn, newParent));
			}
		});
		btnLocation.setToolTipText("Click to move this location in the hierarchy");
		btnLocation.setBounds(104, 45, 262, 23);
		oldParent = getParentId (conn, selectedTNode.getId() );
		newParent = oldParent;
		btnLocation.setText(getLocationName (conn, oldParent ) );
		contentPanel.add(btnLocation);

		setVisible(true);

	}

	public String getLocationName (Connection conn, int id) {
		
		if (id == 0) {
			return "All Locations";
		} else {
			try {
				ResultSet rs = conn.createStatement().executeQuery("select descr from location where id = " + id);
				if (rs.next()) {
					return rs.getString(1);
				} else {
					return "<SQL query failed>";
				}
			} catch (SQLException sqle) {
				sqle.printStackTrace();
				return "<SQL error>";
			}
		}

	}

	/**
	 * Return the parent id of a location
	 * 
	 * @param conn
	 * @param id
	 * @return
	 */
	public int getParentId (Connection conn, int id) {
		
		try {
			ResultSet rs = conn.createStatement().executeQuery("select parent_id from location where id = " + id);
			if (rs.next()) {
				return rs.getInt(1);
			} else {
				return -1;
			}
		} catch (SQLException sqle) {
			sqle.printStackTrace();
			return -1;
		}
	}

}

