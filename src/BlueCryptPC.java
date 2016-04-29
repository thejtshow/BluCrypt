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
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
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
	
	private static final int STATE_WAIT_FOR_PAIR=0;
	private static final int STATE_PAIRED=1;
	private static final int STATE_DISCONNECTED=2;
	private static final int STATE_CONFIG=3;
	
	private static Credential credential = new Credential();

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
	private void startServer() throws Exception{
		
		uuid = new UUID("1101",true);
		connectionString="btspp://localhost:"+uuid+";name=BlueCryptPC";
		streamConnectionNotifier=(StreamConnectionNotifier)Connector.open(connectionString);
		System.out.println("Waiting for client...");
		streamConnection=streamConnectionNotifier.acceptAndOpen();
		System.out.println("Successfully connected...");
	
		remoteDevice=RemoteDevice.getRemoteDevice(streamConnection);
		System.out.println("Remote device address: "+remoteDevice.getBluetoothAddress());
	    System.out.println("Remote device name: "+remoteDevice.getFriendlyName(true));

	    
	    ReadThread readThread=new ReadThread(streamConnection, readMessage);
	    readThread.start();

	  
	    
		//receive message 1 from client, initial a credential for the client
		
		/*JSONUtil.parseJSON(readMessage, credential);
		String response_success = JSONUtil.generateJSON("Success");
		String response_fail = JSONUtil.generateJSON("Fail, you are a new user!");*/
		
		
		//send feedback to client;
		/*WriteThread writeThread=new WriteThread("Success!", streamConnection);
		writeThread.start();*/
		/*OutputStream outputStream=streamConnection.openOutputStream();
		PrintWriter printWriter=new PrintWriter(new OutputStreamWriter(outputStream));
		printWriter.write(response_success);*/
		/*if (DataUtil.checkCredential(credential)) {
			printWriter.write(response_success);
		}else {
			printWriter.write(response_fail);
			DataUtil.inserCredential(credential);
		}
		printWriter.flush();
		printWriter.close();
	    */
	}

	public static byte[] readStream(InputStream inStream) throws Exception {  
	    ByteArrayOutputStream outSteam = new ByteArrayOutputStream();  
	    byte[] buffer = new byte[1024];  
	    int len = -1;  
	    while ((len = inStream.read(buffer)) != -1) {  
	        outSteam.write(buffer, 0, len);  
	    }  
	    outSteam.close();  
	    inStream.close();  
	    return outSteam.toByteArray();  
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
				} catch (Exception e) {
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
		        
		       /* try {
		        	CryptUtil cryptUtil=new CryptUtil(file.getAbsolutePath().replaceAll("\\\\", "/"), "C:\\Users\\Owner\\Desktop\\keyfolder\\", "zx9956123",credential);
		        	cryptUtil.generateKey();
					//cryptUtil.loadKey();
					cryptUtil.encryptFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
		        
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
		        
		        /*try {
		        	CryptUtil cryptUtil=new CryptUtil(file.getAbsolutePath().replaceAll("\\\\", "/"), "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123",credential);
		        	cryptUtil.generateKey();
					cryptUtil.encryptFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
		        
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
		        
		        /*try { 
		        	CryptUtil cryptUtil=new CryptUtil(file.getAbsolutePath().replaceAll("\\\\", "/"), "C:\\Users\\charles\\Desktop\\keyfolder\\", "zx9956123",credential);
					cryptUtil.loadKey();
					cryptUtil.decryptionFile();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} */
		        
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
