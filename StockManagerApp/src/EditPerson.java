import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

public class EditPerson extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main (String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run () {
				try {
					EditPerson frame = new EditPerson();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public EditPerson () {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
		
		JLabel lblPersonnelCod = new JLabel("Personnel code");
		lblPersonnelCod.setBounds(10, 11, 89, 14);
		contentPane.add(lblPersonnelCod);
	}
}
