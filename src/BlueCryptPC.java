import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.GridLayout;
import java.awt.TextArea;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
import javax.crypto.Cipher;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;

import org.omg.CORBA.PUBLIC_MEMBER;

import javax.swing.JLabel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BlueCryptPC {
	
	private UUID uuid; //service serial port uuid
	private String connectionString;
	private StreamConnectionNotifier streamConnectionNotifier;
	private StreamConnection streamConnection;
	private RemoteDevice remoteDevice;
	private String readMessage;
	private String sendMessage;

	private JFrame frmBlurcrypt;
	private JButton btnEncrypt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BlueCryptPC window = new BlueCryptPC();
					window.frmBlurcrypt.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public BlueCryptPC() {
		initialize();
	}
	private void startServer() throws IOException{
		
		uuid = new UUID("1101",true);
		connectionString="btspp://localhost:"+uuid+";name=BlueCryptPC";
		streamConnectionNotifier=(StreamConnectionNotifier)Connector.open(connectionString);
		System.out.println("Waiting for client...");
		streamConnection=streamConnectionNotifier.acceptAndOpen();
		System.out.println("Successfully connected...");
	
		remoteDevice=RemoteDevice.getRemoteDevice(streamConnection);
		System.out.println("Remote device address: "+remoteDevice.getBluetoothAddress());
	    System.out.println("Remote device name: "+remoteDevice.getFriendlyName(true));

	    InputStream inputStream=streamConnection.openInputStream();
		BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream));
		readMessage=bufferedReader.readLine();
		System.out.println(readMessage);
		
		sendMessage="I got it!";
		OutputStream outputStream=streamConnection.openOutputStream();
		PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(outputStream));
		printWriter.write(sendMessage);
		printWriter.flush();
		printWriter.close();
	    
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmBlurcrypt = new JFrame();
		frmBlurcrypt.setTitle("BlurCrypt");
		frmBlurcrypt.setBounds(100, 100, 450, 300);
		frmBlurcrypt.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnStart = new JButton("Start");
		JTextArea textArea = new JTextArea();
		frmBlurcrypt.setDefaultCloseOperation(3);
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				textArea.append("Start listening..."+"\n");
				 try {
					 startServer();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		
		JLabel lblLog = new JLabel("Log:");
		
		JButton btnChoose = new JButton("Encrypt");
		btnChoose.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc=new JFileChooser();  
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
		        jfc.showDialog(new JLabel(), "Encrypt");  
		        File file=jfc.getSelectedFile();  
		        if(file.isDirectory()){  
		            System.out.println("Folder:"+file.getAbsolutePath());  
		        }else if(file.isFile()){  
		            System.out.println("File:"+file.getAbsolutePath());  
		        }  
		        System.out.println(jfc.getSelectedFile().getName());
		        CryptUtil cryptUtil=new CryptUtil(file.getAbsolutePath().replaceAll("\\\\", "/"), "C:\\Users\\Owner\\Desktop\\keyfolder\\", "zx9956123");
		        try {
		        	cryptUtil.generateKey();
					//cryptUtil.loadKey();
					cryptUtil.encryptFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        
			}
		});
		
		JButton btnEncrypt;
		btnEncrypt = new JButton("Encrypt");
		btnEncrypt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc=new JFileChooser();  
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
		        jfc.showDialog(new JLabel(), "Encrypt");  
		        File file=jfc.getSelectedFile();  
		        if(file.isDirectory()){  
		            System.out.println("Folder:"+file.getAbsolutePath());  
		        }else if(file.isFile()){  
		            System.out.println("File:"+file.getAbsolutePath());  
		        }  
		        System.out.println(jfc.getSelectedFile().getName());
		        CryptUtil cryptUtil=new CryptUtil(file.getAbsolutePath().replaceAll("\\\\", "/"), "C:\\Users\\Owner\\Desktop\\keyfolder\\", "zx9956123");
		        try {
		        	cryptUtil.generateKey();
					//cryptUtil.loadKey();
					cryptUtil.encryptFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        
			}
		});
		
		JButton btnDecrypt = new JButton("Decrypt");
		btnDecrypt.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				JFileChooser jfc=new JFileChooser();  
		        jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );  
		        jfc.showDialog(new JLabel(), "Decrypt");  
		        File file=jfc.getSelectedFile();  
		        if(file.isDirectory()){  
		            System.out.println("Folder:"+file.getAbsolutePath());  
		        }else if(file.isFile()){  
		            System.out.println("File:"+file.getAbsolutePath());  
		        }  
		        System.out.println(jfc.getSelectedFile().getName());
		        CryptUtil cryptUtil=new CryptUtil(file.getAbsolutePath().replaceAll("\\\\", "/"), "C:\\Users\\Owner\\Desktop\\keyfolder\\", "zx9956123");
		        try {       	
					cryptUtil.loadKey();
					cryptUtil.decryptionFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
		        
			}
		});
		

		GroupLayout groupLayout = new GroupLayout(frmBlurcrypt.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnStart)
							.addGap(18)
							.addComponent(btnEncrypt)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnDecrypt, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
						.addComponent(lblLog))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnStart)
						.addComponent(btnEncrypt)
						.addComponent(btnDecrypt))
					.addGap(74)
					.addComponent(lblLog)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		frmBlurcrypt.getContentPane().setLayout(groupLayout);
	}
}
