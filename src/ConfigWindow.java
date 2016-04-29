import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

public class ConfigWindow {

	private JFrame frmConfiguration;
	private static Credential credential;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ConfigWindow window = new ConfigWindow();
//					window.frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the application.
	 */
	public ConfigWindow(Credential credential) {
		this.credential=credential;
		initialize();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
	}

	public void setDefaultCloseOperation(int operation) {
		frmConfiguration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void setVisible(boolean b) {
		frmConfiguration.setVisible(b);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmConfiguration = new JFrame();
		frmConfiguration.setTitle("Configuration");
		frmConfiguration.setBounds(100, 100, 450, 300);
		frmConfiguration.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("Encrypt");
		btnNewButton.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				JFileChooser jfc=new JFileChooser();  
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
		        jfc.showDialog(new JLabel(), "Encrypt");  
		        File file=jfc.getSelectedFile();  
		        if(file.isDirectory()){  
		            System.out.println("Folder:"+file.getAbsolutePath());  
		            try {
						DataUtil.insertDirectory(credential, file.getAbsolutePath());
						String [] folderPath = DataUtil.getFolderPathArray(credential);
						CryptUtil cryptUtil = new CryptUtil(folderPath, "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123", credential);
						cryptUtil.generateKey();
						cryptUtil.encryptFile();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }else if(file.isFile()){  
		            System.out.println("File:"+file.getAbsolutePath()); 
		            try {
						DataUtil.insertDirectory(credential, file.getAbsolutePath());
						String [] folderPath = DataUtil.getFolderPathArray(credential);
						CryptUtil cryptUtil = new CryptUtil(folderPath, "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123", credential);
						cryptUtil.generateKey();
						cryptUtil.encryptFile();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }  
		        System.out.println(jfc.getSelectedFile().getName());
		        
		        
			}
			
		});
		
		GroupLayout groupLayout = new GroupLayout(frmConfiguration.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 132, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(288, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton)
					.addContainerGap(215, Short.MAX_VALUE))
		);
		frmConfiguration.getContentPane().setLayout(groupLayout);
		
		
	}

}
