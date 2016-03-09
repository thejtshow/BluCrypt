import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.FlowLayout;
import javax.swing.JButton;
import java.awt.GridLayout;
import java.awt.TextArea;

import javax.bluetooth.RemoteDevice;
import javax.bluetooth.UUID;
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
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class BlueCryptPC {
	
	private UUID uuid; //service serial port uuid
	private String connectionString;
	private StreamConnectionNotifier streamConnectionNotifier;
	private StreamConnection streamConnection;
	private RemoteDevice remoteDevice;
	private String readMessage;
	private String sendMessage;

	private JFrame frmBlurcrypt;

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
		btnStart.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				System.out.println("show...");
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
		GroupLayout groupLayout = new GroupLayout(frmBlurcrypt.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 393, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnStart)
						.addComponent(lblLog))
					.addContainerGap(27, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnStart)
					.addGap(74)
					.addComponent(lblLog)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(textArea, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(40, Short.MAX_VALUE))
		);
		frmBlurcrypt.getContentPane().setLayout(groupLayout);
	}
}
