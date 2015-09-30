import java.awt.BorderLayout;
import java.awt.Font;
import java.sql.Connection;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class AddLocation extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JDialog window;
	private Connection conn;

	/**
	 * Create the dialog.
	 */
	public AddLocation (Connection c, final TNode parentNode) {
		window = this;
		this.conn = c;
		setTitle("Add new location");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 381, 171);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 104, 373, 40);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(null);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						try {
							System.out.println("insert into location (descr, parent_id) values (" + textField.getText() + ")");
							conn.prepareStatement("insert into location (descr, parent_id) values (\"" + textField.getText() + "\"," + parentNode.getId() + ")").execute();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
					}
				});
				okButton.setBounds(10, 11, 90, 23);
				buttonPane.add(okButton);
				okButton.setActionCommand("OK");
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispatchEvent(new WindowEvent(window, WindowEvent.WINDOW_CLOSING));
					}
				});
				cancelButton.setBounds(273, 11, 90, 23);
				buttonPane.add(cancelButton);
				cancelButton.setActionCommand("Cancel");
			}
		}
		
		JLabel lblNoteThisLocation = new JLabel("Note: This location will be added as a child of");
		lblNoteThisLocation.setBounds(13, 15, 230, 14);
		contentPanel.add(lblNoteThisLocation);
		
		JLabel lblLocationDescription = new JLabel("Description");
		lblLocationDescription.setBounds(10, 76, 67, 14);
		contentPanel.add(lblLocationDescription);
		
		textField = new JTextField();
		textField.setBounds(87, 73, 276, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel(parentNode.toString());
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel.setBounds(91, 40, 272, 14);
		contentPanel.add(lblNewLabel);

		setVisible(true);
	}
}
