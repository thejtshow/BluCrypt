import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class BluCrypt {

	private JFrame frmBluecryptWindowsClient;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BluCrypt window = new BluCrypt();
					window.frmBluecryptWindowsClient.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BluCrypt() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//make the frame
		frmBluecryptWindowsClient = new JFrame();
		frmBluecryptWindowsClient.setTitle("BlueCrypt");
		frmBluecryptWindowsClient.setBounds(100, 100, 450, 162);
		frmBluecryptWindowsClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmBluecryptWindowsClient.setLocationRelativeTo(null);
		
		JPanel panel = new JPanel();
		frmBluecryptWindowsClient.getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JLabel lblBlucryptWindowsClient = new JLabel("BluCrypt Windows Client");
		lblBlucryptWindowsClient.setBounds(0, 0, 434, 29);
		lblBlucryptWindowsClient.setFont(new Font("Tahoma", Font.PLAIN, 24));
		lblBlucryptWindowsClient.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(lblBlucryptWindowsClient);
		
		JButton button = new JButton("Pair");
		button.setBounds(89, 40, 260, 23);
		panel.add(button);
		
		JButton btnAbout = new JButton("About");
		btnAbout.setBounds(89, 74, 260, 23);
		btnAbout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JOptionPane.showMessageDialog(null, 
						"Original idea by Justin Davis\n"
						+ "Initial concept proven by Justin Davis, Manoj Seeram, Kody Taylor, and Josh Lewis\n"
						+ "Final app and Windows client designed by Justin Davis, Ang Li, Shiming Li, and Tingxin Yan");
			}
		});
		panel.add(btnAbout);
	}

}
